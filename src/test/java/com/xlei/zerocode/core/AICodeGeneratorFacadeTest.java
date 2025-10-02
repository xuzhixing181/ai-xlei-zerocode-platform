package com.xlei.zerocode.core;

import com.xlei.zerocode.model.enums.CodeGeneratorTypeEnum;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AICodeGeneratorFacadeTest {

    @Resource
    private AICodeGeneratorFacade aiCodeGeneratorFacade;

    @Test
    public void generateAndSaveCode() {
        File file = aiCodeGeneratorFacade.generateAndSaveCode("请生成一个后台系统的登录页面,代码要求不超过30行", CodeGeneratorTypeEnum.MULTI_FILE);
        Assertions.assertNotNull(file);
    }
}