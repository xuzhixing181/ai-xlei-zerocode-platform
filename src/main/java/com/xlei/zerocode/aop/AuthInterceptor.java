package com.xlei.zerocode.aop;

import com.xlei.zerocode.annotation.AuthCheck;
import com.xlei.zerocode.exception.BusinessException;
import com.xlei.zerocode.exception.ErrorCode;
import com.xlei.zerocode.model.entity.User;
import com.xlei.zerocode.model.enums.UserRoleEnum;
import com.xlei.zerocode.service.UserService;
import jakarta.annotation.Resource;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.RequestConditionHolder;

/**
 * @author https://github.com/xuzhixing181
 */
@Aspect
@Component
public class AuthInterceptor {

    @Resource
    private UserService userService;

    @Around("@annotation(authCheck)")
    public Object doInterceptor(ProceedingJoinPoint joinPoint, AuthCheck authCheck) throws Throwable {
        String mustRule = authCheck.mustRule();
        UserRoleEnum mustRoleEnum = UserRoleEnum.getEnumByValue(mustRule);
        // 不需要用户权限,直接放行
        if (mustRoleEnum == null){
            return joinPoint.proceed();
        }
        RequestAttributes requestAttributes = RequestContextHolder.getRequestAttributes();
        HttpServletRequest request = ((ServletRequestAttributes) requestAttributes).getRequest();

        // 如下,必须要有权限才能放行
        User loginUser = userService.getLoginUser(request);
        UserRoleEnum userRoleEnum = UserRoleEnum.getEnumByValue(loginUser.getUserRole());
        if (userRoleEnum == null){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        // 必须要有管理员权限,否则拒绝访问
        if (UserRoleEnum.ADMIN.equals(mustRoleEnum) && !UserRoleEnum.ADMIN.equals(userRoleEnum)){
            throw new BusinessException(ErrorCode.NO_AUTH_ERROR);
        }
        return joinPoint.proceed();
    }
}