package com.xlei.zerocode.ai;

import dev.langchain4j.model.chat.ChatModel;
import dev.langchain4j.service.AiServices;
import jakarta.annotation.Resource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author https://github.com/xuzhixing181
 */
@Configuration
public class AICodeServiceFactory {

    @Resource
    private ChatModel chatModel;

    @Bean
    public AICodeService aiCodeService(){
        return AiServices.create(AICodeService.class,chatModel);
    }
}