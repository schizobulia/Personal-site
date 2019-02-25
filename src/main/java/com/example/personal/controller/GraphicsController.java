package com.example.personal.controller;

import com.example.personal.server.SparkSqlServer;
import com.example.personal.until.Application;
import com.example.personal.until.Encrypt;
import com.example.personal.until.FileTool;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.util.List;

@RestController
public class GraphicsController {

    @RequestMapping(value = "getstructure", method = RequestMethod.GET)
    public String getStructure(@RequestParam String key, @RequestParam String sql){
        JSONObject result = new JSONObject();
        int status = 0;
        if (sql.equals("") || key.equals("")){
            result.put("status", status);
            return result.toString();
        }
        try {
            String path = Application.getParsingFilePath() + "out/" + key;
            if (new File(path).exists()){
                List<String> fileByPath =  FileTool.getFileByKeyAndSql(key, Encrypt.decrypt_Base64(sql));
                JSONObject type = new SparkSqlServer().getStructureByFiles(
                        Application.getParsingFilePath() + "/out/"
                                + key + "/" + sql + "/" + fileByPath.get(0));
                result.put("type", type);
                status = 1;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            result.put("status", status);
        }
        return result.toString();
    }
}
