/**
 * @desc
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-23
 */
package com.linkin.mtv.data;

/**
 * 节目
 */
public class Epg extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6768856242614026928L;

	// 开始时间
	private String beginTime;
	// 结束时间
	private String endTime;
	private String name;

	public String getBeginTime() {
		return beginTime;
	}

	public void setBeginTime(String beginTime) {
		this.beginTime = beginTime;
	}

	public String getEndTime() {
		return endTime;
	}

	public void setEndTime(String endTime) {
		this.endTime = endTime;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
