package com.xlei.zerocode.common;

import lombok.Data;

import java.io.Serializable;

/**
 * @author https://github.com/xuzhixing181
 */
@Data
public class DeleteRequest implements Serializable {

    /**
     * id
     */
    private Long id;

    private static final long serialVersionUID = 1L;
}
