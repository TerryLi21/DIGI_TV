package com.linkin.mtv.digi.adapter;

import java.util.ArrayList;
import java.util.List;

import com.linkin.mtv.digi.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * @desc
 * @author liminwei
 * @since 2015-4-24 下午6:13:46
 */
public class PlanGridAdapter extends BaseAdapter {

	private Context mContext;
	private List<String> plans;

	public PlanGridAdapter(Context mContext, List<String> plans) {
		this.mContext = mContext;
		setPlans(plans);
	}

	public void setPlans(List<String> plans) {
		if (plans == null) {
			this.plans = new ArrayList<String>(0);
		} else {
			this.plans = plans;
		}
	}

	@Override
	public int getCount() {
		return plans.size();
	}

	@Override
	public String getItem(int position) {
		return plans.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		TextView tvContent;
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.item_plan_gridview, null);
			tvContent = (TextView) convertView
					.findViewById(R.id.tv_item_plan_gridview_name);
			convertView.setTag(tvContent);
		} else {
			tvContent = (TextView) convertView.getTag();
		}
		tvContent.setText(plans.get(position));

		return convertView;
	}
}
