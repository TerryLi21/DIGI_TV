/**
 * @desc 频道信息类
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-21
 */
package com.linkin.mtv.data;

public class Channel extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8471975152430197692L;

	private int id;
	/**
	 * 是否为免费频道 0为免费1为付費
	 */
	private int isFree;
	private boolean isLock;
	private boolean isP2p;
	private String logo;
	private int mode;
	private String name;
	private String playUrl;
	private int sort;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getIsFree() {
		return isFree;
	}

	public void setIsFree(int isFree) {
		this.isFree = isFree;
	}

	public boolean isLock() {
		return isLock;
	}

	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}

	public boolean isP2p() {
		return isP2p;
	}

	public void setP2p(boolean isP2p) {
		this.isP2p = isP2p;
	}

	public String getLogo() {
		return logo;
	}

	public void setLogo(String logo) {
		this.logo = logo;
	}

	public int getMode() {
		return mode;
	}

	public void setMode(int mode) {
		this.mode = mode;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPlayUrl() {
		return playUrl;
	}

	public void setPlayUrl(String playUrl) {
		this.playUrl = playUrl;
	}

	public int getSort() {
		return sort;
	}

	public void setSort(int sort) {
		this.sort = sort;
	}

}
