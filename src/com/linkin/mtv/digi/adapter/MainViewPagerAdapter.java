/**
 * @desc
 * 
 * @author fire@ipmacro.com
 * @since 2015-4-3
 */
package com.linkin.mtv.digi.adapter;

import java.util.List;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.ViewGroup;

public class MainViewPagerAdapter extends FragmentPagerAdapter {

	private List<Fragment> mFragments;

	public MainViewPagerAdapter(FragmentManager fm) {
		super(fm);
	}

	public void setFragments(List<Fragment> fragments) {
		this.mFragments = fragments;
	}

	public List<Fragment> getFragments() {
		return mFragments;
	}

	@Override
	public Fragment getItem(int position) {
		if (mFragments != null && mFragments.size() > 0) {
			return mFragments.get(position);
		} else {
			return null;
		}
	}

	@Override
	public Object instantiateItem(ViewGroup container, int position) {
		return super.instantiateItem(container, position);
	}

	@Override
	public int getCount() {
		return mFragments != null ? mFragments.size() : 0;
	}

	/**
	 * 重写此方法，不做实现，可防止子fragment被回收
	 */
	@Override
	public void destroyItem(ViewGroup container, int position, Object object) {
	}

}