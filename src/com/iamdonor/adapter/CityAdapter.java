package com.iamdonor.adapter;

import java.util.ArrayList;
import java.util.List;

import com.iamadonor.splash.R;
import com.iamdonor.model.City;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CityAdapter extends BaseAdapter{
	
	private Context mContext;
	private LayoutInflater mInflater;
	private List<City> mCityList;
	private ArrayList<City> mCityAList;
	
	

	public CityAdapter(Context mContext, List<City> mCityList) {
		super();
		this.mContext = mContext;
		this.mCityList = mCityList;
		this.mInflater = LayoutInflater.from(mContext);
		this.mCityAList = new ArrayList<City>();
		this.mCityAList.addAll(mCityList);
	}
	
	private class ViewHolder{
		TextView placeTextView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCityList.size();
	}

	@Override
	public City getItem(int position) {
		// TODO Auto-generated method stub
		return mCityList.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		if (convertView == null) {
			holder = new ViewHolder();
			convertView = mInflater.inflate(R.layout.place_item, null);
			holder.placeTextView = (TextView) convertView.findViewById(R.id.textView_Place);
			convertView.setTag(holder);
		}else{
			holder = (ViewHolder) convertView.getTag();
		}
		
		holder.placeTextView.setText(mCityList.get(position).getCityName());
		return convertView;
	}
	
	public void filter(String charText) {
		charText = charText.toLowerCase();
		mCityList.clear();
		if (charText.length() == 0) {
			mCityList.addAll(mCityAList);
		} else {
			for (City city: mCityAList) {
				if (charText.equalsIgnoreCase((String) city.getCityName().subSequence(0, Math.min(city.getCityName().length(), charText.length())))) {
					mCityList.add(city);
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
		mCityList.clear();
		notifyDataSetChanged();
	}
}
