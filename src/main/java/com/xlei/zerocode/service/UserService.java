package com.xlei.zerocode.service;

import com.mybatisflex.core.service.IService;
import com.xlei.zerocode.model.entity.User;
import com.xlei.zerocode.model.vo.LoginUserVO;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 用户 服务层
 *
 * @author xlei
 */
public interface UserService extends IService<User> {

    /**
     * 用户注册
     * @param userAccount
     * @param userPassword
     * @param checkPassword
     * @return
     */
    Long register(String userAccount, String userPassword, String checkPassword);

    /**
     * 用户登录
     * @param userAccount
     * @param userPassword
     * @param request
     * @return
     */
    LoginUserVO login(String userAccount, String userPassword, HttpServletRequest request);

    /**
     * 用户退出登录
     * @param request
     * @return
     */
    Boolean logout(HttpServletRequest request);

    /**
     * 获取当前登录用户
     * @param request
     * @return
     */
    User getLoginUser(HttpServletRequest request);

}
