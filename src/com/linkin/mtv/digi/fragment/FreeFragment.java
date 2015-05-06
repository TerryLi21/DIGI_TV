package com.linkin.mtv.digi.fragment;

import java.util.ArrayList;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.linkin.mtv.DataManager;
import com.linkin.mtv.Global;
import com.linkin.mtv.data.Channel;
import com.linkin.mtv.digi.R;
import com.linkin.mtv.digi.activity.PlayChannelActivity;
import com.linkin.mtv.digi.adapter.ChannelGridAdapter;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-22 上午10:05:03
 */
@SuppressLint("NewApi")
public class FreeFragment extends Fragment {

	private GridView gvMian;
	private ChannelGridAdapter adatper;
	private updateReceiver mUpdateReceiver;
	private ArrayList<Channel> channels;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mUpdateReceiver = new updateReceiver();
		IntentFilter filter = new IntentFilter();
		filter.addAction(Global.ACTION_DATA_UPDATE);
		getActivity().registerReceiver(mUpdateReceiver, filter);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		gvMian = (GridView) inflater.inflate(R.layout.fragment_free, null);
		initView();
		setListen();
		return gvMian;
	}

	private void initView() {
		channels = (ArrayList<Channel>) DataManager.getInstance().getFreeList();
		adatper = new ChannelGridAdapter(getActivity(), channels);
		gvMian.setAdapter(adatper);
	}

	private void setListen() {
		gvMian.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent intent = new Intent(getActivity(),
						PlayChannelActivity.class);
				Bundle bu = new Bundle();
				bu.putSerializable("channels", channels);
				bu.putInt("position", position);
				bu.putBoolean("isFree", true);
				intent.putExtras(bu);
				getActivity().startActivity(intent);
			}
		});
	}

	/**
	 * 更新该页面的数据
	 */
	public void updateDate() {
		channels = (ArrayList<Channel>) DataManager.getInstance().getFreeList();
		Log.i("test", "free list channels = " + channels);
		adatper.update(channels);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		getActivity().unregisterReceiver(mUpdateReceiver);
	}

	private class updateReceiver extends BroadcastReceiver {

		@Override
		public void onReceive(Context context, Intent intent) {
			updateDate();
		}
	}
}
