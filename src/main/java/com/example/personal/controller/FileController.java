package com.example.personal.controller;

import com.example.personal.server.SparkSqlServer;
import com.example.personal.until.Application;
import com.example.personal.until.Encrypt;
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
    public String parsingJson(@RequestParam String key, @RequestParam String files, @RequestParam String sql) throws Exception {
        int status = 0;
        JSONObject jsonObject = new JSONObject();
        if (key != null && sql != null && files != null) {
            String outputKey = key + "/" + Encrypt.encrypt_Base64(sql.trim());  //create key output dird
            String staticPath = Application.getParsingFilePath();
            jsonObject.put("static", "/parsingfile/out/" + outputKey + "/");
            List<String> fileByPath =  FileTool.getFileByKeyAndSql(key, sql);
            if (fileByPath.size() > 0) {
                status = 1;
                jsonObject.put("files", fileByPath);
                jsonObject.put("status", status);
                return jsonObject.toString();
            }
            JSONArray jsonArray = new JSONArray(files);
            List<String> filesPath = new ArrayList<String>();
            for (int i = 0; i < jsonArray.length(); i++) {
                filesPath.add(staticPath + "input/" + key + "/" + jsonArray.get(i)); //文件的路径
            }
            if (!FileTool.isFileSuffic(filesPath)){
                status = 0;
                jsonObject.put("status", status);
                jsonObject.put("error", "file suffice is not");
                return jsonObject.toString();
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
                fileByPath = FileTool.getFileByPath(staticPath + "out/" + outputKey + "/", "json");
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
        JSONObject result = new JSONObject();
        int status = 0;
        String key = String.valueOf(new Date().getTime()) + "-" + TimeTool.createIntValue(1100000, 1);
        try {
            status = 1;
            result.put("key", Encrypt.encrypt_Base64(key));
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("status", status);
        return result.toString();
    }

    /**
     * 历史查询
     * @param key
     * @return
     */
    @RequestMapping(value = "/historysql", method = RequestMethod.GET)
    public String historyListBykey(@RequestParam String key){
        JSONObject result = new JSONObject();
        int status = 0;
        try {
            String path = Application.getParsingFilePath() + "out/" + key;
            if (new File(path).exists()){
                status = 1;
                List<String> historySql = FileTool.getDirByPath(path);
                List<JSONObject> list = new ArrayList<JSONObject>();
                for (int i = 0; i < historySql.size(); i++) {
                    JSONObject object = new JSONObject();
                    String sql = Encrypt.decrypt_Base64(historySql.get(i));
                    object.put("sql", sql);
                    object.put("files", FileTool.getFileByKeyAndSql(key, sql));
                    list.add(object);
                }
                result.put("sqlpath", historySql);
                result.put("static", "/parsingfile/out/" + key + "/");
                result.put("sqls", list);
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
        result.put("status", status);
        return result.toString();
    }

}
