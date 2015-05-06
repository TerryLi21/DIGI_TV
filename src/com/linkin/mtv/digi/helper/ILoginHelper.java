package com.linkin.mtv.digi.helper;

import android.app.Activity;

/**
 * @desc
 * @author liminwei
 * @since 2015-5-5 下午4:55:40
 */
public interface ILoginHelper {

	void login(Activity activity);

	void addListener();

	void setReceiver();

	void toMainActivity();

}
