package com.iamdonor.child.frgament;

import android.content.Context;

import com.actionbarsherlock.app.SherlockFragment;
import com.octo.android.robospice.SpiceManager;
import com.robo.retrofit.service.RetroFitService;

public class BaseAbstractFragment extends SherlockFragment{
	
	SpiceManager spcMngr = new SpiceManager(RetroFitService.class);
	@Override
	public void onStart() {
		super.onStart();
		spcMngr.start(getSherlockActivity());
	}

	@Override
	public void onStop() {

        if (spcMngr.isStarted()) {
        	spcMngr.shouldStop();
        }

		super.onStop();
	}

	public SpiceManager getSpiceManager() {
		return spcMngr;
	}
	
}
