package com.example.personal.until;

import java.util.Base64;

public class Encrypt {
    /***
     * Base64加密
     * @param str 需要加密的参数
     * @return
     * @throws Exception
     */
    public static String encrypt_Base64(String str) throws Exception {
        return Base64.getEncoder().encodeToString(str.getBytes("UTF-8"));
    }

    /***
     * Base64解密
     * @param str 需要解密的参数
     * @return
     * @throws Exception
     */
    public static String decrypt_Base64(String str) throws Exception {
        return new String(Base64.getDecoder().decode(str), "UTF-8");
    }
}
