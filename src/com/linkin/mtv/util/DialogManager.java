package com.linkin.mtv.util;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-21 下午4:39:28
 */
public class DialogManager {

	private Activity mActivity;
	private ProgressDialog proDialog;

	public DialogManager(Activity activity) {
		mActivity = activity;
	}

	/**
	 * 按下alertDialog按钮后的回调方法
	 */
	public interface AlertDialogClickListen {
		void positiveButton(DialogInterface dialog, int which);

		void negativeButton(DialogInterface dialog, int which);
	}

	/**
	 * 生成AlertDialog并弹出
	 * 
	 * @param title
	 *            不能为空
	 * @param msg
	 * @param postiveText
	 *            如果为null，则没有确定按钮
	 * @param negativeText
	 *            如果为null，则没有取消按钮
	 * @param clickListen
	 *            按下按钮后的回调方法
	 */
	public void createAlertDialog(String title, String msg, String postiveText,
			String negativeText, final AlertDialogClickListen clickListen) {
		AlertDialog.Builder builder = new Builder(mActivity);
		builder.setTitle(title);
		builder.setMessage(msg);
		builder.setIcon(android.R.drawable.ic_dialog_alert);
		if (postiveText != null) {
			builder.setPositiveButton(postiveText,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							clickListen.positiveButton(dialog, which);
						}
					});
		}
		if (negativeText != null) {
			builder.setNegativeButton(negativeText,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							clickListen.negativeButton(dialog, which);
						}
					});
		}
		builder.show();
	}

	/**
	 * 显示等待框
	 * 
	 * @param title
	 *            为空则不显示
	 * @param msg
	 *            不能为空
	 */
	public void showProgressDialog(String title, String msg) {
		if (proDialog == null) {
			proDialog = new ProgressDialog(mActivity);
			proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
			proDialog.setCancelable(true);
			if (title != null) {
				proDialog.setTitle(title);
			}
			proDialog.setMessage(msg);
		}
		proDialog.show();
	}

	/**
	 * 取消等待框
	 */
	public void dismissProgressDialog() {
		if (proDialog != null) {
			proDialog.dismiss();
		}
	}

}
