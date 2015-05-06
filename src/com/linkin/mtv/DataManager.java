/**
 * @desc 数据处理逻辑
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-15
 */
package com.linkin.mtv;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import com.linkin.mtv.data.Channel;
import com.linkin.mtv.data.ChannelType;
import com.linkin.mtv.data.DataMap;
import com.linkin.mtv.digi.helper.DataParserHelper;
import com.linkin.mtv.util.DataFileManager;
import com.linkin.mtv.util.FileUtil;
import com.linkin.mtv.util.StringUtil;

public class DataManager {
	public static final String LIVE_DATA_INFO = "live_data_info"; // 频道分类信息
	private static DataManager mInstance;
	private Context mContext;
	private String mDataInfo;

	// List<ChannelType> typeList = null;
	List<Channel> channelList = null;

	boolean hasInit = false;

	private DataManager(Context context) {
		mContext = context;
		init();
	}

	public static DataManager getInstance() {
		if (mInstance == null) {
			mInstance = new DataManager(MtvApplication.mApp);
		}
		return mInstance;
	}

	private void init() {
		doInit();
	}

	/**
	 * 初始化入口,初始化完成后,更新标志位hasInit
	 */
	private synchronized void doInit() {
		doInitData();
		hasInit = true;
	}

	/**
	 * 初始化数据
	 */
	private void doInitData() {
		if (!DataFileManager.isExistFile(mContext, LIVE_DATA_INFO)) {
			return;
		}

		String con = FileUtil.readDataFile(LIVE_DATA_INFO, mContext);
		if (con == null || con.length() == 0) {
			return;
		}

		mDataInfo = con;
		DataMap dm = null;
		try {
			dm = DataParserHelper.parser(mContext, con);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (dm != null && dm.hasData()) { // 如果解析失败,而且有数据
			// typeList = dm.getTypeList();
			channelList = dm.getChannelList();
			sendBroadcast(Global.ACTION_DATA_UPDATE);
		}
	}

	/**
	 * 返回是否有数据
	 * 
	 * @return
	 */
	public boolean hasData() {
		return channelList != null;
	}

	/**
	 * 是否已经初始化
	 * 
	 * @return
	 */
	public boolean hasInit() {
		return hasInit;
	}

	/**
	 * 获取免费频道
	 * 
	 * @return
	 */
	public List<Channel> getFreeList() {
		if (!hasData()) {
			return null;
		}

		ArrayList<Channel> channels = new ArrayList<Channel>();
		for (Channel ch : channelList) {
			if (ch.getIsFree() == 1 && !StringUtil.isBlank(ch.getPlayUrl())) {// 免费
				channels.add(ch);
			}
		}

		return channels;
	}

	/**
	 * 获取付费频道
	 * 
	 * @return
	 */
	public List<Channel> getPaidList() {
		if (!hasData()) {
			return null;
		}
		ArrayList<Channel> channels = new ArrayList<Channel>();
		for (Channel ch : channelList) {
			if (ch.getIsFree() == 0 && !StringUtil.isBlank(ch.getPlayUrl())) {// 付费
				channels.add(ch);
			}
		}
		return channels;
	}

	/**
	 * 获取所有的频道
	 * 
	 * @return
	 */
	public List<Channel> getAllList() {
		if (!hasData()) {
			return null;
		}
		return channelList;
	}

	private void sendBroadcast(String action) {
		Intent it = new Intent(action);
		mContext.sendBroadcast(it);
	}

	public void update(String result) {
		if (StringUtil.isBlank(result)) {
			return;
		}
		if (result.equals(mDataInfo)) {
			return;
		}
		DataMap dm = null;
		try {
			dm = DataParserHelper.parser(mContext, result);
			Log.i("test", "data manager dm 0 name = "
					+ dm.getChannelList().get(0).getName());
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (dm != null && dm.hasData()) { // 如果解析失败,而且有数据
			mDataInfo = result;
			// typeList = dm.getTypeList();
			channelList = dm.getChannelList();
			Log.i("test", "data manager channels = " + channelList);
			FileUtil.writeDataFile(LIVE_DATA_INFO, result, mContext);

			sendBroadcast(Global.ACTION_DATA_UPDATE);
		}
	}
}
