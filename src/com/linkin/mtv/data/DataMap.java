/**
 * @desc  返回数据的json结构
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-22
 */
package com.linkin.mtv.data;

import java.util.List;

public class DataMap {

	List<ChannelType> typeList;
	List<Channel> channelList;

	public boolean hasData() {
		return typeList != null && channelList != null;
	}

	public List<ChannelType> getTypeList() {
		return typeList;
	}

	public void setTypeList(List<ChannelType> typeList) {
		this.typeList = typeList;
	}

	public List<Channel> getChannelList() {
		return channelList;
	}

	public void setChannelList(List<Channel> channelList) {
		this.channelList = channelList;
	}
	
	
}
