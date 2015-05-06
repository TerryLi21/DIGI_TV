/**
 * @desc  VideoView Info 帮助类
 * 
 * @author fire@ipmacro.com
 * @since 2015-3-23
 */
package com.linkin.mtv.widget;

public class VideoViewInfo {

	protected Timer timer701 = null;
	protected Timer timer702 = null;
	protected Timer timer = null;
	protected boolean playing = false;
	protected int position = -1;
	protected int times701 = 0;

	public VideoViewInfo() {
		timer701 = null;
		timer702 = null;
		timer = new Timer();
	}

	public long getPlayTime() {
		return timer.getTime();
	}

	/**
	 * 检测是否是有效的701状态值
	 * 
	 * @param what
	 * @return
	 */
	protected boolean check701(int what) {
		boolean bo = playing;
		if (what == 702 || what == 3) {
			playing = true;
			timer702 = new Timer();
		} else if (what == 701) {
			playing = false;
		}
		if (what == 701) {
			if (!bo) {
				return false;
			}
			if (timer702 == null) { // 如果还没有播放过,退出
				return false;
			}
			if (timer702.getTime() < 1000) { // 702出现太短的间隔出现 701,过滤
				return false;
			}

			if (timer701 != null && timer701.getTime() < 1000) { // 如果上一次701出现的间隔小于
				return false;
			}

			timer701 = new Timer();
			times701++;
		}
		return true;
	}

	/**
	 * 检测播放器的位置,是否有效
	 * 
	 * @param pos
	 * @return
	 */
	protected boolean checkPostion(int pos) {
		if (pos > 0) { // 还有没有有关播放起来,不处理
			int old = position;
			position = pos;
			if (old > -1 && old == pos) { // 如果前后位置一样,返回false
				return false;
			}
		}
		return true;
	}

	public int getTimes701() {
		return times701;
	}

	public void setTimes701(int times701) {
		this.times701 = times701;
	}
}
