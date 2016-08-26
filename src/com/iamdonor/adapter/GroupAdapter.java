package com.iamdonor.adapter;

import java.util.ArrayList;
import java.util.List;

import com.iamadonor.splash.R;
import com.iamdonor.model.GroupBean;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class GroupAdapter extends BaseAdapter{
	
	private Context mContext;
	private LayoutInflater mInflater;
	private List<GroupBean> mGroupBeanList;
	private ArrayList<GroupBean> mTempList;
	
	
	public GroupAdapter(Context mContext, List<GroupBean> mGroupBeanList) {
		super();
		this.mContext = mContext;
		this.mGroupBeanList = mGroupBeanList;
		this.mInflater = LayoutInflater.from(mContext);
		this.mTempList = new ArrayList<GroupBean>();
		mTempList.addAll(mGroupBeanList);
	}
	
	private class ViewHolder{
		TextView groupName;
	}
	
	

	@Override
	public int getCount() {
		return mGroupBeanList.size();
	}

	@Override
	public GroupBean getItem(int position) {
		return mGroupBeanList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.group_list_item, null);
			holder.groupName = (TextView) convertView.findViewById(R.id.textView_GroupName);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		holder.groupName.setText(mGroupBeanList.get(position).getGroupName());
		return convertView;
	}
	
	public void filter(String charText) {
		charText = charText.toLowerCase();
		mGroupBeanList.clear();
		if (charText.length() == 0) {
			mGroupBeanList.addAll(mTempList);
		} else {
			for (GroupBean groupBean: mTempList) {
				if (charText.equalsIgnoreCase((String) groupBean.getGroupName().subSequence(0, Math.min(groupBean.getGroupName().length(), charText.length())))) {
					mGroupBeanList.add(groupBean);
				}
			}
		}
		notifyDataSetChanged();
	}

	@Override
	public void notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
		
	public void clear(){
		mGroupBeanList.clear();
		notifyDataSetChanged();
	}
	
	

}
