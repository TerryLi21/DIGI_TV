/**
 * @desc
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-23
 */
package com.linkin.mtv.data;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EpgMap {

	private Map<String, List<Epg>> map = new HashMap<String, List<Epg>>();

	/**
	 * 根据频道id返回节目列表
	 * 
	 * @param id
	 * @return
	 */
	public List<Epg> getEpgList(String id) {
		if (map.containsKey(id)) {
			return map.get(id);
		}
		return null;
	}

	public void setEpgMap(Map<String, List<Epg>> epgMap) {
		this.map = epgMap;
	}

}
