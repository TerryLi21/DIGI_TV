package com.linkin.mtv.database;

import java.sql.SQLException;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.stmt.DeleteBuilder;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-24 下午2:54:12
 */
public class NotiMessageHelper {

	private Dao<NotiMessage, Integer> notiMsgDaoOpe;
	private LinkinDatabaseHelper helper;

	@SuppressWarnings("unchecked")
	public NotiMessageHelper(Context context) {
		try {
			helper = LinkinDatabaseHelper.getHelper(context);
			notiMsgDaoOpe = helper.getDao(NotiMessage.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * 增加一个消息
	 * 
	 * @param msg
	 */
	public void add(NotiMessage msg) {
		try {
			notiMsgDaoOpe.create(msg);
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 删除一条消息
	 * 
	 * @param msgId
	 *            消息的id
	 * @return
	 */
	public int delete(int msgId) {
		try {
			DeleteBuilder<NotiMessage, Integer> deleteBuilder = notiMsgDaoOpe
					.deleteBuilder();
			deleteBuilder.where().eq("msgId", msgId);
			return deleteBuilder.delete();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return -1;
	}

	/**
	 * 获取所有消息
	 * 
	 * @return
	 */
	public List<NotiMessage> getAll() {
		try {
			return notiMsgDaoOpe.queryForAll();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return null;
	}

}
