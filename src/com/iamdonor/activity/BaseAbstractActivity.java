package com.iamdonor.activity;


import com.actionbarsherlock.app.SherlockActivity;
import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.octo.android.robospice.SpiceManager;
import com.octo.android.robospice.networkstate.NetworkStateChecker;
import com.robo.loading.MyApplication;
import com.robo.retrofit.service.RetroFitService;

public class BaseAbstractActivity extends SherlockActivity {

	SpiceManager spcMngr = new SpiceManager(RetroFitService.class);

	@Override
	protected void onStart() {
		super.onStart();
		
		spcMngr.start(this);
	}

	@Override
	protected void onStop() {
		spcMngr.shouldStop();
		super.onStop();
	}

	public SpiceManager getSpiceManager() {
		return this.spcMngr;
	}
}
