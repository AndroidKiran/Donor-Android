package com.iamdonor.activity;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.view.MenuItem;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.devspark.appmsg.AppMsg;
import com.iamadonar.utils.ConnectionDetector;
import com.iamadonor.splash.R;
import com.iamdonor.adapter.DonorAdapter;
import com.iamdonor.model.DonorBean;
import com.iamdonor.model.UserBean;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.robo.loading.MyApplication;
import com.robo.models.WebServiceResponse;
import com.robo.request.WebServiceRequest;
import com.robo.utils.AbstractLoadingRequestListener;

public class DonorListActivity extends BaseAbstractActivity implements OnClickListener{
	private DonorListActivity mContext;
	private Resources mResources;	private List<DonorBean> donorBeansList;
	private Boolean bool;
	private ActionBar mActionBar;
	private Button mJoinGroupButton;
	private Button mInviteFrndButton;
	private TextView mLabelTextView;
	private Bundle bundle;
	private int groupId;
	private String groupName;
	private Intent intent;
	private WebServiceRequest getGroupMemberRequest;
	private WebServiceRequest postJoinGroupRequest;
	private ConnectionDetector connectionDetector;
	private AppMsg appMsg;
	private ProgressBar mProgressBar;
	
	private int pages;
	private int pageInc;
	private LoadMoreListView mDonorsListView;
	
