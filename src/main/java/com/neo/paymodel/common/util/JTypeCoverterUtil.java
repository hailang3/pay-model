package com.neo.paymodel.common.util;

import java.math.BigDecimal;

/**
 * 常用基础数据类型转换工具类
 * 
 * @author wanghh
 * @since 2013-04-01
 * 
 */
public class JTypeCoverterUtil {
	/**
	 * 类型过滤，对特殊类型进行转换
	 * 
	 * @param value
	 * @return
	 */
	public static Object doSpecialTypeFilter(Object value) {
		if (value instanceof Double) {
			return BigDecimal.valueOf((Double) value);
		}
		return value;
	}
}
