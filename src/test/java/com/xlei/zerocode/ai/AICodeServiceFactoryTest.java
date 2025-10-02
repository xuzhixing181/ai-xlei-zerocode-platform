package com.xlei.zerocode.ai;

import com.xlei.zerocode.ai.model.HtmlCodeResult;
import com.xlei.zerocode.ai.model.MultiFileCodeResult;
import jakarta.annotation.Resource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class AICodeServiceFactoryTest {

    @Resource
    private AICodeService aiCodeService;

    @Test
    public void generateHtmlCode() {
        HtmlCodeResult result = aiCodeService.generateHtmlCode("请生成一个系统的登录页面,要求代码不超过30行");
        Assertions.assertNotNull(result);
    }

    @Test
    public void generateMultiFileCode() {
        MultiFileCodeResult result = aiCodeService.generateMultiFileCode("请生成xlei的个人博客网站");
        Assertions.assertNotNull(result);
    }
}