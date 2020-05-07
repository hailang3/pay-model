package com.neo.paymodel.common.util;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Map;


public class JReflectionUtil {
	/**
	 * 将对象转换成对象
	 * 
	 * @param obj
	 * @return
	 * @throws Exception
	 * @throws IllegalArgumentException
	 */
	public static <T> T formObjectFromObject(Object obj, Class<T> clazz)
			throws IllegalArgumentException, Exception {
		//
		T objOther = (T) clazz.newInstance();
		//
		Class cls = obj.getClass();
		for (; !cls.equals(Object.class); cls = cls.getSuperclass()) {
			//
			Field[] fields = cls.getDeclaredFields();
			for (Field field : fields) {
				// final修饰和static修饰的不修改
				if (Modifier.isFinal(field.getModifiers())
						|| Modifier.isStatic(field.getModifiers())) {
					continue;
				}
				//
				field.setAccessible(true);
				//
				Field fieldOther = null;
				for (Field temp : clazz.getDeclaredFields()) {

					if (temp.getName().equals(field.getName())) {

						fieldOther = clazz.getDeclaredField(temp.getName());
						break;
					}
				}

				if (fieldOther != null) {

					fieldOther.setAccessible(true);
					//
					if (field.get(obj) != null) {
						fieldOther.set(objOther,
								doSpecialTypeFilter(field.get(obj)));
					}
				}
				//
			}
		}
		//
		return objOther;
	}

	/**
	 * 类型过滤，对特殊类型进行转换
	 * 
	 * @param value
	 * @return
	 * @throws Exception
	 */
	public static Object doSpecialTypeFilter(Object value) throws Exception {
		// BigDecimal
		if (value instanceof java.math.BigDecimal) {
			return Double.parseDouble(String.valueOf(value));
		}
		// Timestamp
		if (value instanceof java.sql.Timestamp) {
			return value != null ? DateExtendUtil
					.formatDate2FullDateString(DBUtil.getDBTimeStamp(value))
					: ConstUtil.EMPTY;
		}

		//
		return value;
	}

	/*
	 * 根据Map自动赋值对象，map的key必须和对象的属性名一致
	 */
	public static <T> T formObjectFromMap(Class<T> clazz, Map<String, Object> mapValue) throws InstantiationException, IllegalAccessException, SecurityException, ClassNotFoundException {
		//
		T obj = (T) clazz.newInstance();
		Class<?> cls = Class.forName(clazz.getName());
		for (; !cls.equals(Object.class); cls = cls.getSuperclass()) {
			//
			Field[] field = cls.getDeclaredFields();
			for (Field f : field) {
				// final修饰和static修饰的不修改
				if (Modifier.isFinal(f.getModifiers()) || Modifier.isStatic(f.getModifiers())) {
					continue;
				}
				//
				if (mapValue.get(f.getName()) != null) {
					//
					f.setAccessible(true);
					// System.out.println("["+f.getName()+"]的类型是："+f.getType());
					// value = TypeCoverterUtil.doCovert(value.toString(),
					// f.getType());
					f.set(obj, JTypeCoverterUtil.doSpecialTypeFilter(mapValue.get(f.getName())));

				}
			}
		}
		return obj;
	}
}
