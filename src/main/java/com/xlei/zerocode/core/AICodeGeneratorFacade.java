package com.xlei.zerocode.core;

import com.xlei.zerocode.ai.AICodeService;
import com.xlei.zerocode.ai.model.HtmlCodeResult;
import com.xlei.zerocode.ai.model.MultiFileCodeResult;
import com.xlei.zerocode.exception.BusinessException;
import com.xlei.zerocode.exception.ErrorCode;
import com.xlei.zerocode.model.enums.CodeGeneratorTypeEnum;
import jakarta.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;

import java.io.File;

/**
 * AI 代码生成器的门面类
 * @author https://github.com/xuzhixing181
 */
@Service
@Slf4j
public class AICodeGeneratorFacade {

    @Resource
    private AICodeService aiCodeService;

    @Resource
    private AICodeService aiCodeStreamService;

    /**
     * 根据类型生成代码并保存
     * @param userMessage
     * @param codeTypeEnum
     * @return
     */
    public File generateAndSaveCode(String userMessage, CodeGeneratorTypeEnum codeTypeEnum){
        if (codeTypeEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"生成类型不能为空");
        }
        switch (codeTypeEnum){
            // 生成 HTML 代码 并 保存
            case HTML:
                HtmlCodeResult htmlCodeResult = aiCodeService.generateHtmlCode(userMessage);
                return CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
            // 生成多文件代码 并 保存
            case MULTI_FILE:
                MultiFileCodeResult multiFileCodeResult = aiCodeService.generateMultiFileCode(userMessage);
                return CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
            default:
                String errMessage = "不支持的文件生成类型: " + codeTypeEnum.getValue();
                throw new BusinessException(ErrorCode.PARAMS_ERROR,errMessage);
        }
    }

    /**
     * 根据类型 流式 生成代码并保存
     * @param userMessage
     * @param codeTypeEnum
     * @return
     */
    public Flux<String> generateAndSaveCodeWithStream(String userMessage, CodeGeneratorTypeEnum codeTypeEnum){
        if (codeTypeEnum == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"生成类型不能为空");
        }
        return switch (codeTypeEnum){
            // 生成 HTML 代码 并 保存
            case HTML -> generateAndSaveHtmlCodeWithStream(userMessage);
            // 生成多文件代码 并 保存
            case MULTI_FILE -> generateAndSaveMultiFileCodeWithStream(userMessage);
            default -> {
                String errMessage = "不支持的文件生成类型: " + codeTypeEnum.getValue();
                throw new BusinessException(ErrorCode.PARAMS_ERROR,errMessage);
            }
        };
    }

    /**
     * 多文件模式下 流式生成并保存代码文件
     * @param userMessage
     * @return
     */
    private Flux<String> generateAndSaveHtmlCodeWithStream(String userMessage) {
        Flux<String> result = aiCodeStreamService.generateHtmlCodeStream(userMessage);
        StringBuilder builder = new StringBuilder();
        return result.doOnNext(chunk -> {
            // 实时收集代码片段
            builder.append(chunk);
        }).doOnComplete(()->{
            try {
                // 流式返回完成后保存代码
                HtmlCodeResult htmlCodeResult = CodeParser.parseHtmlCode(builder.toString());
                File file = CodeFileSaver.saveHtmlCodeResult(htmlCodeResult);
                log.info("单网页模式, 文件保存成功,路径为: {}", file.getAbsolutePath());
            } catch (Exception e) {
                log.error("单网页模式, 文件保存失败: {}", e.getMessage());
            }
        });
    }

    /**
     * 多文件模式下 流式 生成并保存代码文件
     * @param userMessage
     * @return
     */
    private Flux<String> generateAndSaveMultiFileCodeWithStream(String userMessage) {
        Flux<String> result = aiCodeStreamService.generateMultiFileCodeStream(userMessage);
        StringBuilder builder = new StringBuilder();
        return result.doOnNext(chunk -> {
            // 实时收集代码片段
            builder.append(chunk);
        }).doOnComplete(()->{
            try {
                // 流式返回完成后保存代码
                MultiFileCodeResult multiFileCodeResult = CodeParser.parseMultiFileCode(builder.toString());
                File file = CodeFileSaver.saveMultiFileCodeResult(multiFileCodeResult);
                log.info("多文件模式, 文件保存成功,路径为: {}", file.getAbsolutePath());
            } catch (Exception e) {
                log.error("多文件模式, 文件保存失败: {}", e.getMessage());
            }
        });
    }
}