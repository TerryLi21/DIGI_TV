package com.linkin.mtv.database;

import java.sql.SQLException;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.table.TableUtils;

public class LinkinDatabaseHelper extends ObjectDatabaseHelper {

	private static LinkinDatabaseHelper instance;

	public LinkinDatabaseHelper(Context context, String name, int version) {
		super(context, name, version);
		// TODO Auto-generated constructor stub
	}

	public LinkinDatabaseHelper(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 单例获取该Helper
	 * 
	 * @param context
	 * @return
	 */
	public static synchronized LinkinDatabaseHelper getHelper(Context context,
			String name, int version) {
		context = context.getApplicationContext();
		if (instance == null) {
			synchronized (LinkinDatabaseHelper.class) {
				if (instance == null)
					instance = new LinkinDatabaseHelper(context, name, version);
			}
		}
		return instance;
	}

	/**
	 * 单例获取该Helper
	 * 
	 * @param context
	 * @return
	 */
	public static synchronized LinkinDatabaseHelper getHelper(Context context) {
		context = context.getApplicationContext();
		if (instance == null) {
			synchronized (LinkinDatabaseHelper.class) {
				if (instance == null)
					instance = new LinkinDatabaseHelper(context);
			}
		}
		return instance;
	}

	public void createTable() {
		try {
			TableUtils.createTable(connectionSource, NotiMessage.class);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public void dropTable() {
		try {
			TableUtils.dropTable(connectionSource, NotiMessage.class, true);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
