package com.xlei.zerocode.service.impl;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.paginate.Page;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xlei.zerocode.common.DeleteRequest;
import com.xlei.zerocode.core.AICodeGeneratorFacade;
import com.xlei.zerocode.exception.BusinessException;
import com.xlei.zerocode.exception.ErrorCode;
import com.xlei.zerocode.exception.ThrowUtils;
import com.xlei.zerocode.model.dto.app.AppAddRequest;
import com.xlei.zerocode.model.dto.app.AppAdminUpdateRequest;
import com.xlei.zerocode.model.dto.app.AppQueryRequest;
import com.xlei.zerocode.model.dto.app.AppUpdateRequest;
import com.xlei.zerocode.model.entity.App;
import com.xlei.zerocode.mapper.AppMapper;
import com.xlei.zerocode.model.entity.User;
import com.xlei.zerocode.model.enums.CodeGeneratorTypeEnum;
import com.xlei.zerocode.model.enums.UserRoleEnum;
import com.xlei.zerocode.model.vo.AppVO;
import com.xlei.zerocode.model.vo.UserVO;
import com.xlei.zerocode.service.AppService;
import com.xlei.zerocode.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 应用 服务层实现。
 *
 * @author xlei
 */
@Service
public class AppServiceImpl extends ServiceImpl<AppMapper, App>  implements AppService {

    @Resource
    private UserService userService;

    @Resource
    private AICodeGeneratorFacade aiCodeGeneratorFacade;

    @Override
    public Long addApp(AppAddRequest appAddRequest, HttpServletRequest request) {
        String initPrompt = appAddRequest.getInitPrompt();
        ThrowUtils.throwIf(StrUtil.isBlank(initPrompt), ErrorCode.PARAMS_ERROR);
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        App app = new App();
        app.setInitPrompt(appAddRequest.getInitPrompt());
        app.setUserId(loginUser.getId());
        app.setAppName(initPrompt.substring(0,Math.min(initPrompt.length(),12)));
        app.setCodeGenType(CodeGeneratorTypeEnum.HTML.getValue());
        boolean save = this.save(app);
        ThrowUtils.throwIf(!save,ErrorCode.OPERATION_ERROR);
        return app.getId();
    }

    @Override
    public Boolean updateApp(AppUpdateRequest appUpdateRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Long appId = appUpdateRequest.getId();
        App originApp = this.getById(appId);
        ThrowUtils.throwIf(originApp == null, ErrorCode.NOT_FOUND_ERROR);

        if (!originApp.getUserId().equals(loginUser.getId())){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        App app = new App();
        app.setId(appId);
        app.setAppName(appUpdateRequest.getAppName());
        app.setEditTime(LocalDateTime.now());
        boolean update = this.updateById(app);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR);
        return update;
    }

    @Override
    public Boolean deleteApp(DeleteRequest deleteRequest, HttpServletRequest request) {
        User loginUser = userService.getLoginUser(request);
        ThrowUtils.throwIf(loginUser == null, ErrorCode.NOT_LOGIN_ERROR);

        Long appId = deleteRequest.getId();
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);

        // 仅应用创建者或管理员 可删除应用
        if (!app.getUserId().equals(loginUser.getId()) && !UserRoleEnum.ADMIN.getValue().equals(loginUser.getUserRole())){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        boolean remove = this.removeById(appId);
        ThrowUtils.throwIf(!remove, ErrorCode.OPERATION_ERROR);
        return remove;
    }

    @Override
    public AppVO getAppVO(App app) {
        AppVO appVO = new AppVO();
        BeanUtils.copyProperties(app, appVO);
        Long userId = app.getUserId();
        if (userId != null){
            User user = userService.getById(userId);
            UserVO userVO = userService.getUserVO(user);
            appVO.setUserVO(userVO);
        }
        return appVO;
    }

    @Override
    public QueryWrapper getQueryWrapper(AppQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        return QueryWrapper.create()
                .eq("id", request.getId())
                .like("app_name", request.getAppName())
                .like("cover", request.getCover())
                .like("init_prompt", request.getInitPrompt())
                .eq("code_gen_type", request.getCodeGenType())
                .eq("deploy_key", request.getDeployKey())
                .eq("priority", request.getPriority())
                .eq("userId", request.getUserId())
                .orderBy(request.getSortField(), "ascend".equals(request.getSortOrder()));
    }
    @Override
    public List<AppVO> getAppVOList(List<App> appList) {
        if (CollUtil.isEmpty(appList)) {
            return List.of();
        }
        // 批量获取用户信息，避免 N+1 查询问题
        Set<Long> userIds = appList.stream()
                .map(App::getUserId)
                .collect(Collectors.toSet());
        Map<Long, UserVO> userVOMap = userService.listByIds(userIds).stream()
                .collect(Collectors.toMap(User::getId, userService::getUserVO));
        return appList.stream().map(app -> {
            AppVO appVO = getAppVO(app);
            UserVO userVO = userVOMap.get(app.getUserId());
            appVO.setUserVO(userVO);
            return appVO;
        }).collect(Collectors.toList());
    }


    @Override
    public Page<AppVO> userAppVOByPage(AppQueryRequest appQueryRequest, HttpServletRequest request) {
        // 每页限制最多展示30个应用
        int pageSize = appQueryRequest.getPageSize();
        ThrowUtils.throwIf(pageSize > 30, ErrorCode.PARAMS_ERROR,"每页限制最多展示30个应用");

        // 只展示当前用户的应用
        User loginUser = userService.getLoginUser(request);
        appQueryRequest.setUserId(loginUser.getId());
        QueryWrapper queryWrapper = this.getQueryWrapper(appQueryRequest);
        int pageNum = appQueryRequest.getPageNum();
        Page<App> page = this.page(Page.of(pageNum, pageSize), queryWrapper);

        // App数据封装
        Page<AppVO> appVOPage = new Page<>(pageNum, pageSize, page.getTotalRow());
        List<AppVO> voList = this.getAppVOList(page.getRecords());
        appVOPage.setRecords(voList);
        return appVOPage;
    }

    @Override
    public Boolean adminDeleteApp(DeleteRequest deleteRequest, HttpServletRequest request) {
        Long appId = deleteRequest.getId();
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        return this.removeById(appId);
    }

    @Override
    public Boolean adminUpdateApp(AppAdminUpdateRequest updateRequest) {
        App originApp = this.getById(updateRequest.getId());
        ThrowUtils.throwIf(originApp == null, ErrorCode.NOT_FOUND_ERROR);

        App app = new App();
        BeanUtils.copyProperties(updateRequest, app);
        // 管理员修改应用的时间,和修改记录的时间 updateTime不同
        app.setEditTime(LocalDateTime.now());
        boolean update = this.updateById(app);
        return update;
    }

    @Override
    public Flux<String> gencodeByChat(Long appId, String message, User loginUser) {
        App app = this.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR, "该应用已不存在");

        // 只有应用创建者可通过对话 生成代码
        ThrowUtils.throwIf(!app.getUserId().equals(loginUser.getId()), ErrorCode.NO_AUTH_ERROR,"访问该应用无权限");

        CodeGeneratorTypeEnum codeTypeEnum = CodeGeneratorTypeEnum.getEnumByValue(app.getCodeGenType());
        ThrowUtils.throwIf(codeTypeEnum == null, ErrorCode.SYSTEM_ERROR,"代码生成类型不支持");
        return aiCodeGeneratorFacade.generateAndSaveCodeWithStream(message, codeTypeEnum, appId);
    }


}
