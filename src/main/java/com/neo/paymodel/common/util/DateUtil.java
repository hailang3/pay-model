package com.neo.paymodel.common.util;

import java.text.DateFormatSymbols;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 如有未提供的功能，建议使用org.apache.commons.lang.time.DateUtils类。<br>
 * 
 * @see org.apache.commons.lang.time.DateUtils
 * @author wanghh
 * 
 */
public class DateUtil {
	private DateUtil() {
		// util class, prevent from new instance
	}

	/**
	 * 以指定格式返回当时时间
	 * 
	 * @param pattern
	 *            - 日期显示格式
	 * @return the formatted date-time string
	 * @see SimpleDateFormat
	 */
	public static String formatDateTime(String pattern) {
		SimpleDateFormat sdf = new SimpleDateFormat(pattern);
		String now = sdf.format(new Date());
		return now;
	}

	/**
	 * 以指定格式返回指定日期的字符串
	 *
	 * @param pattern
	 *            - 日期显示格式
	 * @param date
	 *            - 需要格式 化的时间
	 * @return the formatted date-time string
	 * @see SimpleDateFormat
	 */
	public static String formatDateTime(String pattern, Date date) {
		String strDate = null;
		String strFormat = pattern;
		SimpleDateFormat dateFormat = null;

		if (date == null)
			return "";

		dateFormat = new SimpleDateFormat(strFormat);
		strDate = dateFormat.format(date);

		return strDate;
	}

	/**
	 * 以指定格式 和指定的Local返回指定日期的字符串
	 *
	 * @param pattern
	 *            - 时间显示格式
	 * @param date
	 *            - 指定的时间
	 * @param locale
	 *            - the locale whose date format symbols should be used
	 * @return the formatted date-time string
	 * @see SimpleDateFormat
	 */
	public static String formatDateTime(String pattern, Date date,
			Locale locale) {
		String strDate = null;
		String strFormat = pattern;
		SimpleDateFormat dateFormat = null;

		if (date == null)
			return "";

		dateFormat = new SimpleDateFormat(strFormat, locale);
		strDate = dateFormat.format(date);

		return strDate;
	}

