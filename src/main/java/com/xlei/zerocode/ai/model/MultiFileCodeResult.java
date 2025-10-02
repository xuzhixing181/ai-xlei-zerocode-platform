package com.xlei.zerocode.ai.model;

import lombok.Data;
/**
 * @author https://github.com/xuzhixing181
 */
@Data
public class MultiFileCodeResult {

    /**
     * Html
     */
    private String htmlCode;

    private String cssCode;

    private String jsCode;

    private String description;
}
