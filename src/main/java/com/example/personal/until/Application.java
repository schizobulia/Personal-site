package com.example.personal.until;

import org.springframework.util.ResourceUtils;

import java.io.FileNotFoundException;

public class Application {

    /**
     * 获取用户上传的文件路径
     * @return
     * @throws FileNotFoundException
     */
    public static String getParsingFilePath() throws FileNotFoundException {
        return ResourceUtils.getURL("classpath:").getPath() + "static/parsingfile/";
    }


}
