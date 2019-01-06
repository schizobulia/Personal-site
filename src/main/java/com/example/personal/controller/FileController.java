package com.example.personal.controller;

import com.example.personal.server.SparkSqlServer;
import com.example.personal.until.Application;
import com.example.personal.until.FileTool;
import com.example.personal.until.TimeTool;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class FileController {

    /**
     * 获取生成的数据
     *
     * @return
     */
    @RequestMapping(value = "/parsing", method = RequestMethod.POST)
    public String parsingJson(@RequestParam String key, @RequestParam String files, @RequestParam String sql) throws FileNotFoundException {
        int status = 0;
        JSONObject jsonObject = new JSONObject();
        if (key != null && sql != null && files != null) {
            String outputKey = key + "-" + sql.trim();  //create key output dird
            String staticPath = Application.getParsingFilePath();
            jsonObject.put("static", "/parsingfile/out/" + outputKey + "/");
            if (new File(staticPath + "out/" + outputKey).exists()) {
                status = 1;
                List<String> fileByPath = FileTool.getFileByPath(staticPath + "out/" + outputKey + "/", "json");
                jsonObject.put("files", fileByPath);
                jsonObject.put("status", status);
                return jsonObject.toString();
            }
            JSONArray jsonArray = new JSONArray(files);
            List<String> filesPath = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                filesPath.add(staticPath + "input/" + key + "/" + jsonArray.get(i)); //文件的路径
            }
            SparkSqlServer sparkSqlServer = new SparkSqlServer();
            try {
                sparkSqlServer.parsingMoreJson(filesPath, sql, staticPath + "out/"
                        + outputKey + "/");
            } catch (Exception e) {
                key = "";
            } finally {
                sparkSqlServer.close();
            }
            if (key != "") {
                FileTool.mkdirDirectory(staticPath + "out");
                List<String> fileByPath = FileTool.getFileByPath(staticPath + "out/" + outputKey + "/", "json");
                status = 1;
                jsonObject.put("status", status);
                jsonObject.put("files", fileByPath);
                return jsonObject.toString();
            }
        } else {
            jsonObject.put("status", status);
            jsonObject.put("error", "parameter is null");
            return jsonObject.toString();
        }
        jsonObject.put("status", status);
        return jsonObject.toString();
    }

    /**
     * 上传文件
     *
     * @param req
     * @param multiReq
     * @return
     * @throws IOException
     */
    @RequestMapping(value = "/fileupload", method = RequestMethod.POST)
    public String uploadFile(HttpServletRequest req, MultipartHttpServletRequest multiReq) throws IOException {
        String filePath = Application.getParsingFilePath();
        MultipartFile file = multiReq.getFile("file");
        JSONObject jsonObject = new JSONObject();
        int status = 0;
        String key = multiReq.getParameter("data"); //生成的key
        String dirPath = filePath + "input/" + key;  //自动生成的路径
        if (FileTool.mkdirDirectory(dirPath)) {
            status = 1;
            FileOutputStream fos = new FileOutputStream(new File(dirPath + "/" + file.getOriginalFilename()));
            FileInputStream fs = (FileInputStream) file.getInputStream();
            byte[] buffer = new byte[1024];
            int len = 0;
            while ((len = fs.read(buffer)) != -1) {
                fos.write(buffer, 0, len);
            }
            fos.close();
            fs.close();
            jsonObject.put("status", status);
            return jsonObject.toString();
        }
        jsonObject.put("status", status);
        return jsonObject.toString();
    }

    /**
     * 生成key值
     *
     * @return
     */
    @RequestMapping(value = "/createkey", method = RequestMethod.GET)
    public String createKey() {
        return String.valueOf(new Date().getTime()) + "-" + TimeTool.createIntValue(1100000, 1);
    }
}
