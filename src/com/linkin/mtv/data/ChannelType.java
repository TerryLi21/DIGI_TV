/**
 * @desc
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-21
 */
package com.linkin.mtv.data;

public class ChannelType extends BaseEntity {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3332031909959128288L;

	private int id;
	private String imagePath;
	private boolean isLock;
	private String name;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getImagePath() {
		return imagePath;
	}

	public void setImagePath(String imagePath) {
		this.imagePath = imagePath;
	}

	public boolean isLock() {
		return isLock;
	}

	public void setLock(boolean isLock) {
		this.isLock = isLock;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
