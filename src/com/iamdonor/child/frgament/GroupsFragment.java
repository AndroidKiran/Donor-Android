package com.iamdonor.child.frgament;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.devspark.appmsg.AppMsg;
import com.iamadonar.utils.ConnectionDetector;
import com.iamadonor.splash.R;
import com.iamdonor.activity.DonorListActivity;
import com.iamdonor.adapter.EventDataAdapter;
import com.iamdonor.adapter.GroupAdapter;
import com.iamdonor.model.GroupBean;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.robo.loading.MyApplication;
import com.robo.models.WebServiceResponse;
import com.robo.request.WebServiceRequest;
import com.robo.utils.AbstractLoadingRequestListener;

public class GroupsFragment extends BaseAbstractFragment implements OnItemClickListener{
	
	private View mGroupView;
	private SherlockFragmentActivity mContext;
	private Resources mResources;
	private Intent intent;
	private Dialog progressDialog;
	private GroupAdapter mGroupAdapter;
	private WebServiceRequest getGenericGroupRequest;
	private Bundle bundle;
	private GroupBean groupBean;
	private ConnectionDetector connectionDetector;

	private LinkedList<GroupBean> groupBeanList;
	private AppMsg appMsg;
	private ProgressBar mProgressBar;
	private PullAndLoadListView mGroupListView;
	private int pages;
	private int pageInc;
	
