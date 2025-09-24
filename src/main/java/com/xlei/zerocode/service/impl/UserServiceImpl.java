package com.xlei.zerocode.service.impl;

import com.mybatisflex.spring.service.impl.ServiceImpl;
import com.xlei.zerocode.model.entity.User;
import com.xlei.zerocode.mapper.UserMapper;
import com.xlei.zerocode.service.UserService;
import org.springframework.stereotype.Service;

/**
 * 用户 服务层实现。
 *
 * @author xlei
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User>  implements UserService{

}
