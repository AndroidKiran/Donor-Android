package com.iamdonor.child.frgament;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.devspark.appmsg.AppMsg;
import com.iamadonar.utils.ConnectionDetector;
import com.iamadonar.utils.Validation;
import com.iamadonor.splash.R;
import com.iamdonor.activity.DonorListActivity;
import com.iamdonor.adapter.GroupAdapter;
import com.iamdonor.model.GroupBean;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.robo.loading.MyApplication;
import com.robo.models.WebServiceResponse;
import com.robo.request.WebServiceRequest;
import com.robo.userinterface.TabInterface;

public class MyGroupFragment extends BaseAbstractFragment implements OnItemClickListener,TabInterface{
	
	private View mMyGroupView;
	private SherlockFragmentActivity mContext;
	private Resources mResources;
	private Button mCreateButton;
	private View addGroupAlertDialogView;
	private View mAddGroupAlertDialogView;
	private EditText mEditTextGroupName;
	private Button mAddGroupButton;
	private Intent intent;
	private WebServiceRequest getSpecificGroupRequest;
	private Bundle bundle;
	private GroupBean groupBean;
	private ActionBar mActionBar;
	private ConnectionDetector connectionDetector;
	
	private WebServiceRequest postGroupRequest;
	private AppMsg appMsg;
	private Dialog progressDialog;
	private GroupAdapter mGroupAdapter;
	private LinkedList<GroupBean> groupBeanList;
	private ProgressBar mProgressBar;
	private int pages;
	private int pageInc;
	private PullAndLoadListView mMyGroupListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getSherlockActivity();
		mResources = getResources();
		mActionBar = mContext.getSupportActionBar();
		getSpecificGroupRequest = new WebServiceRequest(4, "specific",1);
		connectionDetector = new ConnectionDetector(mContext);
		pageInc = 1;
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mMyGroupView = inflater.inflate(R.layout.fragment_mygroups, container,false);
		setHasOptionsMenu(true);
		mMyGroupListView = (PullAndLoadListView) mMyGroupView.findViewById(R.id.listView_MyGroup);
		mMyGroupListView.setOnItemClickListener(this);
		mProgressBar = (ProgressBar)mMyGroupView.findViewById(R.id.myGrpProgressBarIndicator);
		mProgressBar.setVisibility(View.GONE);
		
		mMyGroupListView.setOnRefreshListener(new OnRefreshListener() {

			public void onRefresh() {
				callRefresh(1);
			}
		});

		
			
		mMyGroupListView.setOnLoadMoreListener(new OnLoadMoreListener() {
        	public void onLoadMore() {
        		pageInc+=1;
        		if (pageInc <= getPages()) {
        			callLoadMore(pageInc);
				}
        	}
		});
		
