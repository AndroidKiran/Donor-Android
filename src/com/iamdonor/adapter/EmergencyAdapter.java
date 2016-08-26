package com.iamdonor.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.iamadonor.splash.R;
import com.iamdonor.activity.DrawerMainActivity;
import com.iamdonor.model.EmergencyBean;
import com.robo.loading.MyApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EmergencyAdapter extends BaseAdapter{
	
	private Context mContext;
	private LayoutInflater inflater;
	private LinkedList<EmergencyBean> mEmergencyList;
	private LinkedList<EmergencyBean> mTempList;

	public EmergencyAdapter(Context mContext, LinkedList<EmergencyBean> mEmergencyList) {
		super();
		this.mContext = mContext;
		this.mEmergencyList = mEmergencyList;
		this.mTempList = new LinkedList<EmergencyBean>();
		this.mTempList.addAll(mEmergencyList);
		inflater = LayoutInflater.from(mContext);
	}
	
	private class ViewHolder{
		TextView mBloodGroupTextView;
		TextView mAddressTextView;
		TextView mContactTextView;
		
		ImageView PlaceImageView;
		ImageView contactNumImageView;
	}

	@Override
	public int getCount() {
		return mEmergencyList.size();
	}

	@Override
	public EmergencyBean getItem(int position) {
		return mEmergencyList.get(position);
	}

	@Override
	public long getItemId(int position) {
		return position;
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = inflater.inflate(R.layout.emergency_list_item, null);
			holder.mBloodGroupTextView = (TextView)convertView.findViewById(R.id.textView_Emergency_BloodGroup);
			holder.mAddressTextView = (TextView)convertView.findViewById(R.id.textView_Emergency_Place);
			holder.mContactTextView = (TextView)convertView.findViewById(R.id.textView_Emergency_ContactNum);
		
			holder.PlaceImageView = (ImageView)convertView.findViewById(R.id.imageView_EShowOnMap);
			holder.contactNumImageView = (ImageView)convertView.findViewById(R.id.imageView_EContactNum);
			convertView.setTag(holder);
		}else{
			holder =(ViewHolder) convertView.getTag();
		}
		
		holder.mBloodGroupTextView.setText(mEmergencyList.get(position).getBloodGroup());
		holder.mAddressTextView.setText(mEmergencyList.get(position).getPlace());
		holder.mContactTextView.setText(mEmergencyList.get(position).getContactNum());
		
		holder.PlaceImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyApplication.showOnMap(mContext, mEmergencyList.get(position).getPlace());
			}
		});
		
		holder.contactNumImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyApplication.dailIntent(mContext, mEmergencyList.get(position).getContactNum());
			}
		});
		
		return convertView;
	}
	
/*
	@Override
	public boolean notifyDataSetChanged() {
		super.notifyDataSetChanged();
	}
		*/
	public void clear(){
		mEmergencyList.clear();
		notifyDataSetChanged();
	}

}
