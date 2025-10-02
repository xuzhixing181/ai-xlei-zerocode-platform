package com.xlei.zerocode.ai.model;

import dev.langchain4j.model.output.structured.Description;
import lombok.Data;

/**
 * @author https://github.com/xuzhixing181
 */
@Description("生成Html代码文件的描述")
@Data
public class HtmlCodeResult {

    @Description("生成HTML代码")
    private String htmlCode;

    @Description("生成代码的描述")
    private String description;
}
