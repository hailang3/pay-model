package com.neo.paymodel.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Calendar;
import java.util.Date;

public class EncryptUtil {
	/** 日志 */
	private static Logger logger = LoggerFactory.getLogger(EncryptUtil.class);

	private static final String CHARSET_NAME = "UTF-8";

	/**
	 * md5加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeToMD5(String str) {
		return encode(str, "MD5");
	}
	
	public static String newencodeToMD5(String str) {
		return newencode(str, "MD5");
	}

	/**
	 * sha加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeToSHA(String str) {
		return encode(str, "SHA");
	}

	/**
	 * sha-1加密
	 * 
	 * @param str
	 * @return
	 */
	public static String encodeToSHA1(String str) {
		return encode(str, "SHA-1");
	}

	/**
	 * 用base64算法进行加密
	 * 
	 * @param str
	 *            需要加密的字符串
	 * @return base64加密后的结果
	 */
	public static String encodeToBase64(String str) {
		try {
			return new String(Base64.encodeBase64(str.getBytes(CHARSET_NAME)),CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			logger.error("字符串(" + str + ")base64加密异常：" + e.getMessage());
		}
		return null;
	}

	/**
	 * 用base64算法进行解密
	 * 
	 * @param str
	 *            需要解密的字符串
	 * @return base64解密后的结果
	 * @throws IOException
	 */
	public static String decodeToBase64(String str) throws IOException {
		try {
			return new String(Base64.decodeBase64(str.getBytes(CHARSET_NAME)),
					CHARSET_NAME);
		} catch (UnsupportedEncodingException e) {
			logger.error("字符串(" + str + ")base64加密异常：" + e.getMessage());
		}
		return null;
	}

	private static String encode(String str, String method) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		MessageDigest md = null;
		String dstr = null;
		try {
			md = MessageDigest.getInstance(method);
			try {
				md.update(str.getBytes(CHARSET_NAME));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				logger.debug("编码异常：" + e.getMessage() + "");
			}
			byte[] mdByte = md.digest();
			int j = mdByte.length;
			char mdChar[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = mdByte[i];
				mdChar[k++] = hexDigits[byte0 >>> 4 & 0xf];
				mdChar[k++] = hexDigits[byte0 & 0xf];
			}
			dstr = new String(mdChar);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.debug("加密异常：" + e.getMessage() + "");
		}
		return dstr;
	}
	
	private static String newencode(String str, String method) {
		char hexDigits[] = { '0', '1', '2', '3', '4', '5', '6', '7', '8', '9',
				'a', 'b', 'c', 'd', 'e', 'f' };
		MessageDigest md = null;
		String dstr = null;
		try {
			md = MessageDigest.getInstance(method);
			try {
				md.update(str.getBytes(CHARSET_NAME));
			} catch (UnsupportedEncodingException e) {
				e.printStackTrace();
				logger.debug("编码异常：" + e.getMessage() + "");
			}
			byte[] mdByte = md.digest();
			int j = mdByte.length;
			char mdChar[] = new char[j * 2];
			int k = 0;
			for (int i = 0; i < j; i++) {
				byte byte0 = mdByte[i];
				mdChar[k++] = hexDigits[byte0 >>> 4 & 0xf];
				mdChar[k++] = hexDigits[byte0 & 0xf];
			}
			dstr = new String(mdChar);
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
			logger.debug("加密异常：" + e.getMessage() + "");
		}
		return dstr;
	}

	public static void main(String[] args) throws IOException {
		String user = "123456789";
		System.out.println("原始字符串 " + user);
		System.out.println("MD5加密 " + encodeToMD5(user));
		System.out.println("SHA加密 " + encodeToSHA(user));
		System.out.println("SHA加密 " + encodeToSHA1(user));
		System.out.println("SHA解密 " + encodeToSHA1("3333"));
		String base64Str = encodeToBase64("jun0130");
		System.out.println("Base64加密 " + base64Str);
		System.out.println("Base64解密 " + decodeToBase64(base64Str));

		String requestUrl = "v1/test/666adfadsfadsf/index";
		String content = "{\"content\":123456}";
		String requestMethod = "GET";
		String secret = "b3b6a44f991a4fe8aa9466ced4f13360";
		long timestamp = 1489661620;// new Date().getTime();
		String str = String
				.format("request_url=%s&content=%s&request_method=%s&timestamp=%d&secret=%s",
						requestUrl, content, requestMethod, timestamp, secret);
		String md5 = EncryptUtil.encodeToMD5(str);
		System.out.println("============ " + md5);

		String xx2 = "request_url=v1/commons/terminal&content={\"deviceId\":\"7e64c76a663d4bbeeadb6a0c42fa75f4\",\"appVersion\":\"1.0\",\"source\":\"ios\",\"deviceVersion\":\"10.2.1\",\"longitude\":0,\"devicePlatform\":\"ios\",\"latitude\":0,\"deviceManufacturer\":\"Apple “Administrator”的 iPhone (2)\",\"network\":\"wifi\",\"deviceModel\":\"iPhone6,1\"}&request_method=POST&timestamp=1492055440&secret=";
		String xx = EncryptUtil.encodeToMD5(xx2);
		System.out.println("===xx========= " + xx);
		String authorization = "Basi";
		if (authorization.indexOf("Basic ") == -1
				|| authorization.split("Basic ").length != 2) {
			System.out.println("===8888888====== " + authorization);
		} else {
			authorization = authorization.split("Basic ")[1];
			System.out.println("===xx===333444444433====== " + authorization);
		}

		Date now = Calendar.getInstance().getTime();
		long nowTime = now.getTime() / 1000;
		System.out.println("===xx===nowTime====== " + nowTime);

		System.out.println("===base64password====== "
				+ encodeToBase64("123456a"));
	}

}
