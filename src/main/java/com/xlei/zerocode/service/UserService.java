package com.xlei.zerocode.service;

import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.core.service.IService;
import com.xlei.zerocode.model.dto.user.UserAddRequest;
import com.xlei.zerocode.model.dto.user.UserQueryRequest;
import com.xlei.zerocode.model.entity.User;
import com.xlei.zerocode.model.vo.LoginUserVO;
import com.xlei.zerocode.model.vo.UserVO;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

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

    String getEncryptPassword(String password);

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

    /**
     * 创建用户
     * @param request
     * @return
     */
    Long addUser(UserAddRequest request);

    List<UserVO> getUserVOList(List<User> userList);

    QueryWrapper getQueryWrapper(UserQueryRequest request);

    UserVO getUserVO(User user);

}
