package com.iamdonor.adapter;

import java.util.ArrayList;
import java.util.List;

import com.iamadonor.splash.R;
import com.iamdonor.adapter.DonorAdapter.ViewHolder;
import com.iamdonor.model.Country;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CountryAdapter extends BaseAdapter {
	
	private Context mContext;
	private LayoutInflater mInflater;
	private List<Country> mCountryList;
	private ArrayList<Country> mCountryAList;
	
	

	public CountryAdapter(Context mContext, List<Country> mCountryList) {
		super();
		this.mContext = mContext;
		this.mCountryList = mCountryList;
		this.mInflater = LayoutInflater.from(mContext);
		this.mCountryAList = new ArrayList<Country>();
		this.mCountryAList.addAll(mCountryList);
	}
	
	private class ViewHolder{
		TextView placeTextView;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return mCountryList.size();
	}

	@Override
	public Country getItem(int position) {
		// TODO Auto-generated method stub
		return mCountryList.get(position);
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
		
		holder.placeTextView.setText(mCountryList.get(position).getCountryName());
		return convertView;
	}
	
	public void filter(String charText) {
		charText = charText.toLowerCase();
		mCountryList.clear();
		if (charText.length() == 0) {
			mCountryList.addAll(mCountryAList);
		} else {
			for (Country country : mCountryAList) {
				if (charText.equalsIgnoreCase((String) country.getCountryName().subSequence(0,  Math.min(country.getCountryName().length(), charText.length())))) {
					mCountryList.add(country);
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
		mCountryList.clear();
		notifyDataSetChanged();
	}
	
	

}
