package com.example.personal.until;

import java.io.File;
import java.util.ArrayList;
import java.util.List;


public class FileTool {
    /**
     * 获取目录下的所有文件名称
     *
     * @param path
     * @return
     */
    public static List<String> getFileByPath(String path) {
        List<String> list = new ArrayList<String>();
        File f = new File(path);
        if (f.exists()) {
            File fa[] = f.listFiles();
            for (int i = 0; i < fa.length; i++) {
                File fs = fa[i];
                if (!fs.isDirectory()) {
                    list.add(fs.getName());
                }
                fs = null;
            }
        }
        f = null;
        return list;
    }

    /**
     * 获取目录下的所有文件名称
     *
     * @param path
     * @param suffix
     * @return
     */
    public static List<String> getFileByPath(String path, String suffix) {
        List<String> list = new ArrayList<String>();
        File f = new File(path);
        if (f.exists()) {
            File fa[] = f.listFiles();
            for (int i = 0; i < fa.length; i++) {
                File fs = fa[i];
                String fileName = fs.getName();
                if (!fs.isDirectory()) {
                    if (suffix.equals(fileName.substring(fileName.lastIndexOf(".") + 1))){
                        list.add(fs.getName());
                    }
                }
                fs = null;
            }
        }
        f = null;
        return list;
    }

    /**
     * @param destDirName 路径
     */
    public static boolean mkdirDirectory(String destDirName) {
        File dir = new File(destDirName);
        if (dir.exists()) {
            return true;
        }
        if (!destDirName.endsWith(File.separator)) {
            destDirName = destDirName + File.separator;
        }
        //创建目录
        if (dir.mkdirs()) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 获取文件后缀
     * @param path
     * @return
     */
    public static String getFileSuffic(String path){
        return path.substring(path.lastIndexOf(".") + 1);
    }

    /**
     * 判断数组中文件后缀是否一致
     * @param files
     * @return
     */
    public static boolean isFileSuffic(List<String> files){
        String suffic = getFileSuffic(files.get(0));
        for (int i = 1; i < files.size(); i++) {
            if (!suffic.equals(getFileSuffic(files.get(i)))){
                return false;
            }
        }
        return true;
    }
}
