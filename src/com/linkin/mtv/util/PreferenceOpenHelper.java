package com.linkin.mtv.util;

/**
 * @desc SharedPreferences工具类
 * 
 * @author cjl
 * @date 2014-09-17
 */

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceOpenHelper {

	private Context mContext;
	private String mName;
	private int mMode;

	public PreferenceOpenHelper(Context paramContext, String paramString) {
		this.mContext = paramContext;
		this.mName = paramString;
		this.mMode = 0;
	}

	private SharedPreferences getSharedPreferences() {
		SharedPreferences localSharedPreferences = this.mContext
				.getSharedPreferences(this.mName, this.mMode);
		return localSharedPreferences;
	}

	public Context getContext() {
		return this.mContext;
	}

	public String getString(String paramString1, String paramString2) {
		return getSharedPreferences().getString(paramString1, paramString2);
	}

	public int getInt(String paramString, int paramInt) {
		return getSharedPreferences().getInt(paramString, paramInt);
	}

	public long getLong(String paramString, long paramLong) {
		return getSharedPreferences().getLong(paramString, paramLong);
	}

	public boolean getBoolean(String paramString, boolean paramBoolean) {
		return getSharedPreferences().getBoolean(paramString, paramBoolean);
	}

	public float getFloat(String paramString, float paramFloat) {
		return getSharedPreferences().getFloat(paramString, paramFloat);
	}

	public boolean putString(String paramString1, String paramString2) {
		return getSharedPreferences().edit()
				.putString(paramString1, paramString2).commit();
	}

	public boolean putInt(String paramString, int paramInt) {
		return getSharedPreferences().edit().putInt(paramString, paramInt)
				.commit();
	}

	public boolean putLong(String paramString, long paramLong) {
		return getSharedPreferences().edit().putLong(paramString, paramLong)
				.commit();
	}

	public boolean putBoolean(String paramString, boolean paramBoolean) {
		return getSharedPreferences().edit()
				.putBoolean(paramString, paramBoolean).commit();
	}

	public boolean putFloat(String paramString, float paramFloat) {
		return getSharedPreferences().edit().putFloat(paramString, paramFloat)
				.commit();
	}

	public SharedPreferences.Editor getEditor() {
		return getSharedPreferences().edit();
	}

	public boolean contains(String paramString) {
		return getSharedPreferences().contains(paramString);
	}

	public String getString(int paramInt, String paramString) {
		return getString(this.mContext.getString(paramInt), paramString);
	}

	public int getInt(int paramInt1, int paramInt2) {
		return getInt(this.mContext.getString(paramInt1), paramInt2);
	}

	public float getFloat(int paramInt, float paramFloat) {
		return getFloat(this.mContext.getString(paramInt), paramFloat);
	}

	public boolean getBoolean(int paramInt, boolean paramBoolean) {
		return getBoolean(this.mContext.getString(paramInt), paramBoolean);
	}

	public long getLong(int paramInt, long paramLong) {
		return getLong(this.mContext.getString(paramInt), paramLong);
	}

	public boolean putInt(int paramInt1, int paramInt2) {
		return putInt(this.mContext.getString(paramInt1), paramInt2);
	}

	public boolean putFloat(int paramInt, float paramFloat) {
		return putFloat(this.mContext.getString(paramInt), paramFloat);
	}

	public boolean putBoolean(int paramInt, boolean paramBoolean) {
		return putBoolean(this.mContext.getString(paramInt), paramBoolean);
	}

	public boolean putString(int paramInt, String paramString) {
		return putString(this.mContext.getString(paramInt), paramString);
	}

	public boolean putLong(int paramInt, long paramLong) {
		return putLong(this.mContext.getString(paramInt), paramLong);
	}

	public boolean removeKey(String paramString) {
		return getEditor().remove(paramString).commit();
	}
}
