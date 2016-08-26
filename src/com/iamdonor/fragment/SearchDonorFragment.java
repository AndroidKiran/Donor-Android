package com.iamdonor.fragment;

import java.lang.reflect.Field;
import java.net.SocketTimeoutException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.HeaderViewListAdapter;
import android.widget.Spinner;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.costum.android.widget.LoadMoreListView;
import com.costum.android.widget.LoadMoreListView.OnLoadMoreListener;
import com.devspark.appmsg.AppMsg;
import com.iamadonar.utils.ConnectionDetector;
import com.iamadonor.splash.R;
import com.iamdonor.activity.ChoosePlaceActivity;
import com.iamdonor.adapter.DonorAdapter;
import com.iamdonor.adapter.EventDataAdapter;
import com.iamdonor.child.frgament.BaseAbstractFragment;
import com.iamdonor.model.City;
import com.iamdonor.model.Country;
import com.iamdonor.model.SearchBean;
import com.iamdonor.model.UserBean;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.robo.loading.MyApplication;
import com.robo.models.WebServiceResponse;
import com.robo.request.WebServiceRequest;
import com.robo.utils.AbstractLoadingRequestListener;

public class SearchDonorFragment extends BaseAbstractFragment implements OnClickListener, OnItemSelectedListener{
	private View mSearchDonorView;
	private Button mSearchDonorButton;
	private SherlockFragmentActivity mContext;
	private Resources mResource;
	private EditText mCountryEditText;
	private EditText mCityEditText;
	private Intent intent;
	private Bundle bundle;
	private Country country;
	private City city;
	private Spinner mBloodGroupSpinner;
	private String mBloodGroup;
	private LinkedList<UserBean> searchList;
	private WebServiceRequest getSearchRequest;
	private ConnectionDetector connectionDetector;
	
	private static final int ACONSTANT = 2;
	private static final String COUNTRY = "country";
	private static final String CITY = "city";
	private static final String IDENTIFIER = "identifier";
	
