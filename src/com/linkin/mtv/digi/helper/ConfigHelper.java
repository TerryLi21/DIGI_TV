package com.linkin.mtv.digi.helper;

import android.content.Context;

import com.linkin.mtv.player.ISinglePlayer;
import com.linkin.mtv.player.SinglePlayer;

/**
 * @desc
 * @author liminwei
 * @since 2015-5-5 下午6:14:42
 */
public class ConfigHelper {

	public static ILoginHelper getLonginHeler() {
		return new LoginHelperImpl();
	}

	public static ISinglePlayer getSinglePlayer(Context context) {
		return new SinglePlayer(context);
	}

	public static String getDataUrl() { // DataUdateService url
		return "http://121.42.146.91/liveListNew";
	}

	public static String AESDecrypt(String str) { // aes
		return str;
	}

	public static String getWebUrl() {
		return "http://79.143.186.63:8080";
	}

	public static String getCustomerId() {
		return "digi";
	}

	// you can modify the SN according to your device
	public static String getSN() {
		return "50746104";
	}
}
