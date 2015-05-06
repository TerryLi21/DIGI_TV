package com.linkin.mtv;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.content.Intent;

import com.linkin.mtv.database.NotiMessage;
import com.linkin.mtv.database.NotiMessageHelper;
import com.linkin.mtv.digi.helper.NotiMessageParserHelper;
import com.linkin.mtv.util.StringUtil;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-24 下午2:06:42
 */
public class NotiMessageManager {

	private Context mContext;
	private static NotiMessageManager mMsgManager;
	private NotiMessageHelper mMsgHelper;

	public static NotiMessageManager getInstance() {

		if (mMsgManager == null) {
			mMsgManager = new NotiMessageManager(MtvApplication.mApp);
		}
		return mMsgManager;
	}

	private NotiMessageManager(Context context) {
		mContext = context;
		mMsgHelper = new NotiMessageHelper(mContext);
	}

	/**
	 * 获取最有一条数据的位置
	 * 
	 * @return
	 */
	public int getLastMsgPosition() {
		List<NotiMessage> list = mMsgHelper.getAll();
		if (list == null || list.size() == 0) {
			return 0;
		}
		int lastPos = 0;
		for (int i = 0; i < list.size(); i++) {
			int curPos = Integer.valueOf(list.get(i).getMsgId());
			if (lastPos < curPos) {
				lastPos = curPos;
			}
		}

		return lastPos;
	}

	public void update(String result) {
		if (StringUtil.isBlank(result)) {
			return;
		}

		ArrayList<NotiMessage> msgs = null;
		try {
			msgs = NotiMessageParserHelper.parse(result);
		} catch (Exception e) {
			e.printStackTrace();
		}
		if (msgs == null || msgs.size() == 0) {// json 格式错误
			return;
		}
		for (int i = 0; i < msgs.size(); i++) {
			mMsgHelper.add(msgs.get(i));
		}

		sendBroadcast(Global.ACTION_MESSAGE_UPDATE);
		return;
	}

	private void sendBroadcast(String action) {
		Intent it = new Intent(action);
		mContext.sendBroadcast(it);
	}

}
