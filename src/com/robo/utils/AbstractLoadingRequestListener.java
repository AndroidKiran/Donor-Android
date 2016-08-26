package com.robo.utils;

import org.springframework.http.HttpStatus;
import org.springframework.web.client.HttpClientErrorException;

import roboguice.util.temp.Ln;
import android.util.Log;

import com.octo.android.robospice.exception.NetworkException;
import com.octo.android.robospice.exception.RequestCancelledException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.octo.android.robospice.request.listener.RequestListener;
import com.octo.android.robospice.request.listener.RequestProgress;
import com.octo.android.robospice.request.listener.RequestProgressListener;
import com.octo.android.robospice.request.listener.RequestStatus;

public abstract class AbstractLoadingRequestListener<T> implements RequestListener<T>, RequestProgressListener {

	@Override
	public final void onRequestProgressUpdate(RequestProgress progress) {
		Log.e("AbstractLoadingRequestListener", "onRequestProgressUpdate " + progress.getStatus() + ", " + progress.getProgress());
		if (progress.getStatus() == RequestStatus.LOADING_FROM_NETWORK) {
			showLoading();

		} else if (progress.getStatus() == RequestStatus.COMPLETE) {
			hideLoading();
		}
		onRequestProgressUpdate_(progress);
	}

	public void onRequestProgressUpdate_(RequestProgress progress) {
	}
	
	@Override
	public void onRequestFailure(SpiceException spiceException) {
		spiceException.printStackTrace();
		 if(spiceException.getCause() instanceof HttpClientErrorException)
	        {
	            HttpClientErrorException exception = (HttpClientErrorException)spiceException.getCause();
	            if(exception.getStatusCode().equals(HttpStatus.UNAUTHORIZED))
	            {
	                Log.d("netwrprk error","401 ERROR");
	            }
	            else
	            {
	            	Log.d("network error","Other Network exception");
	            }
	        }
	        else if(spiceException instanceof RequestCancelledException)
	        {
	            Ln.d("Cancelled");
	        }
	        else if (spiceException instanceof NetworkException) {
	        	
				Ln.d("Network exception");
			}
	        else
	        {
	            Ln.d("Other exception");
	        }
	}
	
	public void onRequestFailure_(SpiceException e) {
	}
	
	public abstract void showLoading();
	
	public abstract void hideLoading();

}