	/**
	 * Parses a string to produce a Date.
	 *
	 * @param pattern
	 *            - the pattern of the string
	 * @param strDateTime
	 *            - the string to be parsed
	 * @return A Date parsed from the string. In case of error, returns null.
	 */
	public static Date parse(String pattern, String strDateTime) {
		Date date = null;
		if (strDateTime == null || pattern == null)
			return null;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat(pattern);
			formatter.setLenient(false);
			date = formatter.parse(strDateTime);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	/**
	 * 把指定的日期和时间合成一个。
	 *
	 * @param date
	 *            - the date
	 * @param time
	 *            - the time
	 * @return the composed datetime
	 */
	public static Date composeDate(Date date, Date time) {
		if (date == null || time == null)
			return null;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(time);
		Calendar c3 = Calendar.getInstance();
		c3.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH),
				c1.get(Calendar.DATE), c2.get(Calendar.HOUR_OF_DAY),
				c2.get(Calendar.MINUTE), c2.get(Calendar.SECOND));
		return c3.getTime();
	}

	/**
	 * 忽略掉指定日期的时间，只返回日期信息
	 *
	 * @param date
	 * @return
	 */
	public static Date getTheDate(Date date) {
		if (date == null)
			return null;
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date);
		Calendar c2 = Calendar.getInstance();
		c2.set(c1.get(Calendar.YEAR), c1.get(Calendar.MONTH),
				c1.get(Calendar.DATE), 0, 0, 0);
		long millis = c2.getTimeInMillis();
		millis = millis - millis % 1000;
		c2.setTimeInMillis(millis);
		return c2.getTime();
	}

	/**
	 * 给指定(d)的日期添加指定(skipDay)的天数
	 *
	 * @param d
	 * @param skipDay
	 *            需要添加的天数，可以为正数或负数
	 * @return
	 */
	public static Date skipDateTime(Date d, int skipDay) {
		if (d == null)
			return null;
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		calendar.add(Calendar.DATE, skipDay);
		return calendar.getTime();
	}

	/**
	 * 以字符串形式返回指定日期添加指定的天数后的结果。
	 */
	public static String skipDateTime(String timeStr, int skipDay) {
		String pattern = "yyyy-MM-dd HH:mm:ss";
		Date d = parse(pattern, timeStr);
		Date date = skipDateTime(d, skipDay);
		return formatDateTime(pattern, date);
	}

	/**
	 * 同skipDateTime,但是返回的字符串只有日期部分忽略掉时间的部分
	 */
	public static String skipDate(String dateStr, int skipDay) {
		String pattern = "yyyy-MM-dd";
		Date d = parse(pattern, dateStr);
		Date date = skipDateTime(d, skipDay);
		return formatDateTime(pattern, date);
	}

	/**
	 * 给指定的时间加上指定的天数小时数和分钟数后返回。
	 */
	public static String getTime(String timeStr, int skipDay, int skipHour,
			int skipMinute) {
		if (null == timeStr) {
			return null;
		}

		GregorianCalendar cal = new GregorianCalendar();
		cal.setTime(parse("yyyy-MM-dd HH:mm:ss", timeStr));

		cal.add(GregorianCalendar.DAY_OF_MONTH, skipDay);
		cal.add(GregorianCalendar.HOUR_OF_DAY, skipHour);
		cal.add(GregorianCalendar.MINUTE, skipMinute);
		cal.get(GregorianCalendar.DAY_OF_WEEK_IN_MONTH);

		SimpleDateFormat dateFormat = new SimpleDateFormat(
				"yyyy-MM-dd HH:mm:ss");

		return dateFormat.format(cal.getTime());
	}

	/**
	 * 比较指定时间和当前时间，如果早于当前时间就返回True否则返回False 如指定2009-01-01和当前时间比较会返回True。
	 */
	public static boolean dateCompare(String str) {
		boolean bea = false;
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd");
		String isDate = sdf_d.format(new Date());
		Date date1;
		Date date0;
		try {
			date1 = sdf_d.parse(str);
			date0 = sdf_d.parse(isDate);
			if (date0.after(date1)) {
				bea = true;
			}
		} catch (ParseException e) {
			bea = false;
		}
		return bea;
	}

	/**
	 * 具体到月的时间比较，如果早于当前时间就返回True，否则返回False
	 */
	public static boolean monthCompare(String str) {
		boolean bea = false;
		SimpleDateFormat sdf_m = new SimpleDateFormat("yyyy-MM");
		String isMonth = sdf_m.format(new Date());
		Date date1;
		Date date0;
		try {
			date1 = sdf_m.parse(str);
			date0 = sdf_m.parse(isMonth);
			if (date0.after(date1)) {
				bea = true;
			}
		} catch (ParseException e) {
			bea = false;
		}
		return bea;
	}

	/**
	 * 具体到秒的时间比较，如果早于当前时间就返回True，否则返回False
	 */
	public static boolean secondCompare(String str) {
		boolean bea = false;
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		String isDate = sdf_d.format(new Date());
		Date date1;
		Date date0;
		try {
			date1 = sdf_d.parse(str);
			date0 = sdf_d.parse(isDate);
			if (date0.after(date1)) {
				bea = true;
			}
		} catch (ParseException e) {
			bea = false;
		}
		return bea;
	}

	/**
	 * 比较两个具体到秒的时间，如果第一个时间早于第二个时间就返回True，否则返回False
	 *
	 * @param data1
	 * @param date2
	 * @return
	 */
	public static boolean secondCompare(String data1, String date2) {
		boolean bea = false;
		SimpleDateFormat sdf_d = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		Date date1;
		Date date0;
		try {
			date1 = sdf_d.parse(data1);
			date0 = sdf_d.parse(date2);
			if (date0.after(date1)) {
				bea = true;
			}
		} catch (ParseException e) {
			bea = false;
		}
		return bea;
	}

	/**
	 * 判断是否为闫年
	 */
	public static boolean isLeapYear(int year) {
		if ((((year % 4) == 0) && ((year % 100) != 0)) || ((year % 4) == 0)
				&& ((year % 400) == 0)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 得到指定月份的天数
	 *
	 * @param month
	 * @param year
	 * @return
	 */
	public static int getMonthsDays(int month, int year) {
		if ((isLeapYear(year) == true) && (month == 2)) {
			return 29;
		} else if ((isLeapYear(year) == false) && (month == 2)) {
			return 28;
		}

		if ((month == 1) || (month == 3) || (month == 5) || (month == 7)
				|| (month == 8) || (month == 10) || (month == 12)) {
			return 31;
		}
		return 30;
	}

	/**
	 * 得到当前是周几
	 *
	 * @return
	 */
	public static String getWeekDay() {
		DateFormatSymbols symboles = new DateFormatSymbols(Locale.getDefault());
		symboles.setShortWeekdays(new String[] { "", "7", "1", "2", "3", "4",
				"5", "6" });
		SimpleDateFormat date = new SimpleDateFormat("E", symboles);
		return date.format(new Date());
	}

	/**
	 * 得到当前年份
	 */
	public static int getYear() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.YEAR);
	}

	/**
	 * 得到当前月份
	 *
	 * @return
	 */
	public static int getMonth() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.MONTH);
	}

	/**
	 * 得到当前日期
	 *
	 * @return
	 */
	public static int getDay() {
		Calendar c = Calendar.getInstance();
		return c.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 获得本月第一天
	 *
	 * @return
	 */
	public static String getMonthFirstDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_MONTH,
				calendar.getActualMinimum(Calendar.DAY_OF_MONTH));
		SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
		return simpleFormate.format(calendar.getTime());

	}

	/**
	 * 获得本周第一天(周一开始)
	 *
	 * @return
	 */
	public static String getWeekFirstDay() {
		Calendar calendar = Calendar.getInstance();
		calendar.set(Calendar.DAY_OF_WEEK,
				calendar.getActualMinimum(Calendar.DAY_OF_WEEK) + 1);
		SimpleDateFormat simpleFormate = new SimpleDateFormat("yyyy-MM-dd");
		return simpleFormate.format(calendar.getTime());
	}

	/**
	 * 比较日期大小 返回小时
	 *
	 * @author zhaojq
	 * @param date
	 * @param date1
	 * @return
	 */
	public static int diffDour(Date date, Date date1) {
		// return (int) (getMillis(date) - getMillis(date1));
		return (int) ((getMillis(date) - getMillis(date1)) / (3600 * 1000));
	}


	/**
	 * @说明 返回毫秒
	 * @param date
	 * @return
	 */
	public static long getMillis(Date date) {
		Calendar c = Calendar.getInstance();
		c.setTime(date);
		return c.getTimeInMillis();
	}

	public static long diffMinute(Date date, Date date1) {
		// return (int) (getMillis(date) - getMillis(date1));
		return (long) (((getMillis(date) - getMillis(date1)) / 1000) / 60);
	}

	/**
	 * 获得当前时间一个月前的时间
	 * 
	 * @return 一个月前
	 */
	public static String getOneMonthBeforeCurrentTime() {
		Calendar cal = Calendar.getInstance();
		cal.add(cal.MONTH, -1);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String date = sdf.format(cal.getTime());
		return date;
	}

	/**
	 * 得到本季度第一天的日期
	 * 
	 * @Methods Name getFirstDayOfQuarter
	 * @return Date
	 */
	public static Date getFirstDayOfQuarter() {
		Date date = new Date();
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(date);
		int curMonth = cDay.get(Calendar.MONTH);
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
			cDay.set(Calendar.MONTH, Calendar.JANUARY);
		}
		if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
			cDay.set(Calendar.MONTH, Calendar.APRIL);
		}
		if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {
			cDay.set(Calendar.MONTH, Calendar.JULY);
		}
		if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
			cDay.set(Calendar.MONTH, Calendar.OCTOBER);
		}
		cDay.set(Calendar.DAY_OF_MONTH,
				cDay.getActualMinimum(Calendar.DAY_OF_MONTH));
		System.out.println("初 ：" + cDay.getTime());
		return cDay.getTime();
	}

	/**
	 * 判断系统当天是不是 本季度的第一天
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static boolean isQuarterFristDay() {
		Date date = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		Date fistDay = getFirstDayOfQuarter();
		String today = sdf.format(date);
		String fistDate = sdf.format(fistDay);
		System.out.println(today + "=====" + fistDate);
		if (fistDate.equals(today)) {
			return true;
		}
		return false;
	}

	/**
	 * 得到当前时间的 上一个季度的
	 * 
	 * @param args
	 * @throws ParseException
	 */
	public static String getFirstDayUpOfQuarter(Date date) {
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd ");
		// 处理时间 处理为上个月的时间
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, -2);
		Calendar cDay = Calendar.getInstance();
		cDay.setTime(cal.getTime());
		int curMonth = cDay.get(Calendar.MONTH);
		if (curMonth >= Calendar.JANUARY && curMonth <= Calendar.MARCH) {
			cDay.set(Calendar.MONTH, Calendar.JANUARY);
		}
		if (curMonth >= Calendar.APRIL && curMonth <= Calendar.JUNE) {
			cDay.set(Calendar.MONTH, Calendar.APRIL);
		}
		if (curMonth >= Calendar.JULY && curMonth <= Calendar.AUGUST) {
			cDay.set(Calendar.MONTH, Calendar.JULY);
		}
		if (curMonth >= Calendar.OCTOBER && curMonth <= Calendar.DECEMBER) {
			cDay.set(Calendar.MONTH, Calendar.OCTOBER);
		}
		cDay.set(Calendar.DAY_OF_MONTH,
				cDay.getActualMinimum(Calendar.DAY_OF_MONTH));
		System.out.println("的 上一个季度的  ：" + sdf.format(cDay.getTime()));
		return sdf.format(cDay.getTime());
	}
}
