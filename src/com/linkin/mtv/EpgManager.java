/**
 * @desc epg处理器
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-23
 */
package com.linkin.mtv;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;

import com.linkin.mtv.data.Epg;
import com.linkin.mtv.data.EpgMap;
import com.linkin.mtv.digi.helper.EpgParserhelper;
import com.linkin.mtv.util.DataFileManager;
import com.linkin.mtv.util.FileUtil;
import com.linkin.mtv.util.StringUtil;

public class EpgManager {
	public static final String EPG_DATA_INFO = "epg_data_info"; // 频道分类信息
	private Context mContext;
	private static EpgManager mInstance;

	private EpgMap mEpgMap;
	String mDataInfo;

	private EpgManager(Context context) {
		mContext = context;
		init();
	}

	public static EpgManager getInstance() {
		if (mInstance == null) {
			mInstance = new EpgManager(MtvApplication.mApp);
		}
		return mInstance;
	}

	private void init() {
		if (!DataFileManager.isExistFile(mContext, EPG_DATA_INFO)) {
			return;
		}

		String con = FileUtil.readDataFile(EPG_DATA_INFO, mContext);
		if (StringUtil.isBlank(con)) {
			return;
		}
		mDataInfo = con;
	}

	/**
	 * 获取当前的节目
	 * 
	 * @param cid
	 * @return
	 */
	@SuppressLint("SimpleDateFormat")
	public Epg getCurEpg(String cid) {
		List<Epg> list = getEpgList(cid);
		Epg curEpg = null;
		if (list != null) {
			Calendar c = Calendar.getInstance();
			SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");

			for (int i = 0; i < list.size(); i++) {
				try {
					// 根据时间判断当前正在播放的节目
					Date today = c.getTime();

					String beginTime = list.get(i).getBeginTime();
					String endTime = list.get(i).getEndTime();
					Date beginDate = sdf.parse(beginTime);
					Date endDate = sdf.parse(endTime);
					if ((today.getTime() - beginDate.getTime() > 0)
							&& (today.getTime() - endDate.getTime() < 0)) {
						curEpg = list.get(i);
						break;
					}
				} catch (ParseException e) {
					e.printStackTrace();
				}
			}
		}
		return curEpg;
	}

	public List<Epg> getEpgList(String cid) {
		if (mEpgMap != null) {
			List<Epg> list = mEpgMap.getEpgList(cid);
			return list;
		}
		return null;
	}

	public boolean update(String result) {
		if (StringUtil.isBlank(result)) {
			return false;
		}

		if (result.equals(mDataInfo)) { // 数据已经存在
			return true;
		}

		EpgMap em = null;
		try {
			em = EpgParserhelper.parser(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (em == null) { // json 格式错误
			return false;
		}
		mDataInfo = result;
		mEpgMap = em;

		FileUtil.writeDataFile(EPG_DATA_INFO, result, mContext);

		sendBroadcast(Global.ACTION_EPG_UPDATE);
		return true;
	}

	private void sendBroadcast(String action) {
		Intent it = new Intent(action);
		mContext.sendBroadcast(it);
	}
}
