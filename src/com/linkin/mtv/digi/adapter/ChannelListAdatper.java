package com.linkin.mtv.digi.adapter;

import com.linkin.mtv.digi.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-22 上午10:14:39
 */
public class ChannelListAdatper extends BaseAdapter {

	private Context mContext;

	public ChannelListAdatper(Context context) {
		mContext = context;
	}

	@Override
	public int getCount() {
		return 0;
	}

	@Override
	public Object getItem(int position) {
		return null;
	}

	@Override
	public long getItemId(int position) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_channel_listview, null);
			vh.tvTitle = (TextView) convertView
					.findViewById(R.id.tv_channel_list_item_title);
			vh.tvContent = (TextView) convertView
					.findViewById(R.id.tv_channel_list_item_content);
			vh.ivIcon = (ImageView) convertView
					.findViewById(R.id.iv_channel_list_item_icon);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		return convertView;
	}

	private class ViewHolder {
		TextView tvTitle, tvContent;
		ImageView ivIcon;
	}

}
