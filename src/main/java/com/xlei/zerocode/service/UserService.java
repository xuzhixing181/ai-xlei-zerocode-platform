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

    Long register(String userAccount, String userPassword, String checkPassword);

    LoginUserVO login(String userAccount, String userPassword, HttpServletRequest request);

    Boolean logout(HttpServletRequest request);
}
