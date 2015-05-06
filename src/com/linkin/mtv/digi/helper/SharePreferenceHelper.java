/**
 * @desc 从私有区或data读取激活码
 * 
 * @author fire@ipmacro.com
 * @since 2015-1-21
 */
package com.linkin.mtv.digi.helper;

import android.content.Context;

import com.linkin.mtv.MtvApplication;
import com.linkin.mtv.util.PreferenceOpenHelper;

public class SharePreferenceHelper extends PreferenceOpenHelper {

	private static final String SHARE_NAME = "base_share";

	private static SharePreferenceHelper sIntance;

	public SharePreferenceHelper(Context paramContext, String paramString) {
		super(paramContext, paramString);
	}

	public synchronized static SharePreferenceHelper getInstance() {
		if (sIntance == null) {
			sIntance = new SharePreferenceHelper(MtvApplication.mApp,
					SHARE_NAME);
		}
		return sIntance;
	}

	public String getCdkey() {
		return super.getString(Keys.CDKEY, null);
	}

	public void setCdKey(String cdkey) {
		super.putString(Keys.CDKEY, cdkey);
	}

	public String getAutokey() {
		return super.getString(Keys.AUTOKEY, null);
	}

	public void setAutokey(String autokey) {
		super.putString(Keys.AUTOKEY, autokey);
	}

	public static interface Keys {
		public static final String CDKEY = "cdkey";
		public static final String AUTOKEY = "autokey";
	}

	public void removeAutokey() {
		super.removeKey(Keys.AUTOKEY);
	}

}