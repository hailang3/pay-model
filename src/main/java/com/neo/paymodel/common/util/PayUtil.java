package com.neo.paymodel.common.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Map.Entry;

public class PayUtil {
	public static final Logger logger = LoggerFactory.getLogger(PayUtil.class);

    private static SimpleDateFormat SMF = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    private static final String MAC_NAME = "HmacSHA1";
    private static final String ENCODING = "UTF-8";
    private static final String QSTRING_EQUAL = "=";
    private static final String QSTRING_SPLIT = "&";
    // 签名key
    private final static String SIGNATURE = "signature";

    /**
     * 使用 HMAC-SHA1 签名方法对对 encryptText 进行签名
     *
     * @param encryptText 被签名的字符串
     * @param encryptKey  密钥
     * @return 返回被加密后的字符串
     * @throws Exception
     */
    public static String HmacSHA1Encrypt(String encryptText, String encryptKey) throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        Mac mac = Mac.getInstance(MAC_NAME);
        mac.init(secretKey);
        byte[] text = encryptText.getBytes(ENCODING);
        byte[] digest = mac.doFinal(text);
        StringBuilder sBuilder = bytesToHexString(digest);
        return sBuilder.toString();
    }

    /**
     * 转换成Hex
     *
     * @param bytesArray
     */
    public static StringBuilder bytesToHexString(byte[] bytesArray) {
        if (bytesArray == null) {
            return null;
        }
        StringBuilder sBuilder = new StringBuilder();
        for (byte b : bytesArray) {
            String hv = String.format("%02x", b);
            sBuilder.append(hv);
        }
        return sBuilder;
    }

    /**
     * 使用 HMAC-SHA1 签名方法对对 encryptText 进行签名
     *
     * @param encryptData 被签名的字符串
     * @param encryptKey  密钥
     * @return 返回被加密后的字符串
     * @throws Exception
     */
    public static String HmacSHA1Encrypt(byte[] encryptData, String encryptKey) throws Exception {
        byte[] data = encryptKey.getBytes(ENCODING);
        SecretKey secretKey = new SecretKeySpec(data, MAC_NAME);
        Mac mac = Mac.getInstance(MAC_NAME);
        mac.init(secretKey);
        byte[] digest = mac.doFinal(encryptData);
        StringBuilder sBuilder = bytesToHexString(digest);
        return sBuilder.toString();
    }

    /**
     * 除去请求要素中的空值和签名参数
     *
     * @param para 请求要素
     * @return 去掉空值与签名参数后的请求要素
     */
    public static Map<String, Object> paraFilter(Map<String, Object> para) {

        Map<String, Object> result = new HashMap<String, Object>();

        if (para == null || para.size() <= 0) {
            return result;
        }
        for (String key : para.keySet()) {
            Object value = para.get(key);
            if (value == null || value.equals("") || key.equalsIgnoreCase(SIGNATURE)) {
                continue;
            }
            result.put(key, (String) value);
        }

        return result;
    }

    /**
     * 把请求要素按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param para   请求要素
     * @param sort   是否需要根据key值作升序排列
     * @param encode 是否需要URL编码
     * @return 拼接成的字符串
     */
    public static String createLinkString(Map<String, Object> para, boolean sort, boolean encode) {

        List<String> keys = new ArrayList<String>(para.keySet());

        if (sort)
            Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            Object key = keys.get(i);
            Object value = para.get(key);

            if (encode) {
                try {
                    key = URLEncoder.encode((String) key, "UTF-8");
                    value = URLEncoder.encode((String) value, "UTF-8");
                    key = URLEncoder.encode((String) key, "UTF-8");
                    value = URLEncoder.encode((String) value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
            }

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                sb.append(key).append(QSTRING_EQUAL).append(value);
            } else {
                sb.append(key).append(QSTRING_EQUAL).append(value).append(QSTRING_SPLIT);
            }
        }
        return sb.toString();
    }

    public static String createLinkString2(Map<String, String> para, boolean sort, boolean encode) {

        List<String> keys = new ArrayList<String>(para.keySet());

        if (sort)
            Collections.sort(keys);

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            Object key = keys.get(i);
            Object value = para.get(key);

            if (encode) {
                try {
                    key = URLEncoder.encode((String) key, "UTF-8");
                    value = URLEncoder.encode((String) value, "UTF-8");
                    key = URLEncoder.encode((String) key, "UTF-8");
                    value = URLEncoder.encode((String) value, "UTF-8");
                } catch (UnsupportedEncodingException e) {
                }
            }

            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                sb.append(key).append(QSTRING_EQUAL).append(value);
            } else {
                sb.append(key).append(QSTRING_EQUAL).append(value).append(QSTRING_SPLIT);
            }
        }
        return sb.toString();
    }

    /**
     * 获取签名
     *
     * @param para 参与签名的要素<key,value>
     * @param key  vivo分配给商户的密钥
     * @return 签名结果
     * @throws Exception
     */
    public static String getPaySign(Map<String, Object> para, String key) {
        //除去数组中的空值和签名参数
        Map<String, Object> filteredReq = paraFilter(para);

        String prestr = createLinkString(filteredReq, true, false); //得到待签名字符串 需要对map进行sort，不需要对value进行URL编码
        String sign = null;
        try {
            if (prestr == null || prestr.equals("")) {
                return sign;
            }
            sign = HmacSHA1Encrypt(prestr, key);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return sign;
    }

    /**
     * 异步通知消息验签
     *
     * @param para 异步通知消息
     * @param key  vivo分配给商户的密钥
     * @return 验签结果
     */
    public static boolean verifySignature(Map<String, Object> para, String key) {
        // 根据参数获取vivo签名
        String signature = getPaySign(para, key);
        // 获取参数中的签名值
        String respSignature = (String) para.get(SIGNATURE);
        System.out.println("服务器签名：" + signature + " | 请求消息中的签名：" + respSignature);
        // 对比签名值
        return null != respSignature && respSignature.equals(signature);
    }

    /**
     * 把getParameterMap转普通map
     *
     * @param decode 是否需要URL解码
     * @return map
     */
    @SuppressWarnings("unchecked")
    public static Map<String, Object> getParameterMap(HttpServletRequest request, boolean decode)
    		throws UnsupportedEncodingException {
        // 参数Map
        Map<String, String[]> properties = request.getParameterMap();
        // 返回值Map
        Map<String, Object> returnMap = new HashMap<String, Object>();
        Iterator<?> entries = properties.entrySet().iterator();
        Entry<String, Object> entry;
        String name = "";
        String value = "";
        while (entries.hasNext()) {
            entry = (Entry<String, Object>) entries.next();
            name = (String) entry.getKey();
            Object valueObj = entry.getValue();
            if (null == valueObj) {
                value = "";
            } else if (valueObj instanceof String[]) {
                String[] values = (String[]) valueObj;
                for (int i = 0; i < values.length; i++) {
                    value = values[i] + ",";
                }
                value = value.substring(0, value.length() - 1);
            } else {
                value = valueObj.toString();
            }
            if (decode) {
                name = URLDecoder.decode(name, "UTF-8");
                value = URLDecoder.decode(value, "UTF-8");
            }
            returnMap.put(name, value);
        }
        return returnMap;
    }

    //生成充值订单号
    public static String genPayOrderNum() {
        return SMF.format(new Date()) + getRandomNumber(4);
    }

    //随机产生的10个数字组合
    private static char[] numberSequence = {'1', '2', '3', '4', '5', '6', '7', '8', '9', '0'};

    public static String getRandomNumber(int codeCount) {
        Random random = getRandomObject();
        StringBuffer randomCode = new StringBuffer();
        for (int i = 0; i < codeCount; i++) {
            String strRand = String.valueOf(numberSequence[random.nextInt(10)]);
            randomCode.append(strRand);
        }
        return randomCode.toString();
    }

    /**
     * 得到单例类的Random对象
     *
     * @return
     */
    public static Random staticRandom = null;

    public static Random getRandomObject() {

        if (staticRandom == null) {
            staticRandom = new Random();
        }
        return staticRandom;
    }

    //实体类转MAP
    public static Map<String, Object> obj2Map(Object obj) {
        if (obj == null) {
            return null;
        }
        Map<String, Object> map = new HashMap<String, Object>();
        Field[] declaredFields = obj.getClass().getDeclaredFields();
        for (Field field : declaredFields) {
            field.setAccessible(true);
            try {
                map.put(field.getName(), String.valueOf(field.get(obj)));
            } catch (IllegalArgumentException e) {
                e.printStackTrace();
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return map;
    }
    public static String readJSONString(HttpServletRequest request) {
        StringBuilder json = new StringBuilder();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null) {
                json.append(line);
            }
        } catch (Exception e) {
            throw new RuntimeException("读取Json字符串出现异常!", e);
        }
        return json.toString();
    }

    /**
     * 把请求要素按照“参数=参数值”的模式用“&”字符拼接成字符串
     *
     * @param para   请求要素
     * @param sort   是否需要根据key值作升序排列
     * @param encode 是否需要URL编码
     * @return 拼接成的字符串
     */
    public static String createLinkString3(JSONObject para, boolean sort, String signName) {

        List<String> keys = new ArrayList<String>(para.keySet());

        if (sort) {
            Collections.sort(keys);
        }

        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < keys.size(); i++) {
            Object key = keys.get(i);
            Object value = para.get(key);
            if (key.equals(signName)) {
                continue;
            }
            if (i == keys.size() - 1) {//拼接时，不包括最后一个&字符
                sb.append(key).append(QSTRING_EQUAL).append(value);
            } else {
                sb.append(key).append(QSTRING_EQUAL).append(value).append(QSTRING_SPLIT);
            }
        }
        return sb.toString();
    }


    public static JSONObject getjsonParam(HttpServletRequest request, boolean decode, boolean jsonReader) {
        JSONObject jsonParam = new JSONObject();
        try {
            if (!jsonReader) {
                jsonParam = new JSONObject(getParameterMap(request, decode));
                if (jsonParam.size() == 1) {
                    String paramStr = jsonParam.keySet().iterator().next();
                    logger.debug("getjsonParam paramStr:{}", paramStr);
                    if (StringUtils.isNotBlank(paramStr)) {
                        JSONObject jsonParam2 = JSON.parseObject(paramStr);
                        if (jsonParam2.size() > 1) {
                            jsonParam = jsonParam2;
                        }
                    } else {
                        jsonParam = new JSONObject();
                    }
                }

            } else {
                String paramStr = PayUtil.readJSONString(request);
                logger.debug("getjsonParam paramStr:{}", paramStr);
                if (StringUtils.isNotBlank(paramStr)) {
                    jsonParam = JSON.parseObject(paramStr);
                } else {
                    jsonParam = new JSONObject();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            logger.error("getjsonParam", e);
        }
        logger.debug("getjsonParam jsonParam:{}", jsonParam);
        return jsonParam;
    }

    public static JSONObject getjsonParam(HttpServletRequest request) {
        return getjsonParam(request, false, false);
    }

}
