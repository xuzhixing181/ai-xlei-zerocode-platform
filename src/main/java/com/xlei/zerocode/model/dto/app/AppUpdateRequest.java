package com.xlei.zerocode.model.dto.app;

import lombok.Data;
import java.io.Serializable;
/**
 * @author https://github.com/xuzhixing181
 */
@Data
public class AppUpdateRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    /**
     * 应用名称
     */
    private String appName;

    private static final long serialVersionUID = 1L;
}
