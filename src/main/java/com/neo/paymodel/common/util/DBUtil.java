package com.neo.paymodel.common.util;

import org.apache.commons.lang.StringUtils;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public final class DBUtil {
	/**
	 * @param o
	 * @return
	 */
	public static byte getDBByte(Object o) throws Exception {
		byte ret = 0;
		if (o != null) {
			try {
				ret = Byte.parseByte(String.valueOf(o));
			} catch (Exception e) {
				throw new Exception("can not convert db type to byte");
			}
		}
		return ret;
	}

	/**
	 * @param o
	 * @return
	 */
	public static int getDBInt(Object o) throws Exception {
		int ret = 0;
		if (o != null) {
			try {
				ret = Integer.parseInt(String.valueOf(o));
			} catch (Exception e) {
				throw new Exception("can not convert db type to int");
			}
		}
		return ret;
	}

	/**
	 * @param o
	 * @return

	 */
	public static double getDBDouble(Object o) throws Exception {
		double ret = 0;
		if (o != null) {
			try {
				ret = Double.parseDouble(String.valueOf(o));
			} catch (Exception e) {
				throw new Exception("can not convert db type to double");
			}
		}
		return ret;
	}

	/**
	 * @param o
	 * @return

	 */
	public static BigDecimal getDBBigDecimal(Object o) throws Exception {
		BigDecimal ret = null;
		if (o != null) {
			try {
				ret = new BigDecimal(String.valueOf(o));
			} catch (Exception e) {
				throw new Exception("can not convert db type to BigDecimal");
			}
		}
		return ret;
	}

	/**
	 * @param o
	 * @return

	 */
	public static boolean getDBBoolean(Object o) throws Exception {
		boolean ret = false;
		if (o != null) {
			try {
				ret = Boolean.parseBoolean(String.valueOf(o));
			} catch (Exception e) {
				throw new Exception("can not convert db type to boolean");
			}
		}
		return ret;
	}

	/**
	 * @param o
	 * @return

	 */
	public static long getDBLong(Object o) throws Exception {
		long ret = 0;
		if (o != null) {
			try {
				ret = Long.parseLong(String.valueOf(o));
			} catch (Exception e) {
				throw new Exception("can not convert db type to long");
			}
		}
		return ret;
	}

	/**
	 * @param o
	 * @return

	 */
	public static String getDBString(Object o) {
		if (o == null) {
			return "";
		}
		return String.valueOf(o);
	}

	/**
	 * @param o
	 * @return

	 */
	public static Timestamp getDBTimeStamp(Object o) throws Exception {
		Timestamp ret = null;
		if (o != null) {
			try {
				ret = new Timestamp(((Date) o).getTime());
			} catch (Exception e) {
				throw new Exception("can not convert db type to Timestamp");
			}
		}
		return ret;
	}

	/******************************************************* 处理提交请求参数 *******************************************************/
	public static int getParamInt(String value) throws Exception {
		if (StringUtils.isBlank(value)) {
			return 0;
		} else {
			try {
				return getDBInt(value);
			} catch (Exception e) {
				throw new Exception("处理提交请求参数：Int类型异常，转换值value=" + value);
			}
		}
	}

	public static List<Integer> getParamIntList(String value) throws Exception {
		List<Integer> returnList = new ArrayList<Integer>();
		if (StringUtils.isBlank(value)) {
			return returnList;
		} else {
			try {
				String[] idsStr = value.split(ConstUtil.SPLIT_DH);
				for (String id : idsStr) {
					returnList.add(Integer.parseInt(id));
				}
			} catch (Exception e) {
				throw new Exception("处理提交请求参数：Int List类型异常，转换值value=" + value);
			}
			return returnList;
		}
	}

	public static List<String> getParamStringList(String value)
			throws Exception {
		List<String> returnList = new ArrayList<String>();
		if (StringUtils.isBlank(value)) {
			return returnList;
		} else {
			try {
				String[] idsStr = value.split(ConstUtil.SPLIT_DH);
				for (String id : idsStr) {
					returnList.add(id);
				}
			} catch (Exception e) {
				throw new Exception("处理提交请求参数：String List类型异常，转换值value="
						+ value);
			}
			return returnList;
		}
	}

}
