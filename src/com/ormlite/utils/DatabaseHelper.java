package com.ormlite.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.SQLException;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.util.Log;

import com.iamdonor.model.City;
import com.iamdonor.model.Country;
import com.iamdonor.model.DonorBean;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

public class DatabaseHelper extends OrmLiteSqliteOpenHelper {

	// name of the database file for your application -- change to something appropriate for your app
	private static final String DATABASE_NAME = "IAmDonorDB.db";
	// any time you make changes to your database objects, you may have to increase the database version
	private static final int DATABASE_VERSION = 1;
	
	private Context mContext;
	 //Database path
    private String DATABASE_PATH = "/data/data/com.iamadonor.splash/databases/";
    

	// the DAO object we use to access the SimpleData table
	private Dao<DonorBean, Integer> donorDao = null;
	private Dao<Country, Integer> countryDao = null;
	private Dao<City, Integer> cityDao = null;
	private SQLiteDatabase sqliteDatabase;

	public DatabaseHelper(final Context context) {
		super(context, DATABASE_NAME, null, DATABASE_VERSION);
		this.mContext = context;
	}
	
	@Override
	public void onCreate(final SQLiteDatabase db, final ConnectionSource connectionSource) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onCreate");
			TableUtils.createTable(connectionSource, DonorBean.class);
			TableUtils.createTable(connectionSource, Country.class);
			TableUtils.createTable(connectionSource, City.class);
		}
		catch (final SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't create database", e);
			throw new RuntimeException(e);
		}
	}

	/**
	 * This is called when your application is upgraded and it has a higher version number. This allows you to adjust the various data to
	 * match the new version number.
	 */
	@Override
	public void onUpgrade(final SQLiteDatabase db, final ConnectionSource connectionSource, final int oldVersion, final int newVersion) {
		try {
			Log.i(DatabaseHelper.class.getName(), "onUpgrade");
			TableUtils.dropTable(connectionSource, DonorBean.class, true);
			TableUtils.dropTable(connectionSource, Country.class, true);
			TableUtils.dropTable(connectionSource, City.class, true);
			// after we drop the old databases, we create the new ones
			onCreate(db, connectionSource);
		}
		catch (final SQLException e) {
			Log.e(DatabaseHelper.class.getName(), "Can't drop databases", e);
			throw new RuntimeException(e);
		}
	}
	
	/**
	 * Creates a empty database on the system and rewrites it with your own database.
	 * */
	public void createDataBase() throws IOException{

		File dbfile = new File(DATABASE_PATH,DATABASE_NAME);
		
		if(!dbfile.exists())
		{
			//By calling this method and empty database will be created into the default system path
			//of your application so we are gonna be able to overwrite that database with our database.
			this.getReadableDatabase();

			try {
				copyDataBase(); 
			} catch (IOException e) {
				throw new Error("Error copying database");
			}
		}
	}


	/**
	 * Copies your database from your local assets-folder to the just created empty database in the
	 * system folder, from where it can be accessed and handled.
	 * This is done by transfering bytestream.
	 * */
	private void copyDataBase() throws IOException{

		//Open your local db as the input stream
		InputStream myInput = mContext.getAssets().open("database/"+DATABASE_NAME);

		// Path to the just created empty db
		String outFileName = DATABASE_PATH + DATABASE_NAME;

		//Open the empty db as the output stream
		OutputStream myOutput = new FileOutputStream(outFileName);

		//transfer bytes from the inputfile to the outputfile
		byte[] buffer = new byte[1024];
		int length;
		while ((length = myInput.read(buffer))>0){
			myOutput.write(buffer, 0, length);
		}

		//Close the streams
		myOutput.flush();
		myOutput.close();
		myInput.close();

	}

	public void openDataBase() throws SQLException{
		//Open the database
		String myPath = DATABASE_PATH + DATABASE_NAME;
		sqliteDatabase = SQLiteDatabase.openDatabase(myPath, null, SQLiteDatabase.OPEN_READWRITE);
	}
	


	public Dao<DonorBean, Integer> getDonorDao() throws SQLException {
		if (this.donorDao == null) {
			this.donorDao = getDao(DonorBean.class);
		}
		return this.donorDao;
	}
	
	public Dao<Country, Integer> getCountryDao() throws SQLException {
		if (this.countryDao == null) {
			this.countryDao = getDao(Country.class);
		}
		return this.countryDao;
	}
	
	public Dao<City, Integer> getCityDao() throws SQLException {
		if (this.cityDao == null) {
			this.cityDao = getDao(City.class);
		}
		return this.cityDao;
	}

	/**
	 * Close the database connections and clear any cached DAOs.
	 */
	@Override
	public void close() {
		if(sqliteDatabase != null)
			sqliteDatabase.close();
		super.close();
		this.donorDao = null;
		this.countryDao = null;
	}

}
