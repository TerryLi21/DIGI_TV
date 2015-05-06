/**
 * @desc
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-22
 */
package com.linkin.mtv.digi.helper;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;

import com.linkin.mtv.data.Channel;
import com.linkin.mtv.data.ChannelType;
import com.linkin.mtv.data.DataMap;
import com.linkin.mtv.util.StringUtil;

public class DataParserHelper {

	public static DataMap parser(Context context, String con) throws Exception {
		if (StringUtil.isBlank(con)) {
			return null;
		}
		DataMap map = new DataMap();
		JSONObject obj = new JSONObject(con);
		// List<ChannelType> typeList =
		// getTypeList(obj.getJSONArray("typeList"));
		List<Channel> channelList = getChannelList(context,
				obj.getJSONArray("releaseList"));
		map.setChannelList(channelList);
		// map.setTypeList(typeList);
		return map;
	}

	private static List<ChannelType> getTypeList(JSONArray typeArr)
			throws JSONException {
		ArrayList<ChannelType> typeList = new ArrayList<ChannelType>();
		for (int i = 0; i < typeArr.length(); i++) {
			JSONObject obj = typeArr.getJSONObject(i);
			ChannelType type = new ChannelType();
			type.setId(obj.getInt("id"));
			type.setImagePath(obj.getString("imagePath"));
			type.setLock(obj.getBoolean("isLock"));
			type.setName(obj.getString("name"));
			typeList.add(type);
		}
		return typeList;
	}

	private static List<Channel> getChannelList(Context context,
			JSONArray channelArr) throws Exception {
		ArrayList<Channel> channelList = new ArrayList<Channel>();
		for (int i = 0; i < channelArr.length(); i++) {
			JSONObject obj = channelArr.getJSONObject(i);
			Channel channel = new Channel();
			channel.setId(obj.getInt("id"));
			channel.setIsFree(obj.getInt("isFree"));
			channel.setLock(obj.getBoolean("isLock"));
			channel.setP2p(obj.getBoolean("isP2p"));
			channel.setLogo(obj.getString("logo"));
			channel.setMode(obj.getInt("mode"));
			channel.setName(ConfigHelper.AESDecrypt(obj.getString("name")));
			channel.setPlayUrl(obj.getString("playUrl"));
			channel.setSort(obj.getInt("sort"));
			channelList.add(channel);
		}
		return channelList;
	}
}
