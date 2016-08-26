package com.iamdonor.activity;

import java.io.Serializable;
import java.util.List;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

import com.actionbarsherlock.app.ActionBar;
import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.view.Menu;
import com.actionbarsherlock.view.MenuInflater;
import com.actionbarsherlock.view.MenuItem;
import com.actionbarsherlock.widget.SearchView;
import com.actionbarsherlock.widget.SearchView.OnCloseListener;
import com.actionbarsherlock.widget.SearchView.OnQueryTextListener;
import com.iamadonor.splash.R;
import com.iamdonor.adapter.CityAdapter;
import com.iamdonor.adapter.CountryAdapter;
import com.iamdonor.fragment.SearchDonorFragment;
import com.iamdonor.model.City;
import com.iamdonor.model.Country;
import com.ormlite.utils.DatabaseHelper;
import com.ormlite.utils.DonorRepository;
import com.robo.loading.MyApplication;

public class ChoosePlaceActivity extends SherlockActivity implements OnQueryTextListener, OnCloseListener, OnItemClickListener {
	
	private Context mContext;
	private ListView placeListView;
	private DonorRepository donorRepository;
	private SearchView searchView;
	private ProgressDialog pDialog;
	private CountryAdapter countryAdpater;
	private List<Country> countryList;
	private List<City> cityList;
	private Resources mResource;
	private ActionBar mActionBar;
	private Country countryBean;
	private Bundle bundle;
	private City city;
	private Country country;
	private Intent intent;
	private CityAdapter cityAdapter;
	private static final String COUNTRY = "country";
	private static final String CITY = "city";
	private boolean isFragment;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activty_place_layout);
		mContext = ChoosePlaceActivity.this;
		mResource = getResources();
		mActionBar = getSupportActionBar(); 
		donorRepository = new DonorRepository(mContext);
		placeListView = (ListView) findViewById(R.id.listView_Place);
		placeListView.setOnItemClickListener(this);
		
		bundle = getIntent().getExtras();
		if (bundle != null) {
			countryBean = (Country) bundle.getSerializable(COUNTRY);
			if (countryBean != null) {
				new FetchCity().execute(String.valueOf(countryBean.getCountryId()));
			}
		}else {
			new FetchCountry().execute();
		}
	}
	
	

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		MenuInflater inflater = getSupportMenuInflater();
		inflater.inflate(R.menu.place_serach_menu, menu);
		searchView = (SearchView) menu.findItem(R.id.searchPlace).getActionView();
    	searchView.setQueryHint("Search");
 	    SearchManager searchManager = (SearchManager)this.getSystemService(Context.SEARCH_SERVICE);
 		searchView.setSearchableInfo(searchManager.getSearchableInfo(this.getComponentName()));
 		searchView.setIconifiedByDefault(false);
 		
		return true;
	}
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == R.id.searchPlace) {
			searchView.setOnQueryTextListener(this);
			searchView.setOnCloseListener(this);
			return true;
		}
		return super.onOptionsItemSelected(item);
	}
	
	private class FetchCountry extends AsyncTask<Void, Void, List<Country>>{
		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Loading");
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(false);
			pDialog.show();
		}

		@Override
		protected List<Country> doInBackground(Void... params) {
			setCountryList(donorRepository.getCountryList());
			return getCountryList();
		}

		@Override
		protected void onPostExecute(List<Country> result) {
			super.onPostExecute(result);
			
			pDialog.dismiss();
			
			CountryAdapter mCountryAdapter = (CountryAdapter) placeListView.getAdapter();
			if (mCountryAdapter != null) {
				mCountryAdapter.clear();
			}
			
			if (result.size() > 0) {
				countryAdpater = new CountryAdapter(mContext, result);
				countryAdpater.notifyDataSetChanged();
				placeListView.setAdapter(countryAdpater);
				placeListView.invalidateViews();
			}else{
				placeListView.setAdapter(null);
				placeListView.invalidateViews();
			}
		}
	}
	
	private class FetchCity extends AsyncTask<String, Void, List<City>>{
		

		@Override
		protected void onPreExecute() {
			// TODO Auto-generated method stub
			super.onPreExecute();
			pDialog = new ProgressDialog(mContext);
			pDialog.setMessage("Loading");
			pDialog.setCancelable(false);
			pDialog.setIndeterminate(false);
			pDialog.show();
		}

		@Override
		protected List<City> doInBackground(String... params) {
			setCityList(donorRepository.getCityList(Integer.parseInt(params[0])));
			return getCityList();
		}

		@Override
		protected void onPostExecute(List<City> result) {
			super.onPostExecute(result);
			
			pDialog.dismiss();
			
			CityAdapter mCityAdapter = (CityAdapter) placeListView.getAdapter();
			if (mCityAdapter != null) {
				mCityAdapter.clear();
			}
			
			if (result.size() > 0) {
				cityAdapter = new CityAdapter(mContext, result);
				cityAdapter.notifyDataSetChanged();
				placeListView.setAdapter(cityAdapter);
				placeListView.invalidateViews();
			}else{
				placeListView.setAdapter(null);
				placeListView.invalidateViews();
			}
		}
		
	}
	
	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position,
			long id) {
		
			if (bundle != null) {
				city = (City) parent.getItemAtPosition(position);
				callBackIntent("city", city);
			}else{
				country = (Country) parent.getItemAtPosition(position);
				callBackIntent("country", country);
			}
		
		
	}
	
	public void callBackIntent(String value,Object valueOf){
		intent = new Intent();
		Bundle bundle = new Bundle();
		bundle.putString("identifier", value);
		bundle.putSerializable(value,(Serializable) valueOf);
		intent.putExtras(bundle);
        setResult(2,intent);  
        finish();
	}

	@Override
	public boolean onClose() {
		
		if (countryAdpater != null) {
			countryAdpater.filter("");
		}
		if(cityAdapter != null){
			cityAdapter.filter("");
		}
		
		return false;
	}


	@Override
	public boolean onQueryTextSubmit(String query) {
		if (countryAdpater != null) {
			if (countryList.size() > 0) {
				countryAdpater.filter(query);
			}else{
				countryAdpater.filter("");
			}
		}
		
		if (cityAdapter != null) {
			if (cityList.size() > 0) {
				cityAdapter.filter(query);
			}else{
				cityAdapter.filter("");
			}
		}
		return false;
	}


	@Override
	public boolean onQueryTextChange(String newText) {
		if (countryAdpater != null) {
			if (countryList.size() > 0) {
				countryAdpater.filter(newText);
			}else{
				countryAdpater.filter("");
			}
			
		}
		

		if (cityAdapter != null) {
			if (cityList.size() > 0) {
				cityAdapter.filter(newText);
			}else{
				cityAdapter.filter("");
			}
		}
		return false;
	}


	public List<Country> getCountryList() {
		return countryList;
	}


	public void setCountryList(List<Country> countryList) {
		this.countryList = countryList;
	}


	public List<City> getCityList() {
		return cityList;
	}


	public void setCityList(List<City> cityList) {
		this.cityList = cityList;
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (donorRepository != null) {
			donorRepository = null;
		}
		/*if (getCountryList().size() > 0) {
			getCountryList().clear();
			countryList = null;
		}
		if (getCityList().size() > 0) {
			getCityList().clear();
			cityList = null;
		}*/
		if (country != null) {
			country = null;
		}
		if (countryBean != null) {
			countryBean = null;
		}
		if (city != null) {
			city = null;
		}
	}


	public boolean isFragment() {
		return isFragment;
	}


	public void setFragment(boolean isFragment) {
		this.isFragment = isFragment;
	}
	
	
	
	
}
