package com.iamdonor.adapter;

import java.util.ArrayList;
import java.util.List;

import com.iamadonor.splash.R;
import com.iamdonor.activity.DrawerMainActivity;
import com.iamdonor.model.UserBean;
import com.robo.loading.MyApplication;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DonorAdapter extends BaseAdapter {
	private Context mContext;
	private LayoutInflater mInflater;
	private List<UserBean>donorBeanList = null;
	private ArrayList<UserBean>listOrigin;

	public DonorAdapter(Context context, List<UserBean>donorBeanList) {
		mContext = context;
		this.donorBeanList = donorBeanList;
		mInflater = LayoutInflater.from(mContext);
		this.listOrigin = new ArrayList<UserBean>();
		this.listOrigin.addAll(donorBeanList);
	}

	public class ViewHolder {		
		TextView textViewDonorFirstName;
		TextView textViewDonorBloodGroup;
		TextView textViewDonorAge;
		TextView textViewDonorMobileNum;
		TextView textViewDonorEmailId;
		TextView textViewDonorCity;
		
		ImageView bloodGroupImageView;
		ImageView contactNumImageView;
		ImageView emailImageView;
		ImageView locationImageView;
	}

	public View getView(final int position, View view, ViewGroup parent) {
		final ViewHolder holder;
		if (view == null) {
			holder = new ViewHolder();
			view = mInflater.inflate(R.layout.donor_list_item, null);
			holder.textViewDonorFirstName = (TextView)view.findViewById(R.id.textView_dFirstName);
			holder.textViewDonorBloodGroup = (TextView)view.findViewById(R.id.textView_dBloodGroup);
			holder.textViewDonorAge = (TextView)view.findViewById(R.id.textView_dAge);
			holder.textViewDonorMobileNum = (TextView)view.findViewById(R.id.textView_dContactNum);
			holder.textViewDonorEmailId = (TextView)view.findViewById(R.id.textView_dEmailId);
			holder.textViewDonorCity = (TextView)view.findViewById(R.id.textView_dCity);
			
			holder.bloodGroupImageView = (ImageView)view.findViewById(R.id.imageView_DBloodGroup);
			holder.contactNumImageView = (ImageView)view.findViewById(R.id.imageView_MyContact);
			holder.emailImageView = (ImageView)view.findViewById(R.id.imageView_Email);
			holder.locationImageView = (ImageView)view.findViewById(R.id.imageView_Location);
			view.setTag(holder);
		} else {
			holder = (ViewHolder) view.getTag();
		}
		
		holder.textViewDonorFirstName.setText(donorBeanList.get(position).getFirstName()+" "+donorBeanList.get(position).getLastName());
		holder.textViewDonorBloodGroup.setText(donorBeanList.get(position).getBloodGroup() +" "+"BloodGroup");
		holder.textViewDonorAge.setText(donorBeanList.get(position).getDob()+" "+"Years");
		holder.textViewDonorMobileNum.setText(donorBeanList.get(position).getMobNum());
		holder.textViewDonorEmailId.setText(donorBeanList.get(position).getEmailId());
		holder.textViewDonorCity.setText(donorBeanList.get(position).getCity()+","+donorBeanList.get(position).getCountry());
		
		holder.bloodGroupImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyApplication.openLink(mContext,"www.wikipedia.com");
				
			}
		});
		
		holder.contactNumImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyApplication.dailIntent(mContext,donorBeanList.get(position).getMobNum());
				
			}
		});
		
		holder.emailImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyApplication.emailIntent(mContext,donorBeanList.get(position).getEmailId(), "subject", "text");
			}
		});
		
		holder.locationImageView.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				MyApplication.showOnMap(mContext, donorBeanList.get(position).getCity()+","+donorBeanList.get(position).getCountry());
			}
		});
		return view;
	}

	public int getCount() {
		return donorBeanList.size();
	}

	public UserBean getItem(int position) {
		return donorBeanList.get(position);
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
		donorBeanList.clear();
		if (charText.length() == 0) {
			donorBeanList.addAll(listOrigin);
		} else {
			for (UserBean donorBean : listOrigin) {
				if (charText.
		            	   equalsIgnoreCase((String) donorBean.getBloodGroup().subSequence(0, charText.length()))) {
					donorBeanList.add(donorBean);
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
		donorBeanList.clear();
	    notifyDataSetChanged();
	}
}
	


