package com.xlei.zerocode.controller;

import com.mybatisflex.core.paginate.Page;
import com.xlei.zerocode.annotation.AuthCheck;
import com.xlei.zerocode.common.BaseResponse;
import com.xlei.zerocode.common.DeleteRequest;
import com.xlei.zerocode.common.ResultUtils;
import com.xlei.zerocode.constants.UserConstants;
import com.xlei.zerocode.exception.ErrorCode;
import com.xlei.zerocode.exception.ThrowUtils;
import com.xlei.zerocode.model.dto.app.AppAddRequest;
import com.xlei.zerocode.model.dto.app.AppAdminUpdateRequest;
import com.xlei.zerocode.model.dto.app.AppQueryRequest;
import com.xlei.zerocode.model.dto.app.AppUpdateRequest;
import com.xlei.zerocode.model.dto.user.UserQueryRequest;
import com.xlei.zerocode.model.entity.App;
import com.xlei.zerocode.model.entity.User;
import com.xlei.zerocode.model.vo.AppVO;
import com.xlei.zerocode.model.vo.UserVO;
import com.xlei.zerocode.service.AppService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author https://github.com/xuzhixing181
 */
@RestController
@RequestMapping("/app")
public class AppController {

    @Resource
    private AppService appService;

    @PostMapping("/add")
    public BaseResponse<Long> addApp(@RequestBody AppAddRequest appAddRequest, HttpServletRequest request){
        ThrowUtils.throwIf(appAddRequest == null, ErrorCode.PARAMS_ERROR);
        Long appId = appService.addApp(appAddRequest,request);
        return ResultUtils.success(appId);
    }

    @PostMapping("/update")
    public BaseResponse<Boolean> updateApp(@RequestBody AppUpdateRequest updateRequest, HttpServletRequest request){
        ThrowUtils.throwIf(updateRequest == null || updateRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        Boolean updated = appService.updateApp(updateRequest,request);
        return ResultUtils.success(updated);
    }

    @DeleteMapping("/delete")
    public BaseResponse<Boolean> deleteApp(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() == null, ErrorCode.PARAMS_ERROR);
        Boolean deleted = appService.deleteApp(deleteRequest,request);
        return ResultUtils.success(deleted);
    }

    @GetMapping("/getvo")
    public BaseResponse<AppVO> getAppVOById(Long appId){
        ThrowUtils.throwIf(appId == null || appId <= 0, ErrorCode.PARAMS_ERROR);
        App app = appService.getById(appId);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(appService.getAppVO(app));
    }

    /**
     * 分页查询用户创建的应用列表
     * @param appQueryRequest
     * @param request
     * @return
     */
    @PostMapping("/appPage/vo")
    public BaseResponse<Page<AppVO>> userAppVOByPage(@RequestBody AppQueryRequest appQueryRequest, HttpServletRequest request){
        ThrowUtils.throwIf(appQueryRequest == null, ErrorCode.PARAMS_ERROR);
        Page<AppVO> appVOPage = appService.userAppVOByPage(appQueryRequest,request);
        return ResultUtils.success(appVOPage);
    }

    @DeleteMapping("/admin/delete")
    @AuthCheck(mustRule = UserConstants.ADMIN_ROLE)
    public BaseResponse<Boolean> adminDeleteApp(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){
        ThrowUtils.throwIf(deleteRequest == null || deleteRequest.getId() <= 0, ErrorCode.NOT_LOGIN_ERROR);
        Boolean delete = appService.adminDeleteApp(deleteRequest, request);
        return ResultUtils.success(delete);
    }

    /**
     * 管理员更新应用
     * @param updateRequest
     * @param request
     * @return
     */
    @PostMapping("/admin/update")
    @AuthCheck(mustRule = UserConstants.ADMIN_ROLE)
    public BaseResponse<Boolean> adminUpdateApp(@RequestBody AppAdminUpdateRequest updateRequest, HttpServletRequest request){
        ThrowUtils.throwIf(updateRequest == null || updateRequest.getId() <= 0, ErrorCode.NOT_LOGIN_ERROR);
        Boolean update = appService.adminUpdateApp(updateRequest);
        return ResultUtils.success(update);
    }

    /**
     * 管理员分页查询应用
     * @param request
     * @return
     */
    @PostMapping("/admin/list/pageVO")
    @AuthCheck(mustRule = UserConstants.ADMIN_ROLE)
    public BaseResponse<Page<AppVO>> adminAppVOByPage(@RequestBody AppQueryRequest request){
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        Page<App> page = appService.page(Page.of(pageNum, pageSize), appService.getQueryWrapper(request));

        // 对查询到的应用信息脱敏处理
        Page<AppVO> appVoPage = new Page<>(pageNum, pageSize, page.getTotalRow());
        List<AppVO> voList = appService.getAppVOList(page.getRecords());
        appVoPage.setRecords(voList);
        return ResultUtils.success(appVoPage);
    }

    /**
     * 管理员查看应用的详情信息
     * @param id: 应用id
     * @return
     */
    @PostMapping("/admin/detail")
    @AuthCheck(mustRule = UserConstants.ADMIN_ROLE)
    public BaseResponse<AppVO> adminAppDetail(long id){
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        App app = appService.getById(id);
        ThrowUtils.throwIf(app == null, ErrorCode.NOT_FOUND_ERROR);
        AppVO appVO = appService.getAppVO(app);
        return ResultUtils.success(appVO);
    }




    /**
     * 保存应用。
     *
     * @param app 应用
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody App app) {
        return appService.save(app);
    }

    /**
     * 根据主键删除应用。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return appService.removeById(id);
    }

    /**
     * 根据主键更新应用。
     *
     * @param app 应用
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody App app) {
        return appService.updateById(app);
    }

    /**
     * 查询所有应用。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<App> list() {
        return appService.list();
    }

    /**
     * 根据主键获取应用。
     *
     * @param id 应用主键
     * @return 应用详情
     */
    @GetMapping("getInfo/{id}")
    public App getInfo(@PathVariable Long id) {
        return appService.getById(id);
    }

    /**
     * 分页查询应用。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<App> page(Page<App> page) {
        return appService.page(page);
    }

}