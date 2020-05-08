package com.neo.paymodel.common.util;

import org.apache.commons.lang.StringUtils;

import java.io.*;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class MD5Util {

	private static String hexDigits[] = { "0", "1", "2", "3", "4", "5", "6", "7", "8", "9", "a", "b", "c", "d", "e", "f" };

	public static String md5(byte b[]) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(b, 0, b.length);
		return byteArrayToHexString(md5.digest());
	}
	
	public static String md5(String raw) throws NoSuchAlgorithmException {
		if(StringUtils.isBlank(raw))
			return "";
		return md5(raw.getBytes());
	}
	public static String md5(String raw, String charset) throws NoSuchAlgorithmException, UnsupportedEncodingException {
		if(StringUtils.isBlank(raw))
			return "";
		if(StringUtils.isBlank(charset))
			charset="UTF-8";
		return md5(raw.getBytes(charset));
	}

	public static String md5File(File file) throws NoSuchAlgorithmException, IOException {

		MessageDigest md = MessageDigest.getInstance("MD5");
		InputStream in = new FileInputStream(file);
		byte[] buffer = new byte[8192];
		int len = -1;
		while ((len = in.read(buffer)) != -1) {
			md.update(buffer, 0, len);
		}
		return byteArrayToHexString(md.digest());

	}

	private static String byteArrayToHexString(byte b[]) {
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < b.length; i++) {
			sb.append(hexDigits[b[i] >>> 4 & 0x0f]);
			sb.append(hexDigits[b[i] & 0x0f]);
		}
		return sb.toString();
	}

	
	private static final char HEX_DIGITS[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F' };  
	  
	private static String toHexString(byte[] b) {  
	    StringBuilder sb = new StringBuilder(b.length * 2);  
	    for (int i = 0; i < b.length; i++) {  
	        sb.append(HEX_DIGITS[(b[i] & 0xf0) >>> 4]);  
	        sb.append(HEX_DIGITS[b[i] & 0x0f]);  
	    }  
	    return sb.toString();  
	}  
	  
	public static String Bit32(String SourceString) throws Exception {  
	    MessageDigest digest = MessageDigest.getInstance("MD5");
	    digest.update(SourceString.getBytes());  
	    byte messageDigest[] = digest.digest();  
	    return toHexString(messageDigest);  
	}  
	  
	public static String Bit16(String SourceString) throws Exception {  
	    return Bit32(SourceString).substring(8, 24);  
	}  
	
	/** 
     * 把中文转成Unicode码 
     * @param str 
     * @return 
     */  
    public static String chinaToUnicode(String str){  
        String result="";  
        for (int i = 0; i < str.length(); i++){  
            int chr1 = (char) str.charAt(i);  
            if(chr1>=19968&&chr1<=171941){//汉字范围 \u4e00-\u9fa5 (中文)  
                result+="\\u" + Integer.toHexString(chr1);  
            }else{  
                result+=str.charAt(i);  
            }  
        }  
        return result;  
    }
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