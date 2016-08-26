package com.iamdonor.child.frgament;

import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.costum.android.widget.PullAndLoadListView;
import com.costum.android.widget.PullAndLoadListView.OnLoadMoreListener;
import com.costum.android.widget.PullToRefreshListView.OnRefreshListener;
import com.devspark.appmsg.AppMsg;
import com.iamadonar.utils.ConnectionDetector;
import com.iamadonor.splash.R;
import com.iamdonor.adapter.EmergencyAdapter;
import com.iamdonor.adapter.EventDataAdapter;
import com.iamdonor.model.EmergencyBean;
import com.iamdonor.model.EventBean;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.DurationInMillis;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.robo.loading.MyApplication;
import com.robo.models.WebServiceResponse;
import com.robo.models.UserResponse;
import com.robo.request.WebServiceRequest;

import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HeaderViewListAdapter;
import android.widget.ListView;
import android.widget.ProgressBar;

public class EventsFragment extends BaseAbstractFragment implements OnLoadMoreListener, OnRefreshListener {
	List<EventBean> eventBeanList1;
	private Resources mResource;
	private SherlockFragmentActivity mContext;

	private Bundle bundle;
	private String phoneNum;
	private String fragmentValue;
	private View mEventsView;
	private Dialog progressDialog;
	private EventDataAdapter mEventDataAdapter;
	private LinkedList<EventBean> eventList;
	private UserResponse userResponse;
	private WebServiceRequest getEventsRequest;
	private ConnectionDetector connectionDetector;
	
	private AppMsg appMsg;
	private ProgressBar mProgressBar;
	
