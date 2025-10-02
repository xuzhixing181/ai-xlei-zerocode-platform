package com.xlei.zerocode.core;

import com.xlei.zerocode.ai.AICodeService;
import com.xlei.zerocode.ai.model.HtmlCodeResult;
import com.xlei.zerocode.ai.model.MultiFileCodeResult;
import com.xlei.zerocode.exception.BusinessException;
import com.xlei.zerocode.exception.ErrorCode;
import com.xlei.zerocode.model.enums.CodeGeneratorTypeEnum;
import jakarta.annotation.Resource;
import org.springframework.stereotype.Service;

import java.io.File;

/**
 * AI 代码生成器的门面类
 * @author https://github.com/xuzhixing181
 */
@Service
public class AICodeGeneratorFacade {

    @Resource
    private AICodeService aiCodeService;

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
}