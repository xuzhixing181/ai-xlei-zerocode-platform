package com.xlei.zerocode.model.dto;

import lombok.Data;

import java.io.Serializable;

/**
 * @author https://github.com/xuzhixing181
 */
@Data
public class UserLoginRequest implements Serializable {

    private static final long serialVersionUID = 3191241716373120793L;

    /**
     * 账号
     */
    private String userAccount;

    /**
     * 密码
     */
    private String userPassword;
}
