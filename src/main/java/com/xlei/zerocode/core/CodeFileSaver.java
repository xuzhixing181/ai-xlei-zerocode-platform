package com.xlei.zerocode.core;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import com.xlei.zerocode.ai.model.HtmlCodeResult;
import com.xlei.zerocode.ai.model.MultiFileCodeResult;
import com.xlei.zerocode.model.enums.CodeGeneratorTypeEnum;

import java.io.File;
import java.nio.charset.StandardCharsets;

/**
 * @author https://github.com/xuzhixing181
 */
public class CodeFileSaver {

    private static final String FILE_SAVER_ROOT_DIR = System.getProperty("user.dir") + "/tmp/code_output";

    /**
     * 保存 HTML 网页文件
     * @param htmlCodeResult
     * @return
     */
    public static File saveHtmlCodeResult(HtmlCodeResult htmlCodeResult,Long appId){
        String baseDirPath = buildUniqueDir(CodeGeneratorTypeEnum.HTML.getValue(),appId);
        saveFile(baseDirPath,"index.html", htmlCodeResult.getHtmlCode());
        return new File(baseDirPath);
    }

    /**
     * 保存多文件网页代码
     * @param multiFileCodeResult
     * @return
     */
    public static File saveMultiFileCodeResult(MultiFileCodeResult multiFileCodeResult,Long appId){
        String baseDirPath = buildUniqueDir(CodeGeneratorTypeEnum.MULTI_FILE.getValue(),appId);
        saveFile(baseDirPath,"index.html", multiFileCodeResult.getHtmlCode());
        saveFile(baseDirPath,"style.css", multiFileCodeResult.getCssCode());
        saveFile(baseDirPath,"script.js", multiFileCodeResult.getJsCode());
        return new File(baseDirPath);
    }
    /**
     * 构建文件的唯一路径: tmp/code_output/bizType_appId
     * @param bizType
     * @return
     */
    private static String buildUniqueDir(String bizType, Long appId){
        String uniqueDirName = StrUtil.format("{}_{}",bizType, appId);
        String dirPath = FILE_SAVER_ROOT_DIR + File.separator + uniqueDirName;
        FileUtil.mkdir(dirPath);
        return dirPath;
    }

    /**
     * 保存单个代码文件
     * @param dirPath
     * @param fileName
     * @param content
     */
    private static void saveFile(String dirPath, String fileName, String content){
        String filePath = dirPath + File.separator + fileName;
        FileUtil.writeString(content,filePath, StandardCharsets.UTF_8);
    }
}