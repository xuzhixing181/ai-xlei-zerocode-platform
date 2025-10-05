package com.xlei.zerocode.core;

import cn.hutool.core.util.StrUtil;
import com.xlei.zerocode.model.enums.CodeGeneratorTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import reactor.core.publisher.Flux;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AICodeGeneratorFacadeTest {

    @Resource
    private AICodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    public void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("请生成一个后台系统的登录页面,代码要求不超过30行", CodeGeneratorTypeEnum.MULTI_FILE, 1L);
        Assertions.assertNotNull(file);
    }

    @Test
    public void generateAndSaveCodeWithStream() {
        Flux<String> codeStream = aiCodeGeneratorFacade.generateAndSaveCodeWithStream("请生成一个用户管理系统,要求代码尽可能精简", CodeGeneratorTypeEnum.MULTI_FILE, 1L);
        // 阻塞等待所有AI回答收集完成
        List<String> result = codeStream.collectList().block();
        Assertions.assertNotNull(result);
        String resultStr = String.join(",", result);
        Assertions.assertNotNull(resultStr);
    }
}