	private int pages;
	private int pageInc;
	private AppMsg appMsg;
	private LoadMoreListView mDonorListView;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		pageInc = 1;
		mContext = getSherlockActivity();
	    mResource = getResources();
	    connectionDetector = new ConnectionDetector(mContext);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	      Bundle savedInstanceState) {
		mSearchDonorView = inflater.inflate(R.layout.fragment_search, container, false);
	    
	    mCountryEditText = (EditText) mSearchDonorView.findViewById(R.id.editText_country_search);
	    mCountryEditText.setOnClickListener(this);
	    mCityEditText = (EditText) mSearchDonorView.findViewById(R.id.editText_city_search);
	    mCityEditText.setOnClickListener(this);
	    mBloodGroupSpinner = (Spinner)mSearchDonorView.findViewById(R.id.spinner_bloodGroupSearch);
	    mBloodGroupSpinner.setOnItemSelectedListener(this);
		mSearchDonorButton = (Button)mSearchDonorView.findViewById(R.id.button_Search);
		mSearchDonorButton.setOnClickListener(this);
		mDonorListView = (LoadMoreListView)mSearchDonorView.findViewById(R.id.listView_BloodDonors);
		
		mDonorListView.setOnLoadMoreListener(new OnLoadMoreListener() {
        	public void onLoadMore() {
        		pageInc+=1;
        		if (pageInc <= getPages()) {
        			callLoadMore(pageInc);
				}
        	}
		});
	   
	    return mSearchDonorView;
	  }

	  @Override
	  public void onDetach() {
	    super.onDetach();
	    try {
	      Field childFragmentManager = Fragment.class
	          .getDeclaredField("mChildFragmentManager");
	      childFragmentManager.setAccessible(true);
	      childFragmentManager.set(this, null);
	    } catch (NoSuchFieldException e) {
	      throw new RuntimeException(e);
	    } catch (IllegalAccessException e) {
	      throw new RuntimeException(e);
	    }
	  }

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_Search:
			getSearchRequest = new WebServiceRequest(3,createSearchBean(),1);
			if (getSearchRequest != null && connectionDetector.isConnectingToInternet()) {
				getSpiceManager().execute(getSearchRequest, new GetSearchRequestListener());
			}else{
				appMsg = AppMsg.makeText(mContext, "No Network", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			break;
		case R.id.editText_country_search:
			if (mCityEditText.getText().toString().trim().length() > 0) {
				mCityEditText.getText().clear();
			}
			intent = new Intent(mContext, ChoosePlaceActivity.class);
			startActivityForResult(intent, ACONSTANT);
			
			break;
		case R.id.editText_city_search:
			if (mCountryEditText.getText().toString().length() > 0) {
				intent = new Intent(mContext, ChoosePlaceActivity.class);
				bundle = new Bundle();
				bundle.putSerializable(COUNTRY, getCountry());
				intent.putExtras(bundle);
				startActivityForResult(intent, ACONSTANT);
			}else{
				appMsg = AppMsg.makeText(mContext, "Please Chhose the Country", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			break;

		default:
			break;
		}
		
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
		 super.onActivityResult(requestCode, resultCode, intent);
			
		 if (requestCode == ACONSTANT) {
			 Bundle bundle = intent.getExtras();
			 if (bundle != null) {
				if (bundle.getString(IDENTIFIER).equals(COUNTRY)) {
					setCountry((Country) bundle.getSerializable(COUNTRY));
					mCountryEditText.setText(getCountry().getCountryName());
				}else{
					setCity((City)bundle.getSerializable(CITY));
					mCityEditText.setText(getCity().getCityName());
				}
			}
		 }
	 }
	
	private SearchBean createSearchBean(){
		String country = mCountryEditText.getText().toString();
		String city = mCityEditText.getText().toString();
		SearchBean searchBean = new SearchBean(country, city, getmBloodGroup());
		return searchBean;
		
	}
	
	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		String bloodGroup = (String) mBloodGroupSpinner.getSelectedItem();
		if (bloodGroup.equalsIgnoreCase("O+")) {
			setmBloodGroup("O%2B");
		}else if (bloodGroup.equalsIgnoreCase("O-")) {
			setmBloodGroup("O%2D");
		}else if (bloodGroup.equalsIgnoreCase("A+")) {
			setmBloodGroup("A%2B");
		}else if (bloodGroup.equalsIgnoreCase("A-")) {
			setmBloodGroup("A%2D");
		}else if (bloodGroup.equalsIgnoreCase("B+")) {
			setmBloodGroup("B%2B");
		}else if (bloodGroup.equalsIgnoreCase("B-")) {
			setmBloodGroup("B%2D");
		}else if (bloodGroup.equalsIgnoreCase("AB+")) {
			setmBloodGroup("AB%2B");
		}else if (bloodGroup.equalsIgnoreCase("AB-")) {
			setmBloodGroup("AB%2D");
		}
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}
	
	private class GetSearchRequestListener extends AbstractLoadingRequestListener<WebServiceResponse>{
		
		private Dialog progressDialog;
		private DonorAdapter mDonorAdpter;

		
		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isSearchInCache()) {
				return;
			} else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isSearchInCache()) {
				return;
			}
		}

		@Override
		public void onRequestSuccess(WebServiceResponse searchResponse) {
			if (searchResponse != null) {
				if (!searchResponse.isError()) {
					
					MyApplication.getDonorSingleton().setSearchInCache(true);
					
					searchList = new LinkedList<UserBean>();
					searchList.addAll(searchResponse.getDonors());
					
					if (searchList.size() != 0) {
						setPages(searchResponse.getPages());
						mDonorAdpter = new DonorAdapter(mContext, searchList);
						mDonorAdpter.notifyDataSetChanged();
						mDonorListView.setAdapter(mDonorAdpter);
						mDonorListView.invalidateViews();
					}
					
				}else{
					appMsg = AppMsg.makeText(mContext, searchResponse.getMessage(), AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			} else {
				appMsg = AppMsg.makeText(mContext, "network error", AppMsg.STYLE_ALERT);
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
		getSearchRequest = new WebServiceRequest(3,createSearchBean(),page);
		if (getSearchRequest != null && connectionDetector.isConnectingToInternet()) {
			getSpiceManager().execute(getSearchRequest, new GetMoreSearchRequestListener());
		}else{
			appMsg = AppMsg.makeText(mContext, "No Network", AppMsg.STYLE_ALERT);
			appMsg.show();
		}
	}
	
	private class GetMoreSearchRequestListener implements RequestListener<WebServiceResponse>{
		
		private ArrayList<UserBean> searchTempList;

		
		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isSearchInCache()) {
				return;
			} else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isSearchInCache()) {
				return;
			}
		}

		@Override
		public void onRequestSuccess(WebServiceResponse searchResponse) {
			if (searchResponse != null) {
				if (!searchResponse.isError()) {
					
					MyApplication.getDonorSingleton().setSearchInCache(true);
					
					searchTempList = new ArrayList<UserBean>();
					searchTempList.addAll(searchResponse.getDonors());
					
					for (UserBean userBean : searchTempList) {
						searchList.addLast(userBean);
					}
					
					setPages(searchResponse.getPages());
					
					((DonorAdapter)((HeaderViewListAdapter)mDonorListView.getAdapter()).getWrappedAdapter()).notifyDataSetChanged();

					// Call onLoadMoreComplete when the LoadMore task, has finished
					mDonorListView.onLoadMoreComplete();
					
				}else{
					appMsg = AppMsg.makeText(mContext, searchResponse.getMessage(), AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			} else {
				appMsg = AppMsg.makeText(mContext, "network error", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
		}
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (appMsg != null) {
			appMsg.cancelAll(mContext);
			appMsg = null;
		}
	}


	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	public String getmBloodGroup() {
		return mBloodGroup;
	}

	public void setmBloodGroup(String mBloodGroup) {
		this.mBloodGroup = mBloodGroup;
	}

	public int getPages() {
		return pages;
	}

	public void setPages(int pages) {
		this.pages = pages;
	}
	
	

}
