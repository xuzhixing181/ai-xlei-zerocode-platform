package com.xlei.zerocode.model.dto.app;

import lombok.Data;

import java.io.Serializable;
/**
 * @author https://github.com/xuzhixing181
 */
@Data
public class AppAddRequest implements Serializable {

    /**
     * 应用初始化的 prompt
     */
    private String initPrompt;

    private static final long serialVersionUID = 1L;
}
