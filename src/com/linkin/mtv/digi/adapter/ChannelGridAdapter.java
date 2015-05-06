package com.linkin.mtv.digi.adapter;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.linkin.mtv.data.Channel;
import com.linkin.mtv.digi.R;
import com.linkin.mtv.digi.helper.ConfigHelper;
import com.linkin.mtv.digi.view.SolaimanTextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-22 下午5:23:44
 */
public class ChannelGridAdapter extends BaseAdapter {

	private Context mContext;
	private List<Channel> channels;

	public ChannelGridAdapter(Context context, List<Channel> channels) {
		mContext = context;
		setChannels(channels);
	}

	private void setChannels(List<Channel> channels) {
		if (channels != null) {
			this.channels = channels;
		} else {
			this.channels = new ArrayList<Channel>(0);
		}
	}

	// 跟新数据
	public void update(List<Channel> channels) {
		setChannels(channels);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return channels.size();
	}

	@Override
	public Channel getItem(int position) {
		return channels.get(position);
	}

	@Override
	public long getItemId(int position) {
		return channels.get(position).getId();
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_channel_gridview, null);
			vh.ivIcon = (ImageView) convertView
					.findViewById(R.id.iv_item_channel_gridview_icon);
			vh.tvName = (SolaimanTextView) convertView
					.findViewById(R.id.tv_item_channel_gridview_name);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}
		// 显示图片的配置
		DisplayImageOptions options = new DisplayImageOptions.Builder()
				.cacheInMemory(true).cacheOnDisc(true)
				.bitmapConfig(Bitmap.Config.RGB_565).build();

		ImageLoader.getInstance().displayImage(
				ConfigHelper.getWebUrl() + channels.get(position).getLogo(),
				vh.ivIcon, options);
		vh.tvName.setText(channels.get(position).getName());

		return convertView;
	}

	private class ViewHolder {
		com.linkin.mtv.digi.view.SolaimanTextView tvName;
		ImageView ivIcon;
	}

}
