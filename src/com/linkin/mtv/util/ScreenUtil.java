package com.linkin.mtv.util;

/**
 * @desc 屏幕大小获取和单位转换工具类
 * 
 * @author cjl
 * @date 2013-06-12
 */

import android.content.Context;
import android.graphics.Point;
import android.util.DisplayMetrics;
import android.view.WindowManager;

public class ScreenUtil {

	/**
	 * 屏幕宽高
	 * 
	 * @param context
	 * @return
	 */
	public static int[] getScreenSize(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		dm = context.getApplicationContext().getResources().getDisplayMetrics();
		int screenWidth = dm.widthPixels;
		int screenHeight = dm.heightPixels;

		return new int[] { screenWidth, screenHeight };
	}

	/**
	 * 根据手机分辨率将dp转为px单位
	 */
	public static int dip2px(Context context, float dpValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (dpValue * scale + 0.5f);
	}

	/**
	 * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
	 */
	public static int px2dip(Context context, float pxValue) {
		final float scale = context.getResources().getDisplayMetrics().density;
		return (int) (pxValue / scale + 0.5f);
	}

	/**
	 * 获取屏幕分辨率
	 * 
	 * @return
	 */
	public static Point getDisplayScreenResolution(Context context) {
		DisplayMetrics dm = new DisplayMetrics();
		android.view.Display display = ((WindowManager) context
				.getSystemService(Context.WINDOW_SERVICE)).getDefaultDisplay();
		display.getMetrics(dm);

		int screen_h = 0, screen_w = 0;
		screen_w = dm.widthPixels;
		screen_h = dm.heightPixels;
		return new Point(screen_w, screen_h);
	}

	/**
	 * 屏幕宽高，字符串形式
	 * 
	 * @param context
	 * @return
	 */
	public static String getScreenSizeStr(Context context) {
		int[] screenSize = ScreenUtil.getScreenSize(context);
		return screenSize[0] + "*" + screenSize[1];
	}
}