	private Dialog progressDialog;
	private LinkedList<UserBean> memberList;
	private DonorAdapter mDonorAdpter;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.fragment_group_donor);
		
		mContext = DonorListActivity.this;
		mResources = getResources();
		mActionBar = getSupportActionBar();
		mActionBar.setDisplayHomeAsUpEnabled(true);
		mActionBar.setHomeButtonEnabled(true);
		connectionDetector = new ConnectionDetector(mContext);
		mProgressBar = (ProgressBar)findViewById(R.id.donorListProgressBarIndicator);
		mProgressBar.setVisibility(View.GONE);
		bundle = getIntent().getExtras();
		if (bundle != null) {
			setGroupName(bundle.getString("groupName"));
			setGroupId(bundle.getInt("groupId"));
			bool = (Boolean)bundle.getBoolean("groups");
		}
		
		mJoinGroupButton = (Button)findViewById(R.id.button_JoinGroup);
		mJoinGroupButton.setOnClickListener(this);
		mInviteFrndButton = (Button)findViewById(R.id.button_InviteFrnd);
		mInviteFrndButton.setOnClickListener(this);
		
		mLabelTextView = (TextView)findViewById(R.id.txtLabel);
		if (!getGroupName().trim().equals("")) {
			mLabelTextView.setText(getGroupName());
		}
		if (bool) {
			mInviteFrndButton.setVisibility(View.GONE);
		} else {
			mJoinGroupButton.setVisibility(View.GONE);
		}
		mDonorsListView = (LoadMoreListView)findViewById(R.id.listView_Donors);
		getGroupMemberRequest = new WebServiceRequest(8, getGroupId(),1);
		pageInc = 1;
		mDonorsListView.setOnLoadMoreListener(new OnLoadMoreListener() {
        	public void onLoadMore() {
        		pageInc+=1;
        		if (pageInc <= getPages()) {
        			callLoadMore(pageInc);
				}
        	}
		});
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
		mProgressBar.setVisibility(View.VISIBLE);
		getSpiceManager().getFromCache(WebServiceResponse.class, "getGroupMemberRequest", DurationInMillis.ALWAYS_RETURNED, new GetGroupMemberRequestListener());
		getSpiceManager().execute(getGroupMemberRequest, "getGroupMemberRequest", DurationInMillis.ALWAYS_EXPIRED, new GetGroupMemberRequestListener());
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			onBackPressed();
			break;

		default:
			break;
		}
		
		return super.onOptionsItemSelected(item);
	}

	public List<DonorBean> getDonorBeansList() {
		return donorBeansList;
	}

	public void setDonorBeansList(List<DonorBean> donorBeansList) {
		this.donorBeansList = donorBeansList;
	}
	
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		intent = new Intent();
        setResult(1,intent);  
        finish();
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_JoinGroup:
			postJoinGroupRequest = new WebServiceRequest(9, getGroupId());
			if (postJoinGroupRequest != null && connectionDetector.isConnectingToInternet()) {
				mProgressBar.setVisibility(View.VISIBLE);
				getSpiceManager().execute(postJoinGroupRequest,new PostJoinGroupRequestListener());
			}else{
				appMsg = AppMsg.makeText(mContext, "No Network", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
			break;

		default:
			break;
		}
		
	}
	
	private class GetGroupMemberRequestListener implements RequestListener<WebServiceResponse>{
		

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isDonorDataInCache()) {
				 return;
			 }else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isDonorDataInCache()) {
				 return;
			 }
		}

		@Override
		public void onRequestSuccess(WebServiceResponse memberResponse) {
			mProgressBar.setVisibility(View.GONE);
			if (memberResponse != null) {
				MyApplication.getDonorSingleton().setDonorDataInCache(true);
				
				if (!memberResponse.isError()) {
					
					if (memberResponse.getDonors().size() > 0) {
						memberList = new LinkedList<UserBean>();
						memberList.addAll(memberResponse.getDonors());
						
						mDonorAdpter = new DonorAdapter(mContext, memberList);
						mDonorAdpter.notifyDataSetChanged();
						mDonorsListView.setAdapter(mDonorAdpter);
						mDonorsListView.invalidateViews();
					}
					
				}else{
					appMsg = AppMsg.makeText(mContext, memberResponse.getMessage(), AppMsg.STYLE_ALERT);
					appMsg.show();
				}
				
			}else{
				appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
		}
	}
	
	private class PostJoinGroupRequestListener extends AbstractLoadingRequestListener<WebServiceResponse>{
		private Dialog progressDialog;

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isJoinInCache()) {
				 return;
			 }else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isJoinInCache()) {
				 return;
			 }
		}

		@Override
		public void onRequestSuccess(WebServiceResponse joinResponse) {
			mProgressBar.setVisibility(View.GONE);
			if (joinResponse != null) {
				if (!joinResponse.isError()) {
					MyApplication.getDonorSingleton().setJoinInCache(true);
					appMsg = AppMsg.makeText(mContext, joinResponse.getMessage(), AppMsg.STYLE_INFO);
					appMsg.show();
					getSpiceManager().execute(getGroupMemberRequest, "getGroupMemberRequest", DurationInMillis.ALWAYS_EXPIRED, new GetGroupMemberRequestListener());
				}else{
					appMsg = AppMsg.makeText(mContext, joinResponse.getMessage(), AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			} else {
				appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
		}

		@Override
		public void showLoading() {
			progressDialog = MyApplication.showLoading(mContext);
		}

		@Override
		public void hideLoading() {
			MyApplication.dismissLoading(progressDialog);
			progressDialog = null;
		}
	}
	
	private void callLoadMore(int page){
		getGroupMemberRequest = new WebServiceRequest(8, getGroupId(), page);
		getSpiceManager().execute(getGroupMemberRequest, "getGroupMemberRequest", DurationInMillis.ALWAYS_EXPIRED, new GetMoreGroupMemberRequestListener());
	}
	
	private class GetMoreGroupMemberRequestListener implements RequestListener<WebServiceResponse>{
		

		private ArrayList<UserBean> memberTempList;

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isDonorDataInCache()) {
				 return;
			 }else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isDonorDataInCache()) {
				 return;
			 }
		}

		@Override
		public void onRequestSuccess(WebServiceResponse memberResponse) {
			mProgressBar.setVisibility(View.GONE);
			if (memberResponse != null) {
				MyApplication.getDonorSingleton().setDonorDataInCache(true);
				
				if (!memberResponse.isError()) {
					
					if (memberResponse.getDonors().size() > 0) {
						memberTempList = new ArrayList<UserBean>();
						memberTempList.addAll(memberResponse.getDonors());
						
						for (UserBean userBean : memberTempList) {
							memberList.addLast(userBean);
						}
						
						setPages(memberResponse.getPages());
						
						((DonorAdapter)((HeaderViewListAdapter)mDonorsListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();

						// Call onLoadMoreComplete when the LoadMore task, has finished
						mDonorsListView.onLoadMoreComplete();
					}
					
				}else{
					appMsg = AppMsg.makeText(mContext, memberResponse.getMessage(), AppMsg.STYLE_ALERT);
					appMsg.show();
				}
				
			}else{
				appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
		}
	}

	
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (appMsg != null) {
			appMsg.cancelAll(mContext);
			appMsg = null;
		}
	}
	
	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}
	
	
}
