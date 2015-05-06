package com.linkin.mtv.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

/**
 * @desc 键盘帮助类
 * @author liminwei
 * @since 2015-4-21 上午11:18:07
 */
public class KeyBoardUtil {

	/**
	 * 隐藏软键盘
	 * 
	 * @param activity
	 */
	public static void hideSystemKeyBoard(Activity activity) {
		InputMethodManager imm = (InputMethodManager) activity
				.getSystemService(Context.INPUT_METHOD_SERVICE);
		imm.hideSoftInputFromWindow(activity.getCurrentFocus()
				.getApplicationWindowToken(),
				InputMethodManager.HIDE_NOT_ALWAYS);
	}
}
