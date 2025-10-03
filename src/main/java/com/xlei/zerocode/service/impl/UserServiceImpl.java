package com.xlei.zerocode.service.impl;

import cn.hutool.core.util.StrUtil;
import com.mybatisflex.core.query.QueryWrapper;
import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xlei.zerocode.exception.BusinessException;
import com.xlei.zerocode.exception.ErrorCode;
import com.xlei.zerocode.exception.ThrowUtils;
import com.xlei.zerocode.model.dto.user.UserAddRequest;
import com.xlei.zerocode.model.dto.user.UserQueryRequest;
import com.xlei.zerocode.model.entity.User;
import com.xlei.zerocode.mapper.UserMapper;
import com.xlei.zerocode.model.enums.UserRoleEnum;
import com.xlei.zerocode.model.vo.LoginUserVO;
import com.xlei.zerocode.model.vo.UserVO;
import com.xlei.zerocode.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.util.DigestUtils;
import org.springframework.util.ObjectUtils;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

import static com.xlei.zerocode.constants.UserConstants.DEFAULT_PASSWORD;
import static com.xlei.zerocode.constants.UserConstants.USER_LOGIN_STATE;

/**
 * 用户 服务层实现。
 *
 * @author xlei
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService{

    @Override
    public Long register(String userAccount, String userPassword, String checkPassword) {
        if (StrUtil.hasBlank(userAccount,userPassword,checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号长度不能小于4位");
        }
        if (userPassword.length() < 8 || checkPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码不能小于8位");
        }
        if (!userPassword.equals(checkPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"两次输入的密码不相等!");
        }
        QueryWrapper wrapper = new QueryWrapper().eq("user_account", userAccount);
        long count = this.mapper.selectCountByQuery(wrapper);
        if (count > 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号名已存在");
        }
        String encryptPassword = getEncryptPassword(userPassword);
        User user = User.builder().userAccount(userAccount).userPassword(encryptPassword)
                .userName("用户" + getRandomId()).userRole(UserRoleEnum.USER.getValue())
                .build();
        boolean save = this.save(user);
        if (!save){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"注册失败");
        }
        return user.getId();
    }

    @Override
    public String getEncryptPassword(String password){
        String SALT = "xlei-zerocode";
        return DigestUtils.md5DigestAsHex((password + SALT).getBytes());
    }

    public String getRandomId(){
        return ThreadLocalRandom.current().nextInt(1000) + "" + ThreadLocalRandom.current().nextInt(1000);
    }

    @Override
    public LoginUserVO login(String userAccount, String userPassword, HttpServletRequest request) {
        // 请求参数校验
        if (StrUtil.hasBlank(userAccount,userPassword)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"请求参数为空");
        }
        if (userAccount.length() < 4){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"账号错误");
        }
        if (userPassword.length() < 8){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码错误");
        }
        // 对传入的密码加密,并查询数据库做比对
        String encryptPassword = getEncryptPassword(userPassword);
        QueryWrapper wrapper = new QueryWrapper()
                .eq("user_account", userAccount)
                .eq("user_password", encryptPassword);
        User user = this.mapper.selectOneByQuery(wrapper);
        if (ObjectUtils.isEmpty(user)){
            throw new BusinessException(ErrorCode.NOT_FOUND_ERROR,"用户不存在或密码错误!");
        }
        request.getSession().setAttribute(USER_LOGIN_STATE, user);
        // 返回脱敏后的用户信息
        LoginUserVO loginUserVO = new LoginUserVO();
        BeanUtils.copyProperties(user,loginUserVO);
        return loginUserVO;
    }

    @Override
    public Boolean logout(HttpServletRequest request) {
        User user = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (user == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR,"用户未登录");
        }
        request.getSession().removeAttribute(USER_LOGIN_STATE);
        return null;
    }

    @Override
    public User getLoginUser(HttpServletRequest request) {
        User curUser = (User) request.getSession().getAttribute(USER_LOGIN_STATE);
        if (curUser == null || curUser.getId() == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN_ERROR);
        }
        return curUser;
    }

    @Override
    public Long addUser(UserAddRequest userAddRequest) {
        String encryptPassword = getEncryptPassword(DEFAULT_PASSWORD);
        User user = new User();
        BeanUtils.copyProperties(userAddRequest, user);
        user.setUserPassword(encryptPassword);
        boolean save = this.save(user);
        ThrowUtils.throwIf(!save,ErrorCode.SYSTEM_ERROR);
        return user.getId();
    }

    @Override
    public List<UserVO> getUserVOList(List<User> userList) {
        if (CollectionUtils.isEmpty(userList)){
            return List.of();
        }
        return userList.stream().map(this::getUserVO).collect(Collectors.toList());
    }

    public UserVO getUserVO(User user) {
        if (user == null) return null;

        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        return userVO;
    }
    @Override
    public QueryWrapper getQueryWrapper(UserQueryRequest request) {
        if (request == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR, "请求参数为空");
        }
        return QueryWrapper.create()
                .eq("id", request.getId())
                .eq("user_role", request.getUserRole())
                .like("user_account", request.getUserAccount())
                .like("user_name", request.getUserName())
                .like("user_profile", request.getUserProfile())
                .orderBy(request.getSortField(), "ascend".equals(request.getSortOrder()));
    }


}
