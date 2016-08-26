package com.iamdonor.child.frgament;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ProgressBar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.devspark.appmsg.AppMsg;
import com.iamadonar.utils.ConnectionDetector;
import com.iamadonor.splash.R;
import com.iamdonor.adapter.EmergencyAdapter;
import com.iamdonor.model.EmergencyBean;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.robo.loading.MyApplication;
import com.robo.models.WebServiceResponse;
import com.robo.request.WebServiceRequest;

public class EmergencyFragment extends BaseAbstractFragment implements OnRefreshListener, OnLoadMoreListener{
	
	private View mEmergencyView;
	private SherlockFragmentActivity mContext;
	private static WebServiceRequest getEmergencyRequest;
	private ConnectionDetector connectionDetector;
	
	private Dialog progressDialog;
	private static GetEmergencyRequestListener listener;
	private EmergencyAdapter emergencyAdpater;
	private AppMsg appMsg;
	private ProgressBar mProgressBar;
	
	private EmergencyAdapter mEmergencyAdapter;
	private static LinkedList<EmergencyBean> emergencyList;
	private int pages;
	private int pageInc;
	private PullAndLoadListView mEmergencyListView;
	private int pageReset;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getSherlockActivity();
		connectionDetector = new ConnectionDetector(mContext);
		emergencyList = new LinkedList<EmergencyBean>();
		
	}
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mEmergencyView = inflater.inflate(R.layout.fragment_events, container,false);
		mEmergencyListView = (PullAndLoadListView)mEmergencyView.findViewById(R.id.eventListView);
		mProgressBar = (ProgressBar)mEmergencyView.findViewById(R.id.progressBarIndicator);
		mProgressBar.setVisibility(View.GONE);
		mEmergencyListView.setOnRefreshListener(this);
		mEmergencyListView.setOnLoadMoreListener(null);

		return mEmergencyView;
	}
	
	@Override
	public void onStart() {
		super.onStart();
		mProgressBar.setVisibility(View.VISIBLE);
		getEmergencyRequest = new WebServiceRequest(6, 1);
		pageInc = 1;
		if (getEmergencyRequest != null && connectionDetector.isConnectingToInternet()) {
			getSpiceManager().execute(getEmergencyRequest,"getEmergencyRequest", DurationInMillis.ONE_MINUTE, new GetEmergencyRequestListener());
		}else{
			appMsg = AppMsg.makeText(mContext, "No Network", AppMsg.STYLE_ALERT);
			appMsg.show();
		}
		//getSpiceManager().getFromCache(WebServiceResponse.class, "getEmergencyRequest", DurationInMillis.ALWAYS_RETURNED, new GetEmergencyRequestListener());
	
	}
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setMenuVisibility(true);
	}
	
	
	private class GetEmergencyRequestListener implements RequestListener<WebServiceResponse>{

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isEmergencyDataInCache()) {
				
				return;
			}else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isEmergencyDataInCache()) {
				
				return;
			}
		}
		
		@Override
		public void onRequestSuccess(WebServiceResponse emergencyResponse) {
				mProgressBar.setVisibility(View.GONE);
				if (emergencyResponse != null) {
					if (!emergencyResponse.isError()) {
						
						if (emergencyResponse.getPages() > 0) {
							MyApplication.getDonorSingleton().setEmergencyDataInCache(true);
							
							if (emergencyResponse.getNeeds().size() > 0) {
								
								emergencyList.addAll(emergencyResponse.getNeeds());
								
								setPages(emergencyResponse.getPages());
								setPageReset(emergencyResponse.getPages());
								
								mEmergencyAdapter = new EmergencyAdapter(mContext, emergencyList);
								mEmergencyAdapter.notifyDataSetChanged();
								mEmergencyListView.setAdapter(mEmergencyAdapter);
								mEmergencyListView.invalidateViews();
								
							}
						}else{
							appMsg = AppMsg.makeText(mContext,"No Data Avaiable Currently", AppMsg.STYLE_ALERT);
							appMsg.show();
						}
						
						
					}else{
						appMsg = AppMsg.makeText(mContext,emergencyResponse.getMessage(), AppMsg.STYLE_ALERT);
						appMsg.show();
					}
				} else {
					appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
					appMsg.show();
				}
		}
	}
	
	private void callLoadMore(int page){
		getEmergencyRequest = new WebServiceRequest(6, page);
		if (getEmergencyRequest != null && connectionDetector.isConnectingToInternet()) {
			getSpiceManager().execute(getEmergencyRequest,new GetMoreEmergencyRequestListener());
		}else{
			appMsg = AppMsg.makeText(mContext, "No Network", AppMsg.STYLE_ALERT);
			appMsg.show();
		}
		
	}
	
	private class GetMoreEmergencyRequestListener implements RequestListener<WebServiceResponse>{

		private ArrayList<EmergencyBean> emergencyTempList;

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isEmergencyDataInCache()) {
				
				return;
			}else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isEmergencyDataInCache()) {
				
				return;
			}
		}
		
		@Override
		public void onRequestSuccess(WebServiceResponse emergencyResponse) {
				mProgressBar.setVisibility(View.GONE);
				if (emergencyResponse != null) {
					if (!emergencyResponse.isError()) {
						MyApplication.getDonorSingleton().setEmergencyDataInCache(true);

						if (emergencyResponse.getNeeds().size() > 0) {
							
							emergencyTempList = new ArrayList<EmergencyBean>();
							emergencyTempList.addAll(emergencyResponse.getNeeds());
							
							for (EmergencyBean emergencyBean : emergencyTempList) {
								if (!emergencyList.contains(emergencyBean)) {
									
									emergencyList.addLast(emergencyBean);
								}
							}
							setPages(emergencyResponse.getPages());
							// We need notify the adapter that the data have been changed
							((EmergencyAdapter)((HeaderViewListAdapter)mEmergencyListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();

							// Call onLoadMoreComplete when the LoadMore task, has finished
							mEmergencyListView.onLoadMoreComplete();
						}
						
					}else{
						appMsg = AppMsg.makeText(mContext,emergencyResponse.getMessage(), AppMsg.STYLE_ALERT);
						appMsg.show();
					}
				} else {
					appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
					appMsg.show();
				}
		}
	}
	
	private void callRefresh(int page){
		getEmergencyRequest = new WebServiceRequest(6, page);
		if (getEmergencyRequest != null && connectionDetector.isConnectingToInternet()) {
			getSpiceManager().execute(getEmergencyRequest, new GetRefreshEmergencyRequestListener());
		}else{
			appMsg = AppMsg.makeText(mContext, "No Network", AppMsg.STYLE_ALERT);
			appMsg.show();
		}
		
	}
	
	private class GetRefreshEmergencyRequestListener implements RequestListener<WebServiceResponse>{

		private ArrayList<EmergencyBean> emergencyTempList;

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isEmergencyDataInCache()) {
				
				return;
			}else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isEmergencyDataInCache()) {
				
				return;
			}
		}
		
		@Override
		public void onRequestSuccess(WebServiceResponse emergencyResponse) {
				mProgressBar.setVisibility(View.GONE);
				if (emergencyResponse != null) {
					if (!emergencyResponse.isError()) {
						
						if (emergencyResponse.getNeeds().size() > 0) {
							
							emergencyTempList = new ArrayList<EmergencyBean>();
							emergencyTempList.addAll(emergencyResponse.getNeeds());
							
							for (EmergencyBean emergencyBean : emergencyTempList) {
								
								if (!emergencyList.contains(emergencyBean)) {
									emergencyList.addFirst(emergencyBean);
								}
							}
							setPages(emergencyResponse.getPages());
							// We need notify the adapter that the data have been changed
							((EmergencyAdapter)((HeaderViewListAdapter)mEmergencyListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();
								
							// Call onLoadMoreComplete when the LoadMore task, has finished
							((PullAndLoadListView)mEmergencyListView).onRefreshComplete();
						}
						
					}else{
						appMsg = AppMsg.makeText(mContext,emergencyResponse.getMessage(), AppMsg.STYLE_ALERT);
						appMsg.show();
					}
				} else {
					appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
					appMsg.show();
				}
		}
		
	}
	
	@Override
	public void onRefresh() {
		callRefresh(1);
	}
	
	@Override
	public void onLoadMore() {
		pageInc+=1;
		callLoadMore(pageInc);
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (appMsg != null) {
			appMsg.cancelAll(mContext);
			appMsg = null;
		}
		if (mEmergencyAdapter != null) {
			//((EmergencyAdapter)((HeaderViewListAdapter)mEmergencyListView.getAdapter()).getWrappedAdapter()).clear();
		}
	}
	
	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	public int getPageReset() {
		return pageReset;
	}

	public void setPageReset(int pageReset) {
		this.pageReset = pageReset;
	}
	
}
