package com.xlei.zerocode.controller;

import com.xlei.zerocode.common.BaseResponse;
import com.xlei.zerocode.common.ResultUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author https://github.com/xuzhixing181
 */
@RestController
@RequestMapping("/health")
public class HealthController {

    @GetMapping("/check")
    public BaseResponse<String> healthCheck() {
        return ResultUtils.success("OK");
    }
}
