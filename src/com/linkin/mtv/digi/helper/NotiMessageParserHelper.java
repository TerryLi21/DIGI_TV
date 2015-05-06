package com.linkin.mtv.digi.helper;

import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.linkin.mtv.database.NotiMessage;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-24 下午3:25:05
 */
public class NotiMessageParserHelper {

	public static ArrayList<NotiMessage> parse(String result)
			throws JSONException {

		if (result == null || result.length() == 0) {
			return null;
		}

		ArrayList<NotiMessage> msgs = new ArrayList<NotiMessage>();
		JSONObject obj = new JSONObject(result);
		JSONArray arr = obj.getJSONArray("messageList");
		for (int i = 0; i < arr.length(); i++) {
			JSONObject msgObj = arr.getJSONObject(i);
			NotiMessage msg = new NotiMessage();
			msg.setMsgId(String.valueOf(msgObj.getInt("id")));
			msg.setContent(msgObj.getString("content"));
			msg.setInsertTime(msgObj.getString("insertTime"));
			msg.setLastUpdateTime(msgObj.getString("lastUpdate"));
			msgs.add(msg);
		}

		return msgs;
	}

}
