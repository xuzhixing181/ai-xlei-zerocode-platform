package com.xlei.zerocode.ai;

import com.xlei.zerocode.ai.model.HtmlCodeResult;
import com.xlei.zerocode.ai.model.MultiFileCodeResult;
import dev.langchain4j.service.SystemMessage;

/**
 * @author https://github.com/xuzhixing181
 */
public interface AICodeService {

    /**
     * 生成 HTML 代码
     *
     * @param userMessage 用户消息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/html-system-prompt.txt")
    HtmlCodeResult generateHtmlCode(String userMessage);

    /**
     * 生成多文件代码
     *
     * @param userMessage 用户消息
     * @return 生成的代码结果
     */
    @SystemMessage(fromResource = "prompt/multi-file-system-prompt.txt")
    MultiFileCodeResult generateMultiFileCode(String userMessage);
}