		return mMyGroupView;
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setMenuVisibility(true);
	}
	
	@Override
	public void onStart() {
        super.onStart();
        mProgressBar.setVisibility(View.VISIBLE);
		getSpiceManager().getFromCache(WebServiceResponse.class, "getSpecificGroupRequest", DurationInMillis.ALWAYS_RETURNED, new GetGroupRequestListener());
    	getSpiceManager().execute(getSpecificGroupRequest, "getSpecificGroupRequest", DurationInMillis.ONE_MINUTE, new GetGroupRequestListener());

    }  
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		setGroupBean((GroupBean)parent.getItemAtPosition(position));
		intent = new Intent(mContext, DonorListActivity.class);
		bundle = new Bundle();
		bundle.putString("groupName", getGroupBean().getGroupName());
		bundle.putInt("groupId", getGroupBean().getGroupId());
		bundle.putBoolean("groups", false);
		intent.putExtras(bundle);
		startActivityForResult(intent, 1);
	}
	
	private void showAddGroupAlertDialog(){
		final AlertDialog alertDialogBuilder = new AlertDialog.Builder(mContext).create();
		LayoutInflater layoutInflater = LayoutInflater.from(mContext);
		mAddGroupAlertDialogView = layoutInflater.inflate(R.layout.create_group_dialog, null);
		mEditTextGroupName = (EditText)mAddGroupAlertDialogView.findViewById(R.id.editText_GroupName);
		
		alertDialogBuilder.setView(mAddGroupAlertDialogView);
		mAddGroupAlertDialogView.findViewById(R.id.button_CreateGroup).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				if (checkValidation()) {
					postGroupRequest = new WebServiceRequest(5, mEditTextGroupName.getText().toString());
					if (postGroupRequest != null && connectionDetector.isConnectingToInternet()) {
						mProgressBar.setVisibility(View.VISIBLE);
						getSpiceManager().execute(postGroupRequest, new PostGroupRequestListener());
					}else{
						appMsg = AppMsg.makeText(mContext, "No Network", AppMsg.STYLE_ALERT);
						appMsg.show();
					}
				}
				alertDialogBuilder.dismiss();
			}
			
		});
		
		mAddGroupAlertDialogView.findViewById(R.id.button_Cancel).setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View v) {
				alertDialogBuilder.dismiss();
			}
		});
		
		alertDialogBuilder.show();
		
	}
	
	private class PostGroupRequestListener implements RequestListener<WebServiceResponse>{
		

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			 if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isPostNewGroupInCache()) {
				 return;
			 }else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isPostNewGroupInCache()) {
				 return;
			 }
		}

		@Override
		public void onRequestSuccess(WebServiceResponse groupResponse) {
			mProgressBar.setVisibility(View.GONE);
			if (groupResponse != null) {
				MyApplication.getDonorSingleton().setPostNewGroupInCache(true);
				if (!groupResponse.isError()) {
					appMsg = AppMsg.makeText(mContext, groupResponse.getMessage(), AppMsg.STYLE_INFO);
					appMsg.show();
					
					 if (connectionDetector.isConnectingToInternet()) {
						 getSpiceManager().execute(getSpecificGroupRequest, "getSpecificGroupRequest", DurationInMillis.ALWAYS_EXPIRED, new GetGroupRequestListener());
					 } else {
						 getSpiceManager().getFromCache(WebServiceResponse.class, "getSpecificGroupRequest", DurationInMillis.ALWAYS_RETURNED, new GetGroupRequestListener());
					 }
				}else{
					appMsg = AppMsg.makeText(mContext, groupResponse.getMessage(), AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			} else {
				appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
		}
	}
	
	
	private class GetGroupRequestListener implements RequestListener<WebServiceResponse>{

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			 if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isMyGroupsDataInCache()) {
				 return;
			 }else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isMyGroupsDataInCache()) {
				 return;
			 }
		}

		@Override
		public void onRequestSuccess(WebServiceResponse groupResponse) {
			mProgressBar.setVisibility(View.GONE);
			if (groupResponse != null) {
				MyApplication.getDonorSingleton().setMyGroupsDataInCache(true);
				if (!groupResponse.isError()) {
					
					if (groupResponse.getGroups().size() > 0) {
						groupBeanList = new LinkedList<GroupBean>();
						groupBeanList.addAll(groupResponse.getGroups());
						setPages(groupResponse.getPages());
						mGroupAdapter = new GroupAdapter(mContext, groupBeanList);
						mGroupAdapter.notifyDataSetChanged();
						mMyGroupListView.setAdapter(mGroupAdapter);
						mMyGroupListView.invalidateViews();
					}
					
				}else{
					appMsg = AppMsg.makeText(mContext, groupResponse.getMessage(), AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			}else{
				appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
		}
	}
	
	private void callLoadMore(int page){
		
		getSpecificGroupRequest = new WebServiceRequest(4, "specific", page);
    	getSpiceManager().execute(getSpecificGroupRequest, "getSpecificGroupRequest", DurationInMillis.ALWAYS_EXPIRED, new GetMoreGroupRequestListener());

	}
	
	private class GetMoreGroupRequestListener implements RequestListener<WebServiceResponse>{

		private ArrayList<GroupBean> groupTempBeanList;

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			 if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isMyGroupsDataInCache()) {
				 return;
			 }else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isMyGroupsDataInCache()) {
				 return;
			 }
		}

		@Override
		public void onRequestSuccess(WebServiceResponse groupResponse) {
			mProgressBar.setVisibility(View.GONE);
			if (groupResponse != null) {
				
				MyApplication.getDonorSingleton().setMyGroupsDataInCache(true);
				if (!groupResponse.isError()) {
					
					if (groupResponse.getGroups().size() > 0) {
						
						groupTempBeanList = new ArrayList<GroupBean>();
						groupTempBeanList.addAll(groupResponse.getGroups());
						
						for (GroupBean groupBean : groupTempBeanList) {
							groupBeanList.addLast(groupBean);
						}
						
						setPages(groupResponse.getPages());
						
						((GroupAdapter)((HeaderViewListAdapter)mMyGroupListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();

						// Call onLoadMoreComplete when the LoadMore task, has finished
						mMyGroupListView.onLoadMoreComplete();
					}
					
				}else{
					appMsg = AppMsg.makeText(mContext, groupResponse.getMessage(), AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			}else{
				appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
		}
	}
	
	private void callRefresh(int page){
		
		getSpecificGroupRequest = new WebServiceRequest(4, "specific", page);
    	getSpiceManager().execute(getSpecificGroupRequest, "getSpecificGroupRequest", DurationInMillis.ONE_MINUTE, new GetRefreshGroupRequestListener());

	}
	
	private class GetRefreshGroupRequestListener implements RequestListener<WebServiceResponse>{

		private ArrayList<GroupBean> groupTempBeanList;

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			 if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isMyGroupsDataInCache()) {
				 return;
			 }else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isMyGroupsDataInCache()) {
				 return;
			 }
		}

		@Override
		public void onRequestSuccess(WebServiceResponse groupResponse) {
			mProgressBar.setVisibility(View.GONE);
			if (groupResponse != null) {
				
				MyApplication.getDonorSingleton().setMyGroupsDataInCache(true);
				if (!groupResponse.isError()) {
					
					if (groupResponse.getGroups().size() > 0) {
						
						groupTempBeanList = new ArrayList<GroupBean>();
						groupTempBeanList.addAll(groupResponse.getGroups());
						
						for (GroupBean groupBean : groupTempBeanList) {
							groupBeanList.addFirst(groupBean);
						}
						
						setPages(groupResponse.getPages());
						
						((GroupAdapter)((HeaderViewListAdapter)mMyGroupListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();

						// Call onLoadMoreComplete when the LoadMore task, has finished
						mMyGroupListView.onRefreshComplete();
						
					}
					
				}else{
					appMsg = AppMsg.makeText(mContext, groupResponse.getMessage(), AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			}else{
				appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
		}
	}
	
	private boolean checkValidation() {
        boolean ret = true;
       
        if (!Validation.isNull(mEditTextGroupName, true)) ret = false;  
        return ret;
    }
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	@Override
	public void createGroup() {
		showAddGroupAlertDialog();
	}
	
    @Override
    public void onResume() {
    	super.onResume();
    }
    
    @Override
	public void onPause() {
        super.onPause();
    }
    
    @Override
    public void onStop() {
    	super.onStop();
    }
	
    @Override
    public void onDestroyView() {
    	super.onDestroyView();
    	if (appMsg != null) {
			appMsg.cancelAll(mContext);
			appMsg = null;
		}
    }

	public GroupBean getGroupBean() {
		return groupBean;
	}

	public void setGroupBean(GroupBean groupBean) {
		this.groupBean = groupBean;
	}
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	
	
}
