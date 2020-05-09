package com.neo.paymodel.common.util;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.RandomUtils;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 字符串的工具类<br>
 * TODO 这里面这么有些不该在这里面的方法
 * 
 * @author wanghh
 * 
 */
public class StringUtil {
	final static String METHOD_SET = "set";
	final static String METHOD_GET = "get";
	private static final String CHARSET_NAME = "UTF-8";
	
	/**
	 * 生成批次号
	 * @param company_id 商户id
	 * @return
	 */
	public static String createBatchId(long company_id){
		return company_id + new SimpleDateFormat("yyyyMMddssmmhh").format(new Date());
	}
	
	/**
	 * 笨方法：String s = "你要去除的字符串"; <br>
	 * 1.去除空格：s = s.replace('\\s',''); <br>
	 * 2.去除回车：s =s.replace('\n',''); 这样也可以把空格和回车去掉，其他也可以照这样做。<br>
	 * 注：\n 回车 \t 水平制表符 \s空格 \r 换行
	 * 
	 * @param str
	 * @return
	 */
	public static String replaceBlank(String str) {
		String dest = "";
		if (str != null) {
			Pattern p = Pattern.compile("\\s*|\t|\r|\n");
			Matcher m = p.matcher(str);
			dest = m.replaceAll("");
		}
		return dest;
	}

	/**
	 * 校验银行卡
	 * 数字+最多30位 [8,30]
	 * @param account
	 * @return
	 */
	public static boolean isBankAccount(String account){
		if(null==account||account.trim().equals("")){
			return false;
		}
		
		if(!(StringUtils.isNumeric(account.trim()))||account.trim().length()>30||account.trim().length()<8){
			return false;
		}
		return true;
	}
	/**
	 * 校验字符串长度是否合法
	 * @param name
	 * @param minLen  最小长度>=
	 * @param maxLen  最大长度<=
	 * @return
	 */
	public static boolean isStrLengthEffectively(String name,int minLen,int maxLen){
		int len=StringUtils.isBlank(name)?0:name.length();
		if(len<minLen||len>maxLen){
			return false;
		}
		return true;
	}
	
	/**
	 * 将特殊字符转义再切分
	 * 
	 * @param source
	 * @param spliter
	 * @return
	 */
	public static String[] splitStr2Array(String source, String spliter) {

		String regex = spliter;
		if (regex.equals("?") || regex.equals("*") || regex.equals(")")
				|| regex.equals("(") || regex.equals("{") || regex.equals("$")
				|| regex.equals("+") || regex.equals(".") || regex.equals("|")) {

			regex = "[" + regex + "]";
		}

		return source.split(regex);
	}

	/**
	 * 将byte[] 转换成字符串
	 *
	 * @return
	 */
	public static String byte2Hex(byte[] srcBytes) {
		StringBuilder hexRetSB = new StringBuilder();
		for (byte b : srcBytes) {
			String hexString = Integer.toHexString(0x00ff & b);
			hexRetSB.append(hexString.length() == 1 ? 0 : "").append(hexString);
		}
		return hexRetSB.toString();
	}

	/**
	 * 将16进制字符串转为转换成字符串
	 * 
	 * @param source
	 * @return
	 */
	public static byte[] hex2Bytes(String source) {
		byte[] sourceBytes = new byte[source.length() / 2];
		for (int i = 0; i < sourceBytes.length; i++) {
			sourceBytes[i] = (byte) Integer.parseInt(
					source.substring(i * 2, i * 2 + 2), 16);
		}
		return sourceBytes;
	}

	/**
	 * 如果给定字符串为null,则返回空
	 * 
	 * @param text
	 * @return
	 */
	public static String formatString(String text) {
		if (text == null) {
			return "";
		}
		return text;
	}

	/**
	 * 判断字符串是否有数字串加上特定的分隔符组成 如:1,2,3
	 * 
	 * @param value
	 * @param seperator
	 * @return
	 */
	public static boolean isNumberSepString(String value, String seperator) {

		boolean result = true;
		String[] arr = value.split(seperator);
		for (int i = 0; i < arr.length; i++) {

			if (!isNumeric(arr[i]))
				return false;
		}
		return result;
	}

