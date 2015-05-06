package com.linkin.mtv.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.net.Uri;
import android.provider.Settings;
import android.view.WindowManager;

/**
 * @desc 调节亮度的工具类
 * @author liminwei
 * @since 2015-4-27 下午6:51:42
 */
public class BrightnessUtil {

	/**
	 * 获取当前亮度
	 * 
	 * @param activity
	 * @return
	 */
	public static int getScreenBrightness(Activity activity) {

		int nowBrightnessValue = 0;

		ContentResolver resolver = activity.getContentResolver();

		try {
			nowBrightnessValue = android.provider.Settings.System.getInt(
					resolver, Settings.System.SCREEN_BRIGHTNESS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return nowBrightnessValue;
	}

	/** * 设置亮度 */

	public static void setBrightness(Activity activity, int brightness) {
		if (brightness < 50) {// 如果小于50就不再设置了，因为全黑了什么都操作不了
			return;
		}
		WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
		lp.screenBrightness = Float.valueOf(brightness) * (1f / 255f);
		activity.getWindow().setAttributes(lp);
		saveBrightness(activity.getContentResolver(), brightness);// 保存当前设置的值
	}

	/**
	 * 获取最大亮度值，因为这里在设置亮度值的时候，使用的比例是1比225，而真正亮度值的范围是0.0到1.0之间
	 * 
	 * @return
	 */
	public static int getMaxBrightness() {
		return 255 * 1;
	}

	/** * 停止自动亮度调节 */

	public static void stopAutoBrightness(Activity activity) {

		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_MANUAL);
	}

	/**
	 * * 开启亮度自动调节 *
	 * 
	 * @param activity
	 */

	public static void startAutoBrightness(Activity activity) {
		Settings.System.putInt(activity.getContentResolver(),
				Settings.System.SCREEN_BRIGHTNESS_MODE,
				Settings.System.SCREEN_BRIGHTNESS_MODE_AUTOMATIC);
	}

	/** * 保存亮度设置状态 */

	private static void saveBrightness(ContentResolver resolver, int brightness) {

		Uri uri = android.provider.Settings.System
				.getUriFor("screen_brightness");
		android.provider.Settings.System.putInt(resolver, "screen_brightness",
				brightness);
		resolver.notifyChange(uri, null);
	}

}
