package com.iamdonor.adapter;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import com.iamadonor.splash.R;
import com.iamdonor.activity.DrawerMainActivity;
import com.iamdonor.model.EventBean;
import com.robo.loading.MyApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class EventDataAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private LinkedList<EventBean> eventBeanList;
	private LinkedList<EventBean> listOrigin;
	

	public EventDataAdapter(Context context, LinkedList<EventBean> eventBeanList) {
		mContext = context;
		this.eventBeanList = eventBeanList;
		mInflater = LayoutInflater.from(mContext);
		this.listOrigin = new LinkedList<EventBean>();
		this.listOrigin.addAll(eventBeanList);
	}

	public class ViewHolder {	
		TextView textViewEventDesc;
		TextView textViewEventVenue;
		TextView textViewEventContactNum;
		TextView textViewEventDate;
		
		ImageView contactNumImageView;
		ImageView venueDetailImageView;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.events_list_item, null);
			holder.textViewEventDesc = (TextView)view.findViewById(R.id.textView_EventDesc);
			holder.textViewEventVenue = (TextView)view.findViewById(R.id.textView_VenueDetail);
			holder.textViewEventContactNum = (TextView)view.findViewById(R.id.textView_ContactDetail);
			holder.textViewEventDate = (TextView)view.findViewById(R.id.textView_EventDate);
			
			holder.venueDetailImageView = (ImageView)view.findViewById(R.id.imageView_VenueDetail);
			holder.contactNumImageView = (ImageView)view.findViewById(R.id.imageView_ContactDetail);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}

		holder.textViewEventDesc.setText(eventBeanList.get(position).getEventDesc());
		holder.textViewEventVenue.setText(eventBeanList.get(position).getEventVenue());
		holder.textViewEventContactNum.setText(eventBeanList.get(position).getEventContactNum());
		holder.textViewEventDate.setText(eventBeanList.get(position).getEventDate());
		holder.contactNumImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyApplication.dailIntent(mContext, eventBeanList.get(position).getEventContactNum());
				
			}
		});
		
		holder.venueDetailImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyApplication.showOnMap(mContext, eventBeanList.get(position).getEventVenue());
			}
		});
		return view;
	}

	public int getCount() {
		return eventBeanList.size();
	}

	public EventBean getItem(int position) {
		return eventBeanList.get(position);
	}

	public long getItemId(int position) {
		return position;
	}

	/**
	 * Filter
	 * @author 9Android.net
	 *
	 */
	public void filter(String charText) {
		charText = charText.toLowerCase();
		eventBeanList.clear();
		if (charText.length() == 0) {
			eventBeanList.addAll(listOrigin);
		} else {
			for (EventBean eventBean : listOrigin) {
				if (charText.
		            	   equalsIgnoreCase((String) eventBean.getEventName().subSequence(0, charText.length()))) {
					eventBeanList.add(eventBean);
				}
			}
		}
		notifyDataSetChanged();
	}
	
	public void clear(){
		eventBeanList.clear();
	    notifyDataSetChanged();
	    }
}
