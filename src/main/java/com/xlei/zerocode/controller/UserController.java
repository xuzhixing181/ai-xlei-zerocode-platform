package com.xlei.zerocode.controller;

import com.mybatisflex.core.paginate.Page;
import com.xlei.zerocode.common.BaseResponse;
import com.xlei.zerocode.common.ResultUtils;
import com.xlei.zerocode.exception.BusinessException;
import com.xlei.zerocode.exception.ErrorCode;
import com.xlei.zerocode.exception.ThrowUtils;
import com.xlei.zerocode.model.dto.UserLoginRequest;
import com.xlei.zerocode.model.dto.UserRegisterRequest;
import com.xlei.zerocode.model.vo.LoginUserVO;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import com.xlei.zerocode.model.entity.User;
import com.xlei.zerocode.service.UserService;
import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import static com.xlei.zerocode.constants.UserConstants.USER_LOGIN_STATE;

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
        User curUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (curUser == null || curUser.getId() == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(curUser,loginUserVO);
        return ResultUtils.success(loginUserVO);
    }

    /**
     * 保存用户。
     *
     * @param user 用户
     * @return {@code true} 保存成功，{@code false} 保存失败
     */
    @PostMapping("save")
    public boolean save(@RequestBody User user) {
        return userService.save(user);
    }

    /**
     * 根据主键删除用户。
     *
     * @param id 主键
     * @return {@code true} 删除成功，{@code false} 删除失败
     */
    @DeleteMapping("remove/{id}")
    public boolean remove(@PathVariable Long id) {
        return userService.removeById(id);
    }

    /**
     * 根据主键更新用户。
     *
     * @param user 用户
     * @return {@code true} 更新成功，{@code false} 更新失败
     */
    @PutMapping("update")
    public boolean update(@RequestBody User user) {
        return userService.updateById(user);
    }

    /**
     * 查询所有用户。
     *
     * @return 所有数据
     */
    @GetMapping("list")
    public List<User> list() {
        return userService.list();
    }

    /**
     * 根据主键获取用户。
     *
     * @param id 用户主键
     * @return 用户详情
     */
    @GetMapping("getInfo/{id}")
    public User getInfo(@PathVariable Long id) {
        return userService.getById(id);
    }

    /**
     * 分页查询用户。
     *
     * @param page 分页对象
     * @return 分页对象
     */
    @GetMapping("page")
    public Page<User> page(Page<User> page) {
        return userService.page(page);
    }

}
