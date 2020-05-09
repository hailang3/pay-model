package com.neo.paymodel.common.util;

import sun.misc.BASE64Decoder;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AESUtils {

	static String key = "RFC3E85633C43925";  //16位
    static String iv = "RFC3E85633C43925";  //16位

    public static String encryptAES(String data) throws Exception {

        try {
            Cipher cipher = Cipher.getInstance("AES/CBC/NOPadding");   //参数分别代表 算法名称/加密模式/数据填充方式
            int blockSize = cipher.getBlockSize();

            byte[] dataBytes = data.getBytes();
            int plaintextLength = dataBytes.length;
            if (plaintextLength % blockSize != 0) {
                plaintextLength = plaintextLength + (blockSize - (plaintextLength % blockSize));
            }

            byte[] plaintext = new byte[plaintextLength];
            System.arraycopy(dataBytes, 0, plaintext, 0, dataBytes.length);
            
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.ENCRYPT_MODE, keyspec, ivspec);
            byte[] encrypted = cipher.doFinal(plaintext);
            String str = new sun.misc.BASE64Encoder().encode(encrypted);
            str = str.replaceAll(System.getProperty("line.separator"), "");
            return parseByte2HexStr(str.getBytes());
//            return new sun.misc.BASE64Encoder().encode(encrypted);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static String decryptAES(String data) throws Exception {
        try {
        	String newData = new String(parseHexStr2Byte(data), "UTF-8");
            byte[] encrypted1 = new BASE64Decoder().decodeBuffer(newData);

            Cipher cipher = Cipher.getInstance("AES/CBC/NOPadding");
            SecretKeySpec keyspec = new SecretKeySpec(key.getBytes(), "AES");
            IvParameterSpec ivspec = new IvParameterSpec(iv.getBytes());

            cipher.init(Cipher.DECRYPT_MODE, keyspec, ivspec);

            byte[] original = cipher.doFinal(encrypted1);
            String originalString = new String(original);
            return originalString;
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    
    
    public static String parseByte2HexStr(byte buf[]) {
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < buf.length; i++) {
                String hex = Integer.toHexString(buf[i] & 0xFF);
                if (hex.length() == 1) {
                        hex = '0' + hex;
                }
                sb.append(hex.toLowerCase());
        }
        return sb.toString();
}
    /**将16进制转换为二进制
     * @param hexStr
     * @return
     */
    public static byte[] parseHexStr2Byte(String hexStr) {
            if (hexStr.length()< 1){
            	return null;
            }  
            byte[] result = new byte[hexStr.length()/2];
            for (int i = 0;i < hexStr.length()/2; i++) {
                    int high = Integer.parseInt(hexStr.substring(i*2, i*2+1), 16);
                    int low = Integer.parseInt(hexStr.substring(i*2+1, i*2+2), 16);
                    result[i] = (byte) (high * 16 + low);
            }
            return result;
    }
}