	public static GroupsFragment newInstance() {
		return new GroupsFragment();
	}
	
	     
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getSherlockActivity();
		mResources = getResources();
		getGenericGroupRequest = new WebServiceRequest(4, "generic",1);
		connectionDetector = new ConnectionDetector(mContext);
		pageInc = 1;
		
	}
	

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mGroupView = inflater.inflate(R.layout.fragment_groups,container, false);
		mProgressBar = (ProgressBar)mGroupView.findViewById(R.id.groupProgressBarIndicator);
		mProgressBar.setVisibility(View.GONE);
		setHasOptionsMenu(false);
		mGroupListView = (PullAndLoadListView) mGroupView.findViewById(R.id.listView_Groups);
		mGroupListView.setOnItemClickListener(this);
		
		mGroupListView.setOnRefreshListener(new OnRefreshListener() {

			public void onRefresh() {
				callRefresh(1);
			}
		});

		
			
		mGroupListView.setOnLoadMoreListener(new OnLoadMoreListener() {
        	public void onLoadMore() {
        		pageInc+=1;
        		if (pageInc <= getPages()) {
        			callLoadMore(pageInc);
				}
        	}
		});
		 
		return mGroupView;
	}
	
	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		
		
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
		getSpiceManager().getFromCache(WebServiceResponse.class, "getGenericGroupRequest", DurationInMillis.ALWAYS_RETURNED, new GetGroupRequestListener());
    	getSpiceManager().execute(getGenericGroupRequest, "getGenericGroupRequest", DurationInMillis.ONE_MINUTE, new GetGroupRequestListener());
        
    }  
	
	private class GetGroupRequestListener implements RequestListener<WebServiceResponse>{

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isGroupsDataInCache()) {
				 return;
			 }else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isGroupsDataInCache()) {
				 return;
			 }
		}

		@Override
		public void onRequestSuccess(WebServiceResponse groupResponse) {
			mProgressBar.setVisibility(View.GONE);
			if (groupResponse != null) {
				MyApplication.getDonorSingleton().setGroupsDataInCache(true);
				if (!groupResponse.isError()) {
					
					if (groupResponse.getGroups().size() > 0) {
						
						groupBeanList = new LinkedList<GroupBean>();
						groupBeanList.addAll(groupResponse.getGroups());
						setPages(groupResponse.getPages());
						mGroupAdapter = new GroupAdapter(mContext, groupBeanList);
						mGroupAdapter.notifyDataSetChanged();
						mGroupListView.setAdapter(mGroupAdapter);
						mGroupListView.invalidateViews();
						
					}
				}else{
					appMsg = AppMsg.makeText(mContext,groupResponse.getMessage() , AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			}else{
				appMsg = AppMsg.makeText(mContext,"Network Error" , AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
		}
	}
	
	private void callLoadMore(int page){
		getGenericGroupRequest = new WebServiceRequest(4, "generic", page);
    	getSpiceManager().execute(getGenericGroupRequest, "getGenericGroupRequest", DurationInMillis.ALWAYS_EXPIRED, new GetMoreGroupRequestListener());
	}
	
	private class GetMoreGroupRequestListener implements RequestListener<WebServiceResponse>{

		private ArrayList<GroupBean> groupTempBeanList;

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isGroupsDataInCache()) {
				 return;
			 }else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isGroupsDataInCache()) {
				 return;
			 }
		}

		@Override
		public void onRequestSuccess(WebServiceResponse groupResponse) {
			mProgressBar.setVisibility(View.GONE);
			if (groupResponse != null) {
				MyApplication.getDonorSingleton().setGroupsDataInCache(true);
				if (!groupResponse.isError()) {
					
					if (groupResponse.getGroups().size() > 0) {
						groupTempBeanList = new ArrayList<GroupBean>();
						groupTempBeanList.addAll(groupResponse.getGroups());
						
						for (GroupBean groupBean : groupTempBeanList) {
							groupBeanList.addLast(groupBean);
						}
						setPages(groupResponse.getPages());
						
						((GroupAdapter)((HeaderViewListAdapter)mGroupListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();

						// Call onLoadMoreComplete when the LoadMore task, has finished
						mGroupListView.onLoadMoreComplete();
						
						System.out.println("pageeeeeeeeeeeeeeeeeee "+pageInc);
						
					}
				}else{
					appMsg = AppMsg.makeText(mContext,groupResponse.getMessage() , AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			}else{
				appMsg = AppMsg.makeText(mContext,"Network Error" , AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
		}
	}
	
	private void callRefresh(int page){
		getGenericGroupRequest = new WebServiceRequest(4, "generic", page);
    	getSpiceManager().execute(getGenericGroupRequest, "getGenericGroupRequest", DurationInMillis.ONE_MINUTE, new GetRefreshGroupRequestListener());
	}
	
	private class GetRefreshGroupRequestListener implements RequestListener<WebServiceResponse>{

		private ArrayList<GroupBean> groupTempBeanList;

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isGroupsDataInCache()) {
				 return;
			 }else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isGroupsDataInCache()) {
				 return;
			 }
		}

		@Override
		public void onRequestSuccess(WebServiceResponse groupResponse) {
			mProgressBar.setVisibility(View.GONE);
			if (groupResponse != null) {
				MyApplication.getDonorSingleton().setGroupsDataInCache(true);
				if (!groupResponse.isError()) {
					
					if (groupResponse.getGroups().size() > 0) {
						groupTempBeanList = new ArrayList<GroupBean>();
						groupTempBeanList.addAll(groupResponse.getGroups());
						
						for (GroupBean groupBean : groupTempBeanList) {
							groupBeanList.addFirst(groupBean);
						}
						setPages(groupResponse.getPages());
						
						((GroupAdapter)((HeaderViewListAdapter)mGroupListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();

						// Call onLoadMoreComplete when the LoadMore task, has finished
						mGroupListView.onRefreshComplete();
						
					}
				}else{
					appMsg = AppMsg.makeText(mContext,groupResponse.getMessage() , AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			}else{
				appMsg = AppMsg.makeText(mContext,"Network Error" , AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
		}
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,long id) {
		setGroupBean((GroupBean) parent.getItemAtPosition(position));
		intent = new Intent(mContext, DonorListActivity.class);
		bundle = new Bundle();
		bundle.putString("groupName", getGroupBean().getGroupName());
		bundle.putInt("groupId", getGroupBean().getGroupId());
		bundle.putBoolean("groups", true);
		intent.putExtras(bundle);
		startActivityForResult(intent, 1);
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
	}
	
	/*@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		inflater.inflate(R.menu.add_group_menu, menu);
		super.onCreateOptionsMenu(menu, inflater);
	}*/
	
	

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
