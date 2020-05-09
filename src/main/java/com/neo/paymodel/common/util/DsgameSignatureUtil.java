package com.neo.paymodel.common.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class DsgameSignatureUtil {
    public static boolean md5Check(Map params, String publicKey) throws Exception {
        String sign = (String) params.get("sign");
        if(StringUtil.isEmpty(sign)){
            return false;
        }
        String content = getSignCheckContent(params);
        System.out.println("content:"+content);
        return md5CheckContent(content, sign, publicKey);
    }

    public static String getSignCheckContent(Map params) {
        if (params == null){
            return null;
        }
        params.remove("sign");
        params.remove("sign_type");
        StringBuffer content = new StringBuffer();
        List keys = new ArrayList(params.keySet());
        Collections.sort(keys);
        for (int i = 0; i < keys.size(); i++) {
            String key = (String) keys.get(i);
            String value = (String) params.get(key);
            content.append((new StringBuilder()).append(i != 0 ? "&" : "")
                    .append(key).append("=").append(value).toString());
        }

        return content.toString();
    }

    public static boolean md5CheckContent(String content, String sign, String publicKey) throws Exception {
        try {
            System.out.println(EncryptUtil.encodeToMD5(content+"&key="+publicKey));
            return sign.equals(EncryptUtil.encodeToMD5(content+"&key="+publicKey));
        } catch (Exception e) {
            throw new Exception(new StringBuilder()
                    .append("MD5 SIGN ERROR,content=").append(content)
                    .append(",sign=").append(sign).append(",publicKey=")
                    .append(publicKey).toString());
        }
    }
    
    public static String getSign(String content, String publicKey) throws Exception {
        try {
            return EncryptUtil.encodeToMD5(content+"&key="+publicKey);
        } catch (Exception e) {
            throw new Exception("GET MD5 SIGN ERROR!!!");
        }
    }
}
