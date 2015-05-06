package com.linkin.mtv.util;

import android.annotation.SuppressLint;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

/**
 * @desc 计算日期用到的工具类
 * @author liminwei
 * @since 2015-4-29 下午5:43:22
 */
@SuppressLint("SimpleDateFormat")
public class DateUtil {

	/**
	 * 计算两个日期之前的相差值
	 * 
	 * @param date1
	 *            第一个日期，格式为"yy-MM-dd"
	 * @param date2
	 *            第二个日期，格式为"yy-MM-dd"
	 * @return 相差的天数
	 */

	public static int calculateTwoDaysDistance(String date1, String date2) {
		int diff = 0;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
			Date dt1 = sdf.parse(date1);
			Date dt2 = sdf.parse(date2);
			Calendar cal1 = Calendar.getInstance();
			cal1.setTime(dt1);
			Calendar cal2 = Calendar.getInstance();
			cal2.setTime(dt2);
			long time0 = cal1.getTimeInMillis();
			long time1 = cal2.getTimeInMillis();
			long millis_diff = Math.abs(time0 - time1);
			diff = (int) (millis_diff / (1000 * 3600 * 24));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return diff;
	}

	/**
	 * 计算传入日期与当天之间相差的天数
	 */
	public static int calculateFromTodayDistance(String date) {
		int diff = 0;
		Calendar c = Calendar.getInstance();
		Date dt = c.getTime();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
		String today = sdf.format(dt);
		diff = calculateTwoDaysDistance(date, today);

		return diff;
	}

}
