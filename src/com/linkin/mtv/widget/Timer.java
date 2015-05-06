/**
 * @desc 计时器
 * 
 * @author fire@ipmacro.com
 * @since 2015-3-24
 */
package com.linkin.mtv.widget;

import android.os.SystemClock;

public class Timer {
	long mBaseTime = SystemClock.uptimeMillis();
	
	
	public long getTime(){
		return SystemClock.uptimeMillis() - mBaseTime;
	}
	
	public void setTime(long time){
		mBaseTime = SystemClock.uptimeMillis() - time;
	}
	
	public void reset(){
		setTime(0);
	}
}
