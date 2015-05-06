/**
 * @desc
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-23
 */
package com.linkin.mtv.digi.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

import com.linkin.mtv.data.Epg;
import com.linkin.mtv.data.EpgMap;
import com.linkin.mtv.util.StringUtil;

public class EpgParserhelper {

	public static EpgMap parser(String con) throws Exception {

		if (StringUtil.isBlank(con)) {
			return null;
		}

		JSONObject obj = new JSONObject(con);
		JSONArray channelArr = obj.getJSONArray("releaseList");
		Map<String, List<Epg>> map = new HashMap<String, List<Epg>>();
		for (int i = 0; i < channelArr.length(); i++) {
			JSONObject chObj = channelArr.getJSONObject(i);
			int cid = chObj.getInt("id");
			JSONObject proObj = chObj.getJSONObject("propMap");
			List<Epg> epgList = getEpgList(proObj.getJSONArray("program"));
			map.put(String.valueOf(cid), epgList);
			Log.i("test", "cid = " + cid);
			Log.i("test", "epgList size= " + epgList.size());
		}
		Log.i("test", "map = " + map);
		EpgMap epgMap = new EpgMap();
		epgMap.setEpgMap(map);
		return epgMap;
	}

	private static List<Epg> getEpgList(JSONArray arr) throws JSONException {
		List<Epg> epgList = new ArrayList<Epg>();
		// for (int i = 0; i < arr.length(); i++) {
		// JSONObject obj = arr.getJSONObject(i);
		// Epg epg = new Epg();
		// epg.setBeginTime(obj.getString("beginTime"));
		// epg.setEndTime(obj.getString("endTime"));
		// epg.setName(obj.getString("name"));
		// epgList.add(epg);
		// Log.i("test", "epg name = " + epg.getName());
		Epg epg = new Epg();
		epg.setBeginTime("2015-4-23 16:20:00");
		epg.setEndTime("2015-4-23 19:20:00");
		epg.setName("test");
		epgList.add(epg);
		// }

		return epgList;
	}

}
