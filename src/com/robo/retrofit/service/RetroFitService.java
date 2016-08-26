package com.robo.retrofit.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.http.converter.xml.SimpleXmlHttpMessageConverter;

import retrofit.RestAdapter;
import retrofit.RestAdapter.Builder;
import retrofit.client.OkClient;


import android.app.Application;
import android.content.Context;

import com.iamdonor.model.EmergencyBean;
import com.iamdonor.model.EventBean;
import com.iamdonor.model.GroupBean;
import com.iamdonor.model.UserBean;
import com.octo.android.robospice.networkstate.NetworkStateChecker;
import com.octo.android.robospice.persistence.CacheManager;
import com.octo.android.robospice.persistence.exception.CacheCreationException;
import com.octo.android.robospice.persistence.ormlite.InDatabaseObjectPersisterFactory;
import com.octo.android.robospice.persistence.ormlite.RoboSpiceDatabaseHelper;
import com.octo.android.robospice.retrofit.RetrofitGsonSpiceService;
import com.robo.models.WebServiceResponse;
import com.robo.models.UserResponse;
import com.robo.userinterface.UserService;
import com.robo.utils.MyErrorHandler;

public class RetroFitService extends RetrofitGsonSpiceService {
	private final static String BASE_URL = "http://10.0.2.2/donors/v1";
	private static final String DB_NAME = "IAmDonorResponse.db";
	private static final Integer DB_VERSION = 1;
	
	@Override
	public void onCreate() {
		super.onCreate();
		addRetrofitInterface(UserService.class);
	}
	
	@Override
	public CacheManager createCacheManager(Application application)
			throws CacheCreationException {
		CacheManager cacheMngr = new CacheManager();
		List<Class<?>> clsCollections = new ArrayList<Class<?>>();
		
		clsCollections.add(WebServiceResponse.class);
		clsCollections.add(EmergencyBean.class);
		clsCollections.add(EventBean.class);
		clsCollections.add(UserBean.class);
		clsCollections.add(GroupBean.class);
		
		// start cache
		RoboSpiceDatabaseHelper dbHlpr = new RoboSpiceDatabaseHelper(
				application, DB_NAME, DB_VERSION);

		InDatabaseObjectPersisterFactory inDatabaseObjectPersisterFactory = new InDatabaseObjectPersisterFactory(
				application, dbHlpr, clsCollections);

		cacheMngr.addPersister(inDatabaseObjectPersisterFactory);
		return cacheMngr;
	}
	
	@Override
	protected String getServerUrl() {
		return BASE_URL;
	}

	@Override
	protected Builder createRestAdapterBuilder() {
		
		RestAdapter.Builder restAdapter = super.createRestAdapterBuilder();
		restAdapter.setServer(getServerUrl())
		.setErrorHandler(new MyErrorHandler())
		.setClient(new OkClient())
		.setLogLevel(RestAdapter.LogLevel.FULL)
		.build();
		return restAdapter;
	}
	
	@Override
    protected NetworkStateChecker getNetworkStateChecker() {
        return new NetworkStateChecker() {

            @Override
            public boolean isNetworkAvailable( Context context ) {
                return true;
            }

            @Override
            public void checkPermissions( Context context ) {
            	
            }
        };
    }
	
	
	
	

}
