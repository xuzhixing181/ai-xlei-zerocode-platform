package com.xlei.zerocode.common;

import com.xlei.zerocode.exception.ErrorCode;
import lombok.Data;

import java.io.Serializable;

/**
 * @author https://github.com/xuzhixing181
 */
@Data
public class BaseResponse<T> implements Serializable {

    private int code;

    private T data;

    private String message;

    public BaseResponse(int code, T data, String message) {
        this.code = code;
        this.data = data;
        this.message = message;
    }

    public BaseResponse(int code, T data) {
        this(code, data, "");
    }

    public BaseResponse(ErrorCode errorCode) {
        this(errorCode.getCode(), null, errorCode.getMessage());
    }
}
