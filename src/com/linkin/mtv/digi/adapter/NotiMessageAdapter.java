package com.linkin.mtv.digi.adapter;

import java.util.ArrayList;
import java.util.List;

import com.linkin.mtv.database.NotiMessage;
import com.linkin.mtv.digi.R;
import com.linkin.mtv.digi.view.SolaimanTextView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-24 下午4:04:11
 */
public class NotiMessageAdapter extends BaseAdapter {

	private List<NotiMessage> msgs;
	private Context mContext;

	public NotiMessageAdapter(Context mContext, List<NotiMessage> msgs) {
		this.mContext = mContext;
		setMsgs(msgs);
	}

	private void setMsgs(List<NotiMessage> msgs) {
		if (msgs == null) {
			this.msgs = new ArrayList<NotiMessage>();
		} else {
			this.msgs = msgs;
		}
	}

	/**
	 * 更新信息
	 * 
	 * @param msgs
	 */
	public void update(List<NotiMessage> msgs) {
		setMsgs(msgs);
		notifyDataSetChanged();
	}

	@Override
	public int getCount() {
		return msgs.size();
	}

	@Override
	public NotiMessage getItem(int position) {
		return msgs.get(position);
	}

	@Override
	public long getItemId(int position) {
		return Integer.valueOf(msgs.get(position).getMsgId());
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh = null;
		if (convertView == null) {
			vh = new ViewHolder();
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_noti_message_listview, null);
			vh.tvMsg = (SolaimanTextView) convertView
					.findViewById(R.id.tv_item_noti_message_listview_msg);
			vh.tvTime = (SolaimanTextView) convertView
					.findViewById(R.id.tv_item_noti_message_listview_time);
			convertView.setTag(vh);
		} else {
			vh = (ViewHolder) convertView.getTag();
		}

		vh.tvMsg.setText(msgs.get(position).getContent());
		String time = msgs.get(position).getLastUpdateTime();
		time = time.substring(0, time.lastIndexOf(":"));
		vh.tvTime.setText(time);

		return convertView;
	}

	private class ViewHolder {
		SolaimanTextView tvMsg, tvTime;
	}

}