	/**
	 * 判断给定字符串是否是数字
	 * 
	 * @param str
	 * @return
	 */
	public static boolean isNumeric(String str) {
		Pattern pattern = Pattern.compile("[0-9]*");
		Matcher isNum = pattern.matcher(str);
		if (!isNum.matches()) {
			return false;
		}
		return true;
	}

	/** 
     * 手机号验证 
     *  
     * @param  str 
     * @return 验证通过返回true 
     */  
    public static boolean isMobile(String str) {   
    	if(null==str||"".equals(str.trim())){
    		return false;
    	}
        Pattern p = Pattern.compile("^(13[0-9]|14[579]|15[0-3,5-9]|16[0-9]|17[0135678]|18[0-9]|19[189])\\d{8}$"); // 验证手机号
        Matcher m = p.matcher(str);  
        return m.matches();   
    }  
    
    /**
     * 验证固话
     * @param str
     * @return
     */
    public static boolean isFixedLine(String str){
    	if(null==str||"".equals(str.trim())){
    		return false;
    	}
    	Pattern p = Pattern.compile("[0-9]*");
    	//TODO 固话目前只验证为数字
//    	Pattern p=Pattern.compile("^(0[0-9]{2,3})?([2-9][0-9]{6,7})+(\\-[0-9]{1,4})?$");
    	Matcher m=p.matcher(str);
    	return m.matches();
    }
	/**
	 * 首字母大写
	 * 
	 * @param string
	 * @return
	 */
	public static String upperFirstChar(String string) {
		StringBuilder sb = new StringBuilder();
		sb.append((char) (string.charAt(0) - 32)).append(string.substring(1));
		return sb.toString();
	}

	/**
	 * @param fieldName
	 * @return
	 */
	public static String genGetMethodName(String fieldName) {
		StringBuilder sb = new StringBuilder();
		sb.append(METHOD_GET).append(StringUtil.upperFirstChar(fieldName));
		return sb.toString();
	}

	private static char[] RANDOM_CHARS = new char[] { '0', '1', '2', '3', '4',
			'5', '6', '7', '8', '9', 'a', 'b', 'c', 'd', 'e', 'f', 'g', 'h',
			'i', 'j', 'k', 'l', 'm', 'n', 'o', 'p', 'q', 'r', 's', 't', 'u',
			'v', 'w', 'x', 'y', 'z', 'A', 'B', 'C', 'D', 'E', 'F', 'G', 'H',
			'I', 'J', 'K', 'L', 'M', 'N', 'O', 'P', 'Q', 'R', 'S', 'T', 'U',
			'V', 'W', 'X', 'Y', 'Z' };

	public static String randomString(int length) {
		char[] randBuffer = new char[length];
		for (int i = 0; i < randBuffer.length; i++) {
			randBuffer[i] = RANDOM_CHARS[RandomUtils
					.nextInt(RANDOM_CHARS.length)];
		}
		return new String(randBuffer);
	}

	public static String parseDomain(String requestUrl) {
		int eIdx = requestUrl.length();
		if (requestUrl.startsWith("http://")) {
			requestUrl = requestUrl.substring(7);
		} else if (requestUrl.startsWith("https://")) {
			requestUrl = requestUrl.substring(8);
		}
		eIdx = requestUrl.indexOf("/");
		return requestUrl.substring(0, eIdx);
	}
	/**
	 * 是否为十六进制
	 */
	public static boolean isHex(String str){
		if(null==str||"".equals(str)){
			return false;
		}
		
		str=str.toUpperCase().replaceAll("\\\\X", "").replaceAll("\\\\0X", "");
		String regEx="^[0-9a-fA-F·]*$";
		Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(str);  
        return m.matches();
	}
	 /**
	 * 16进制数字字符集
	 */
	 private static String hexString="0123456789ABCDEF";
	 