	private int pages;
	private int pageInc;
	private PullAndLoadListView mEventListView;

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getSherlockActivity();
	    mResource = getResources();
	    getEventsRequest = new WebServiceRequest(1,1);
	    connectionDetector = new ConnectionDetector(mContext);
	    pageInc = 1;
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        mEventsView = inflater.inflate(R.layout.fragment_events, container, false);
        mProgressBar = (ProgressBar)mEventsView.findViewById(R.id.progressBarIndicator);
        mProgressBar.setVisibility(View.GONE);
        mEventListView = (PullAndLoadListView) mEventsView.findViewById(R.id.eventListView);
        mEventListView.setOnLoadMoreListener(this);
        mEventListView.setOnRefreshListener(this);
        return mEventsView;
    }
	
	@Override
	public void onStart() {
		super.onStart();
		mProgressBar.setVisibility(View.VISIBLE);
		getSpiceManager().getFromCache(WebServiceResponse.class, "getEventsRequest", DurationInMillis.ALWAYS_RETURNED, new GetEventRequestListner());
		getSpiceManager().execute(getEventsRequest, "getEventsRequest", DurationInMillis.ALWAYS_EXPIRED, new GetEventRequestListner());
		
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
	
	
	
	private class GetEventRequestListner implements RequestListener<WebServiceResponse>{


		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isEventDataInCache()) {
				return;
			 }else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isEventDataInCache()) {
				return;
			 }
		}
		
		
		
		@Override
		public void onRequestSuccess(WebServiceResponse mEventResponse) {
			mProgressBar.setVisibility(View.GONE);
			if (mEventResponse != null) {
				
				MyApplication.getDonorSingleton().setEventDataInCache(true);
				if (!mEventResponse.isError()) {
					if (mEventResponse.getPages() > 0) {
						if (mEventResponse.getEvents().size() != 0) {
							
							eventList = new LinkedList<EventBean>(mEventResponse.getEvents());
							
							setPages(mEventResponse.getPages());
							mEventDataAdapter = new EventDataAdapter(mContext, eventList);
							mEventDataAdapter.notifyDataSetChanged();
							mEventListView.setAdapter(mEventDataAdapter);
							mEventListView.invalidateViews();
							
						} 
					}else{
						appMsg = AppMsg.makeText(mContext, "No Data Avaiable Currently", AppMsg.STYLE_ALERT);
						appMsg.show();
					}
				}else{
					appMsg = AppMsg.makeText(mContext, mEventResponse.getMessage(), AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			}else{
				appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
		}
	}
	
	private void callLoadMore(int page){
		getEventsRequest = new WebServiceRequest(1,page);
		getSpiceManager().execute(getEventsRequest, "getEventsRequest", DurationInMillis.ALWAYS_EXPIRED, new GetMoreEventRequestListener());

	}
	
	private class GetMoreEventRequestListener implements RequestListener<WebServiceResponse>{

		private ArrayList<EventBean> eventTempList;

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isEventDataInCache()) {
				
				return;
			}else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isEventDataInCache()) {
				
				return;
			}
		}
		
		@Override
		public void onRequestSuccess(WebServiceResponse mEventResponse) {
				mProgressBar.setVisibility(View.GONE);
				if (mEventResponse != null) {
					MyApplication.getDonorSingleton().setEventDataInCache(true);
					if (!mEventResponse.isError()) {
						if (mEventResponse.getEvents().size() != 0) {
							
							eventTempList = new ArrayList<EventBean>();
							eventTempList.addAll(mEventResponse.getEvents());
							
							for (EventBean eventBean : eventTempList) {
								if (!eventList.contains(eventBean)) {
									eventList.addLast(eventBean);
								}
							}
							setPages(mEventResponse.getPages());
							
							((EventDataAdapter)((HeaderViewListAdapter)mEventListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();

							// Call onLoadMoreComplete when the LoadMore task, has finished
							mEventListView.onLoadMoreComplete();
							System.out.println("getpage "+getPages());
							System.out.println("pageInc "+pageInc);
							
						} 
						else{
							((EventDataAdapter)((HeaderViewListAdapter)mEventListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();

							// Call onLoadMoreComplete when the LoadMore task, has finished
							mEventListView.onLoadMoreComplete();
						}
						
					}else{
						appMsg = AppMsg.makeText(mContext, mEventResponse.getMessage(), AppMsg.STYLE_ALERT);
						appMsg.show();
					}
				}else{
					appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
					appMsg.show();
				}
		}
	}
	
	private void callRefresh(int page){
		getEventsRequest = new WebServiceRequest(1,page);
		getSpiceManager().execute(getEventsRequest, "getEventsRequest", DurationInMillis.ALWAYS_EXPIRED, new GetRefreshEventRequestListener());

	}
	
	private class GetRefreshEventRequestListener implements RequestListener<WebServiceResponse>{

		private ArrayList<EventBean> eventTempList;

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isEmergencyDataInCache()) {
				return;
			}else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isEmergencyDataInCache()) {
				return;
			}
		}
		
		@Override
		public void onRequestSuccess(WebServiceResponse mEventResponse) {
				mProgressBar.setVisibility(View.GONE);
				if (mEventResponse != null) {
					
					MyApplication.getDonorSingleton().setEventDataInCache(true);
					if (!mEventResponse.isError()) {
						if (mEventResponse.getEvents().size() != 0) {
							
							eventTempList = new ArrayList<EventBean>(mEventResponse.getEvents());
							for (EventBean eventBean : eventList) {
								System.out.println("eventList data ......"+eventBean.getEventId());
							}
							for (EventBean eventBean: eventTempList) {
								System.out.println("eventList data ......"+eventBean.getEventId());
								System.out.println("outside if.........."+eventList.contains(eventBean));
								if (!eventList.contains(eventBean)) {
									System.out.println("val............."+eventList.contains(eventBean));
									eventList.addFirst(eventBean);
								}
							}
							setPages(mEventResponse.getPages());
							
							((EventDataAdapter)((HeaderViewListAdapter)mEventListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();

							// Call onLoadMoreComplete when the LoadMore task, has finished
							((PullAndLoadListView)mEventListView).onRefreshComplete();
						} 
						
					}else{
						appMsg = AppMsg.makeText(mContext, mEventResponse.getMessage(), AppMsg.STYLE_ALERT);
						appMsg.show();
					}
				}else{
					appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			}
	}
	
	@Override
	public void onLoadMore() {
		pageInc+=1;
		callLoadMore(pageInc);
	}
	
	@Override
	public void onRefresh() {
		callRefresh(1);
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

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}

	
	
}
