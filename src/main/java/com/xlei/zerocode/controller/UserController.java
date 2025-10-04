package com.xlei.zerocode.controller;

import com.mybatisflex.core.paginate.Page;
import com.xlei.zerocode.annotation.AuthCheck;
import com.xlei.zerocode.common.BaseResponse;
import com.xlei.zerocode.common.DeleteRequest;
import com.xlei.zerocode.common.ResultUtils;
import com.xlei.zerocode.constants.UserConstants;
import com.xlei.zerocode.exception.ErrorCode;
import com.xlei.zerocode.exception.ThrowUtils;
import com.xlei.zerocode.model.dto.user.UserLoginRequest;
import com.xlei.zerocode.model.dto.user.UserRegisterRequest;
import com.xlei.zerocode.model.dto.user.UserAddRequest;
import com.xlei.zerocode.model.dto.user.UserQueryRequest;
import com.xlei.zerocode.model.dto.user.UserUpdateRequest;
import com.xlei.zerocode.model.vo.LoginUserVO;
import com.xlei.zerocode.model.vo.UserVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xlei.zerocode.model.entity.User;
import com.xlei.zerocode.service.UserService;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

/**
 * 用户 控制层。
 *
 * @author xlei
 */
@RestController
@RequestMapping("/user")
public class UserController {

    @Resource
    private UserService userService;

    /**
     * 用户注册
     *
     * @param request 用户注册请求
     * @return 注册结果(含用户id)
     */
    @PostMapping("/register")
    public BaseResponse<Long> register(@RequestBody UserRegisterRequest request){
        ThrowUtils.throwIf(ObjectUtils.isEmpty(request), ErrorCode.PARAMS_ERROR);
        Long userId = userService.register(request.getUserAccount(),request.getUserPassword(),request.getCheckPassword());
        return ResultUtils.success(userId);
    }

    /**
     * 用户登录
     *
     * @param request 用户登录请求
     * @return 注册结果(含用户id)
     */
    @PostMapping("/login")
    public BaseResponse<LoginUserVO> login(@RequestBody UserLoginRequest request, HttpServletRequest req){
        ThrowUtils.throwIf(ObjectUtils.isEmpty(request), ErrorCode.PARAMS_ERROR);
        LoginUserVO loginUserVO = userService.login(request.getUserAccount(),request.getUserPassword(),req);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 用户退出登录
     *
     * @param request 退出登录请求
     * @return
     */
    @PostMapping("/logout")
    public BaseResponse<Boolean> logout(HttpServletRequest request){
        ThrowUtils.throwIf(ObjectUtils.isEmpty(request), ErrorCode.PARAMS_ERROR);
        Boolean result = userService.logout(request);
        return ResultUtils.success(result);
    }

    /**
     * 获取当前登录的用户信息
     *
     * @param request
     * @return
     */
    @GetMapping("/getLoginUser")
    public BaseResponse<LoginUserVO> getLoginUser(HttpServletRequest request){
        User loginUser = userService.getLoginUser(request);
        if (loginUser == null){
            return ResultUtils.success(null);
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(loginUser,loginUserVO);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 管理员查看用户详情
     * @param id
     * @return
     */
    @GetMapping("/getUser")
    @AuthCheck(mustRule = UserConstants.ADMIN_ROLE)
    public BaseResponse<User> getUserById(Long id){
        ThrowUtils.throwIf(id <= 0, ErrorCode.PARAMS_ERROR);
        User user = userService.getById(id);
        ThrowUtils.throwIf(user == null, ErrorCode.NOT_FOUND_ERROR);
        return ResultUtils.success(user);
    }

    /**
     * 查看用户详情
     * @param id
     * @return
     */
    @GetMapping("/getUserVO")
    public BaseResponse<UserVO> getUserVOById(Long id){
        User user = getUserById(id).getData();
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user,userVO);
        return ResultUtils.success(userVO);
    }



    /**
     * 管理员创建用户
     * @param request
     * @return
     */
    @PostMapping("/add")
    @AuthCheck(mustRule = UserConstants.ADMIN_ROLE)
    public BaseResponse<Long> addUser(@RequestBody UserAddRequest request){
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        Long userId = userService.addUser(request);
        return ResultUtils.success(userId);
    }

    /**
     * 管理员删除用户
     * @param request
     * @return
     */
    @DeleteMapping("/delete")
    @AuthCheck(mustRule = UserConstants.ADMIN_ROLE)
    public BaseResponse<Boolean> deleteById(@RequestBody DeleteRequest request){
        ThrowUtils.throwIf(request == null || request.getId() <= 0, ErrorCode.PARAMS_ERROR);
        boolean remove = userService.removeById(request.getId());
        ThrowUtils.throwIf(!remove, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(remove);
    }

    /**
     * 管理员更新用户
     * @param request
     * @return
     */
    @PostMapping("/update")
    @AuthCheck(mustRule = UserConstants.ADMIN_ROLE)
    public BaseResponse<Boolean> updateUser(@RequestBody UserUpdateRequest request){
        ThrowUtils.throwIf(request == null || request.getId() <= 0, ErrorCode.PARAMS_ERROR);
        User user = new User();
        BeanUtils.copyProperties(request,user);
        boolean update = userService.updateById(user);
        ThrowUtils.throwIf(!update, ErrorCode.OPERATION_ERROR);
        return ResultUtils.success(update);
    }

    /**
     * 管理员 分页查询用户的脱敏信息
     * @param request
     * @return
     */
    @PostMapping("/list/pageVO")
    @AuthCheck(mustRule = UserConstants.ADMIN_ROLE)
    public BaseResponse<Page<UserVO>> listUserVOByPage(@RequestBody UserQueryRequest request){
        ThrowUtils.throwIf(request == null, ErrorCode.PARAMS_ERROR);
        int pageNum = request.getPageNum();
        int pageSize = request.getPageSize();
        Page<User> page = userService.page(Page.of(pageNum, pageSize), userService.getQueryWrapper(request));

        // 对查询到的用户信息脱敏处理
        Page<UserVO> userVoPage = new Page<>(pageNum, pageSize, page.getTotalRow());
        List<UserVO> userVOList = userService.getUserVOList(page.getRecords());
        userVoPage.setRecords(userVOList);
        return ResultUtils.success(userVoPage);
    }

}