	 /**
	  * 将字符串编码成16进制数字,适用于所有字符（包括中文）
	  */
	public static String encodeHex(String str) {
		// 根据默认编码获取字节数组
		byte[] bytes = str.getBytes();
		StringBuilder sb = new StringBuilder(bytes.length * 2);
		// 将字节数组中每个字节拆解成2位16进制整数
		for (int i = 0; i < bytes.length; i++) {
			sb.append(hexString.charAt((bytes[i] & 0xf0) >> 4));
			sb.append(hexString.charAt((bytes[i] & 0x0f) >> 0));
		}
		return sb.toString();
	}
	 
	 /**
	  * 将16进制数字解码成字符串,适用于所有字符（包括中文）
	  */
	 public static boolean isHexChineseName(String bytes){
		 try{
			bytes = bytes.toUpperCase().replaceAll("\\\\X", "").replaceAll("\\\\0X", "").replaceAll("·", "");
			ByteArrayOutputStream baos = new ByteArrayOutputStream(bytes.length() / 2);
			// 将每2位16进制整数组装成一个字节
			for (int i = 0; i < bytes.length(); i += 2){
				baos.write((hexString.indexOf(bytes.charAt(i)) << 4 | hexString.indexOf(bytes.charAt(i + 1))));
			}
				
			String res=new String(baos.toByteArray(),Charset.forName("UTF-8"));
			String filterRes= filterChineseName(res);
			if(res.equals(filterRes)){
				return true;
			}
			return false;
		 }catch(Exception e ){
			 e.printStackTrace();
		 }
		return false;
	}

	 /**
     * 转换为字节数组
     * @param str
     * @return
     */
    public static byte[] getBytes(String str){
        if (str != null){
            try {
                return str.getBytes(CHARSET_NAME);
            } catch (UnsupportedEncodingException e) {
                return null;
            }
        }else{
            return null;
        }
    }

    /**
     * @param key
     * @return
     */
    public static String toString(byte[] key) {
        String str = null;
        try{
            if(key != null && key.length > 0) {
                str = new String(key, CHARSET_NAME);
            }
        } catch(Exception e) {
        }
        return str;
    }

    /**
     * @param value
     * @return
     */
    public static boolean isNotBlank(String value) {
        if(value == null || "".equals(value)) {
            return false;
        }
        return true;
    }

    /**
     * 检查指定的字符串列表是否不为空。
     * 
     * @param values 字符串列表
     * @return true/false
     */
    public static boolean areNotEmpty(String... values) {
        boolean result = true;
        if (values == null || values.length == 0) {
            result = false;
        } else {
            for (String value : values) {
                result &= !isEmpty(value);
            }
        }
        return result;
    }

