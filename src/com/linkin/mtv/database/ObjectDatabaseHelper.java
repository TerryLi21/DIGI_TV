package com.linkin.mtv.database;

/**
 * @desc 抽象第三方对象数据库基类
 * 
 * @author cjl
 * @since 2014-9-18
 */

import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;

public abstract class ObjectDatabaseHelper extends OrmLiteSqliteOpenHelper {

	private Map<String, Dao> mDaoMap = new HashMap<String, Dao>();
	private final static int DEFAULT_VERSION = 2;
	private final static String DEFAULT_DB_NAME = "object.db";

	protected ObjectDatabaseHelper(Context context) {
		super(context, DEFAULT_DB_NAME, null, DEFAULT_VERSION);
	}

	protected ObjectDatabaseHelper(Context context, String dbName, int dbVersion) {
		super(context, dbName, null, dbVersion);
	}

	/**
	 * 创建表
	 */
	public abstract void createTable();

	/**
	 * 销毁表
	 */
	public abstract void dropTable();

	@Override
	public void onCreate(SQLiteDatabase database,
			ConnectionSource connectionSource) {
		createTable();
	}

	@Override
	public void onUpgrade(SQLiteDatabase database,
			ConnectionSource connectionSource, int oldVersion, int newVersion) {
		dropTable();
		onCreate(database, connectionSource);
	}

	public synchronized Dao getDao(Class clazz) throws SQLException {
		Dao dao = null;
		String className = clazz.getSimpleName();

		if (mDaoMap.containsKey(className)) {
			dao = mDaoMap.get(className);
		}
		if (dao == null) {
			dao = super.getDao(clazz);
			mDaoMap.put(className, dao);
		}
		return dao;
	}

	/**
	 * 释放资源
	 */
	@Override
	public void close() {
		super.close();

		for (String key : mDaoMap.keySet()) {
			Dao dao = mDaoMap.get(key);
			dao = null;
		}
	}

}
