package com.ormlite.utils;


import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.util.Log;

import com.iamdonor.model.City;
import com.iamdonor.model.Country;
import com.iamdonor.model.DonorBean;
import com.j256.ormlite.android.apptools.OpenHelperManager;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.dao.ForeignCollection;

public class DonorRepository {
	private final static String LOG_TAG = "DemoRepository";
	
	private Dao<DonorBean, Integer> donorDao;
	private Dao<Country, Integer> countryDao;
	private Dao<City, Integer> cityDao;
	
	private DatabaseHelper mDatabaseHelper;
	private Context mContext;
	
	public DonorRepository(Context mContext) {
		this.mContext = mContext;
		this.donorDao = getDonorDao(getHelper());
		this.countryDao = getCountryDao(getHelper());
		this.cityDao = getCityDao(getHelper());
	}
	
	public DatabaseHelper getHelper() {
        if (mDatabaseHelper == null) {
            mDatabaseHelper = OpenHelperManager.getHelper(mContext, DatabaseHelper.class);
        }
        return mDatabaseHelper;
    }
	

	public List<DonorBean> getDonors() {
		try {
			return this.donorDao.queryForAll();
		}
		catch (final SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<DonorBean>();
	}

	public void saveOrUpdateDonor(final DonorBean donorBean) {
		try {
			
			this.donorDao.createOrUpdate(donorBean);
		}
		catch (final SQLException e) {
			e.printStackTrace();
			
		}
	}

	public void deleteDonor(final DonorBean donorBean ) {
		try {
			this.donorDao.delete(donorBean);
		}
		catch (final SQLException e) {
			e.printStackTrace();
		}
	}
	
	public List<Country> getCountryList() {
		try {
			return this.countryDao.queryForAll();
		}
		catch (final SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<Country>();
	}
	
	public List<City> getCityList(int value) {
		try {
			return this.cityDao.queryForEq("fk_country", value);
					//queryBuilder().selectColumns("city_id","city_name").where().eq("fk_country", value);
		}
		catch (final SQLException e) {
			e.printStackTrace();
		}
		return new ArrayList<City>();
	}

	private Dao<DonorBean, Integer> getDonorDao(final DatabaseHelper databaseHelper) {
		if (null == this.donorDao) {
			try {
				this.donorDao = databaseHelper.getDonorDao();
			}
			catch (final SQLException e) {
				Log.e(LOG_TAG, "Unable to load DAO: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return this.donorDao;
	}
	
	private Dao<City, Integer> getCityDao(final DatabaseHelper databaseHelper) {
		if (null == this.cityDao) {
			try {
				this.cityDao = databaseHelper.getCityDao();
			}
			catch (final SQLException e) {
				Log.e(LOG_TAG, "Unable to load DAO: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return this.cityDao;
	}

	private Dao<Country, Integer> getCountryDao(final DatabaseHelper databaseHelper) {
		if (null == this.countryDao) {
			try {
				this.countryDao = databaseHelper.getCountryDao();
			}
			catch (final SQLException e) {
				Log.e(LOG_TAG, "Unable to load DAO: " + e.getMessage());
				e.printStackTrace();
			}
		}
		return this.countryDao;
	}
}