    /**
     * 检查指定的字符串是否为空。
     * <ul>
     * <li>SysUtils.isEmpty(null) = true</li>
     * <li>SysUtils.isEmpty("") = true</li>
     * <li>SysUtils.isEmpty("   ") = true</li>
     * <li>SysUtils.isEmpty("abc") = false</li>
     * </ul>
     * 
     * @param value 待检查的字符串
     * @return true/false
     */
    public static boolean isEmpty(String value) {
        int strLen;
        if (value == null || (strLen = value.length()) == 0) {
            return true;
        }
        for (int i = 0; i < strLen; i++) {
            if ((Character.isWhitespace(value.charAt(i)) == false)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 把二进制转化为大写的十六进制
     * @param bytes
     * @return
     */
    public static String byte2hex(byte[] bytes) {
        StringBuilder sign = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(bytes[i] & 0xFF);
            if (hex.length() == 1) {
                sign.append("0");
            }
            sign.append(hex.toUpperCase());
        }
        return sign.toString();
    }
    
    /**
     * 是否是有效的身份证号，支持15位和18位
     * 包含地区校验，日期校验，校验位校验（18位）
     */
    public static boolean isIDCard(String idCardStr){
    	//idCardStr空或者不是15位，也不是18位
    	if(null==idCardStr||"".equals(idCardStr)||(idCardStr.length()!=15&&idCardStr.length()!=18)){
    		return false;
    	}
    	
    	/**
    	 * 1）十七位数字本体码加权求和公式 S = Sum(Ai * Wi), i = 0, ... , 16 ，先对前17位数字的权求和
    	 * Ai:表示第i位置上的身份证号码数字值 Wi:表示第i位置上的加权因子 Wi: 7 9 10 5 8 4 2 1 6 3 7 9 10 5 8 4 2 
    	 * （2）计算模 Y = mod(S, 11) （3）通过模得到对应的校验码 Y: 0 1 2 3 4 5 6 7 8 9 10 校验码: 1 0 X 9 8 7 6 5 4 3 2
    	 */
    	idCardStr=idCardStr.toUpperCase();
    	String Ai="";
    	String[] Wi = { "7", "9", "10", "5", "8", "4", "2", "1", "6", "3", "7","9", "10", "5", "8", "4", "2" };
    	String[] ValCodeArr = { "1", "0", "X", "9", "8", "7", "6", "5", "4","3", "2" };
    	if(idCardStr.length()==18){
    		Ai=idCardStr.substring(0,17);//前17位
    	}else{
    		Ai=idCardStr.substring(0,6)+"19"+idCardStr.substring(6, 15);//转换为18位身份证，370802940221002 转换为18位后为 370802199402210029
    	}
    	
    	if(isNumeric(Ai)==false){//非数字
    		return false;
    	}
    	
    	// ================ 地区码时候有效 ================
        Hashtable<String, String> h = GetAreaCode();
        if (h.get(Ai.substring(0, 2)) == null) {
           return false;
        }
        // ==============================================

        
    	//================出生年月日是否有效 begin
    	String strYear = Ai.substring(6, 10);// 年份
        String strMonth = Ai.substring(10, 12);// 月份
        String strDay = Ai.substring(12, 14);// 月份
        if (isDate(strYear + "-" + strMonth + "-" + strDay) == false) {
            return false;
        }
        GregorianCalendar gc = new GregorianCalendar();
        SimpleDateFormat s = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if ((gc.get(Calendar.YEAR) - Integer.parseInt(strYear)) > 150
                    || (gc.getTime().getTime() - s.parse(strYear + "-" + strMonth + "-" + strDay).getTime()) < 0) {
               return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (Integer.parseInt(strMonth) > 12 || Integer.parseInt(strMonth) == 0) {
            return false;
        }
        if (Integer.parseInt(strDay) > 31 || Integer.parseInt(strDay) == 0) {
            return false;
        }
      //================出生年月日是否有效 end
        
        // ================ 判断最后一位的值(18位身份证) ================
		if (idCardStr.length() == 18) {
			int TotalmulAiWi = 0;
			for (int i = 0; i < 17; i++) {
				TotalmulAiWi = TotalmulAiWi
						+ Integer.parseInt(String.valueOf(Ai.charAt(i)))
						* Integer.parseInt(Wi[i]);
			}
			int modValue = TotalmulAiWi % 11;
			String strVerifyCode = ValCodeArr[modValue];
			Ai = Ai + strVerifyCode;

			if (Ai.equals(idCardStr) == false) {
				return false;
			}
		}
        // =====================(end)=====================
    	return true;
    }
    
    /**
     * 功能：判断字符串是否为日期格式
     * @return
     */
    public static boolean isDate(String strDate) {
        Pattern pattern = Pattern
                .compile("^((\\d{2}(([02468][048])|([13579][26]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])))))|(\\d{2}(([02468][1235679])|([13579][01345789]))[\\-\\/\\s]?((((0?[13578])|(1[02]))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(3[01])))|(((0?[469])|(11))[\\-\\/\\s]?((0?[1-9])|([1-2][0-9])|(30)))|(0?2[\\-\\/\\s]?((0?[1-9])|(1[0-9])|(2[0-8]))))))(\\s(((0?[0-9])|([1-2][0-3]))\\:([0-5]?[0-9])((\\s)|(\\:([0-5]?[0-9])))))?$");
        Matcher m = pattern.matcher(strDate);
        if (m.matches()) {
            return true;
        } else {
            return false;
        }
    }
    
    /**
     * 过滤出合法的中文姓名
     */
    public static String filterChineseName(String str){
    	if(str==null){
    		return "";
    	}
//    	String regEx = "[\u4E00-\u9FA5·]{2,20}"; //最原始常用中文
    	String regEx = "[\u2E80-\uFE4F·]{2,20}";//考拉
//    	String regEx = "[\u0391-\uFFE5·]{2,20}";//新颜 20170424废弃，无法过滤大点
//    	String regEx = "[\u2E80-\u2EFF\u31C0-\u31EF\u2F00-\u2FDF\u3400-\u4DBF\u4DC0-\u4DFF\u4E00-\u9FA5\uFF00-\uFFEF·]{2,20}";//场景
    	Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(str);  
        StringBuffer s = new StringBuffer("");
        while(m.find()){  
        	s.append(m.group(0));  
        } 
        
        return s.toString();  
    }
    
   /**
    * 过滤特殊字符 目前仅用于50202姓名校验
    * @param str
    * @return
    */
    public static String StringFilter(String str){
    	if(str==null){
    		return "";
    	}
    	// 只允许字母和数字  
        // String regEx = "[^a-zA-Z0-9]";  
        // 清除掉所有特殊字符  
//        String regEx = "[\u4E00-\u9FA5]|[\\x09\\x0A\\x0D\\x20-\\x7E]|[\\xC2-\\xDF][\\x80-\\xBF]|\\xE0[\\xA0-\\xBF][\\x80-\\xBF]  | [\\xE1-\\xEC\\xEE\\xEF][\\x80-\\xBF]{2}  | \\xED[\\x80-\\x9F][\\x80-\\xBF]  | \\xF0[\\x90-\\xBF][\\x80-\\xBF]{2}  | [\\xF1-\\xF3][\\x80-\\xBF]{3}  | \\xF4[\\x80-\\x8F][\\x80-\\xBF]{2}";  
    	String regEx ="^[\\p{L} ·.'-]+$";
    	Pattern p = Pattern.compile(regEx);  
        Matcher m = p.matcher(str);  
        StringBuffer s = new StringBuffer("");
        while(m.find()){  
        	s.append(m.group(0));  
        } 
        
        return s.toString();  
    }
    
    /**
     * 汇总了十六进制和中文
     */
    public static boolean isChineseName(String str){
    	if(isHex(str)){
    		return isHexChineseName(str);
		}else{
			if(str.equals(filterChineseName(str))){
				return true;
			}
		}
    	return false;
    }

    /**
     * 判断是否匹配
     * @param value  需要匹配的字符串
     * @param regExp 正则表达式
     * @return
     */
    public static boolean isMatch(String value,String regExp){
    	if(isEmpty(regExp)||null==value){
    		return false;
    	}
    	
    	Pattern p = Pattern.compile(regExp);  
        Matcher m = p.matcher(value);  
    	return m.matches();
    }

	/**
	 * 身份证脱敏
	 * @param document_no
	 * @return
	 */
	public static String getDesensitizationDocuNoString(String document_no){
		if(StringUtils.isBlank(document_no)){
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		if(document_no.length()>=15){
			sb.append(document_no.substring(0, 6)).append("********").append(document_no.substring(document_no.length()-4));
		}else{
			sb.append(document_no);
		}
		return sb.toString();
	}
	
	/**
	 * 手机号码脱敏
	 * @return
	 */
	public static String getDesensitizationMobileString(String mobile){
		if(StringUtils.isBlank(mobile)){
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		if(mobile.length()==11){
			sb.append(mobile.substring(0, 3)).append("****").append(mobile.substring(mobile.length()-4));
		}else{
			sb.append(mobile);
		}
		return sb.toString();
	}
	
	/**
	 * 银行卡脱敏
	 * @return
	 */
	public static String getDesensitizationBankNoString(String bankNo){
		if(StringUtils.isBlank(bankNo)){
			return "";
		}
		
		StringBuffer sb = new StringBuffer();
		if(bankNo.length()>=8){
			sb.append(bankNo.substring(0, 4)).append("*******").append(bankNo.substring(bankNo.length()-4));
		}else{
			sb.append(bankNo);
		}
		return sb.toString();
	}
    
    private static Hashtable<String, String> GetAreaCode() {
        Hashtable<String, String> hashtable = new Hashtable<String, String>();
        hashtable.put("11", "北京");
        hashtable.put("12", "天津");
        hashtable.put("13", "河北");
        hashtable.put("14", "山西");
        hashtable.put("15", "内蒙古");
        hashtable.put("21", "辽宁");
        hashtable.put("22", "吉林");
        hashtable.put("23", "黑龙江");
        hashtable.put("31", "上海");
        hashtable.put("32", "江苏");
        hashtable.put("33", "浙江");
        hashtable.put("34", "安徽");
        hashtable.put("35", "福建");
        hashtable.put("36", "江西");
        hashtable.put("37", "山东");
        hashtable.put("41", "河南");
        hashtable.put("42", "湖北");
        hashtable.put("43", "湖南");
        hashtable.put("44", "广东");
        hashtable.put("45", "广西");
        hashtable.put("46", "海南");
        hashtable.put("50", "重庆");
        hashtable.put("51", "四川");
        hashtable.put("52", "贵州");
        hashtable.put("53", "云南");
        hashtable.put("54", "西藏");
        hashtable.put("61", "陕西");
        hashtable.put("62", "甘肃");
        hashtable.put("63", "青海");
        hashtable.put("64", "宁夏");
        hashtable.put("65", "新疆");
        hashtable.put("71", "台湾");
        hashtable.put("81", "香港");
        hashtable.put("82", "澳门");
        hashtable.put("91", "国外");
        return hashtable;
    }
    
    public static String getCityName(String cityCode){
    	String cityName=null;
    	Hashtable<String, String> hashtable = GetAreaCode();
    	cityName = hashtable.get(cityCode);
    	return cityName;
    }
    
    /**
	 * 随机生成N位数
	 * @return
	 */
	public static String randomNum(int n){
		DecimalFormat df = new DecimalFormat("0");
		String num =df.format(Math.random()* Math.pow(10, n));
		for (int i =num.length(); i < n;i++) {
			num += "0";
		}
		return num;
	}
	
	/**
	 * 时间戳+3位随机数
	 * @return
	 */
	public static String getUniqueSerialNumber() {
		StringBuilder result = new StringBuilder();
		result.append(System.currentTimeMillis());
		int seq = new Random().nextInt(999);
		if (seq < 10) {
			result.append("00");
		}else if (seq < 100) {
			result.append("0");
		}
		result.append(seq);
		try {
			Thread.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		return result.toString();
	}
	
	/**
	 * 时间戳+7位随机数
	 * @return
	 */
	public static String getOut_trade_no(){
		DateFormat df = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        return df.format(new Date()) + randomNum(7);
	}
	
	
	public static String getSecritIpStr(String ipstr,String key){
		int iplen=ipstr.length();
		int keylen=key.length();
		char[] ipchararry=ipstr.toCharArray();
		char[] keychararry=key.toCharArray();
		char[] newkeychararry=new char[iplen];
		char[] lastchararry=new char[iplen];
		int j=0;
		if(iplen>keylen){
			for(int i=0;i<iplen;i++){
				if(i<keylen){
					newkeychararry[i]=keychararry[i];
				}else{
					j=i-keylen;
					if(j>=keylen){
						j=j-keylen;
					}
					newkeychararry[i]=keychararry[j];
				}
			}
		}
		
		StringBuffer sB1 = new StringBuffer();
		StringBuffer sB2 = new StringBuffer();
		for(int k=0;k<iplen;k++){
			lastchararry[k]=(char)(ipchararry[k]^newkeychararry[k]);
		}
		String lastStr = String.valueOf(lastchararry);
		return lastStr;
	}
}
