package com.neo.paymodel.api.pay.util;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Md5Util {
	private static final char[] HEX_DIGITS = {'0', '1', '2', '3', '4', '5',
            '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f'};
	
	public static String encode(String strTemp)
	{
		StringBuffer hexString = new StringBuffer();
		MessageDigest mdAlgorithm;
		try {
			mdAlgorithm = MessageDigest.getInstance("MD5");
			mdAlgorithm.update(strTemp.getBytes("UTF-8"));
			
			byte[] digest = mdAlgorithm.digest();
			
			for (int i = 0; i < digest.length; i++) {
				strTemp = Integer.toHexString(0xFF & digest[i]);

			    if (strTemp.length() < 2) {
			    	strTemp = "0" + strTemp;
			    }

			    hexString.append(strTemp);
			}
			
		} catch (NoSuchAlgorithmException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return hexString.toString();
	}
	public static String encodeEezie(String str) {
        if (str == null) {
            return null;
        }
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	public static String encodeFlashPay(String str) {
        if (str == null) {
            return null;
        }
        
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA1");
            messageDigest.update(str.getBytes());
            return getFormattedText(messageDigest.digest());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
	 /**
     * Takes the raw bytes from the digest and formats them correct.
     *
     * @param bytes the raw bytes from the digest.
     * @return the formatted bytes.
     */
    private static String getFormattedText(byte[] bytes) {
        int len = bytes.length;
        StringBuilder buf = new StringBuilder(len * 2);
        // 把密文转换成十六进制的字符串形式
        for (int j = 0; j < len; j++) {
            buf.append(HEX_DIGITS[(bytes[j] >> 4) & 0x0f]);
            buf.append(HEX_DIGITS[bytes[j] & 0x0f]);
        }
        return buf.toString();
    }

}
