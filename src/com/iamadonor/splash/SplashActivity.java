package com.iamadonor.splash;

import java.io.IOException;
import java.util.List;

import com.actionbarsherlock.app.SherlockActivity;
import com.iamadonar.utils.DonorSingleton;
import com.iamdonor.activity.DrawerMainActivity;
import com.iamdonor.activity.LoginActivity;
import com.iamdonor.model.DonorBean;
import com.ormlite.utils.DatabaseHelper;
import com.ormlite.utils.DonorRepository;
import com.robo.loading.MyApplication;

import android.content.Context;
import android.content.Intent;
import android.database.SQLException;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.ProgressBar;

public class SplashActivity extends SherlockActivity {

	private ProgressBar progressBar;
	private Context mContext;
	private List<DonorBean> donorList;

	private Intent intent;
	private DonorRepository donorRepo;
	private DonorSingleton mDonorSingleton;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_splash);
		mContext = SplashActivity.this;
		progressBar = (ProgressBar)findViewById(R.id.progressBar);
		mDonorSingleton = new DonorSingleton();
		new FlashCall().execute(25);
	}


	
	private class FlashCall extends AsyncTask<Integer, Integer, Void>{
		
		@Override
		protected void onPreExecute() {
			super.onPreExecute();
			
			progressBar.setMax(100);
		}

		@Override
		protected Void doInBackground(Integer... params) {
			int start = params[0];
			
			try {
				new DatabaseHelper(mContext).createDataBase();
			} catch (IOException e2) {
				e2.printStackTrace();
			}
			
			try {
				
				donorRepo = new DonorRepository(mContext);
				
			} catch (SQLException e1) {
				e1.printStackTrace();
				throw new Error("Unable to Open database");
			}
			
			
			for (int i = start; i < 100; i+=25) {
				try {
					
					publishProgress(i);
					SystemClock.sleep(1000);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			return null;
		}

		@Override
		protected void onProgressUpdate(Integer... values) {
			super.onProgressUpdate(values);
			
			progressBar.setProgress(values[0]);
		}

		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			setDonorList(donorRepo.getDonors());
			Log.e("SIZZZZZZZZEEEEEEE",""+getDonorList().size());
			if (getDonorList().size() > 0) {
				mDonorSingleton.setDonorBean(getDonorList().get(0));
				MyApplication.setDonorSingleton(mDonorSingleton);
				String m = MyApplication.getDonorSingleton().getDonorBean().getFirstName();
				Log.e("mmmmmmmmmmmmmmmmmmmmmmmmmmmmm", m);
				intent = new Intent(SplashActivity.this, DrawerMainActivity.class);
				startActivity(intent);
				finish();
			}else{
				intent = new Intent(SplashActivity.this, LoginActivity.class);
				startActivity(intent);
				finish();
			}
			
			
		}
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event)
	{
		switch (keyCode)
		{
		case KeyEvent.KEYCODE_BACK :
			return true;
		}
		
		return super.onKeyDown(keyCode, event);
	}
	
	
	
	@Override
    protected void onStart() {
        super.onStart();
    }  
    @Override
    protected void onResume()
    {
        super.onResume();
       
    }  
    
    @Override
    protected void onPause() {
        super.onPause();
        
    }
   @Override
    protected void onStop() {
        super.onStop();
        
    }
   @Override
    protected void onDestroy() 
    {	
        super.onDestroy();

 	   
	   	if (donorRepo != null) {
			donorRepo = null;
		}
	   	
	   	if(getDonorList().size() > 0){
	   		donorList.clear();
	   	}
        this.finish();
    }



	public List<DonorBean> getDonorList() {
		return donorList;
	}



	public void setDonorList(List<DonorBean> donorList) {
		this.donorList = donorList;
	}

   

}
