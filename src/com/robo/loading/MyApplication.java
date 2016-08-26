package com.robo.loading;

import com.devspark.appmsg.AppMsg;
import com.iamadonar.utils.DonorSingleton;
import com.iamadonar.utils.IntentUtils;

import android.app.Activity;
import android.app.Application;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

public class MyApplication extends Application {

	private static Context instance;
	private static DonorSingleton donorSingleton;
	private static Intent intent;
	private static AppMsg appMsg;
	
	@Override
	public void onCreate() {
		super.onCreate();
		instance = this;
	}

	public static Context getContext() {
		return instance;
	}

	public static Dialog showLoading(Context c) {
		try {
			return FullScreenProgressDialog.show(c);
		} catch (Exception e) {
			Log.e("MyApplication", "Can't instantiate dialog", e);
		}
		return null;
	}

	public static void dismissLoading(Dialog dialog) {
		if (dialog != null) {
			try {
				dialog.dismiss();
			} catch (Exception e) {
				Log.e("MyApplication", "Can't dismiss dialog", e);
			}
		}
	}

	public static DonorSingleton getDonorSingleton() {
		return donorSingleton;
	}

	public static void setDonorSingleton(DonorSingleton donorSingleton) {
		MyApplication.donorSingleton = donorSingleton;
	}
	
	public static void clearForm(ViewGroup group)
	{       
	    for (int i = 0, count = group.getChildCount(); i < count; ++i) {
	    View view = group.getChildAt(i);
	    if (view instanceof EditText) {
	        ((EditText)view).getText().clear();
	    }

	    if(view instanceof ViewGroup && (((ViewGroup)view).getChildCount() > 0))
	        clearForm((ViewGroup)view);
	    }
	}
	
	public static void emailIntent(Context context,String to,String subject,String text){
		try {
			intent = IntentUtils.sendEmail(to, subject, text);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(Intent.createChooser(intent, "Send Mail Using"));
		} catch (ActivityNotFoundException  e) {
			e.printStackTrace();
			appMsg = AppMsg.makeText((Activity) context, "No Acivity found", AppMsg.STYLE_ALERT);
			appMsg.show();
		}
		
	}
	
	public static void dailIntent(Context context, String phoneNumber){
		try {
			intent = IntentUtils.dialPhone(phoneNumber);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(Intent.createChooser(intent, "Dial Using"));
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			appMsg = AppMsg.makeText((Activity) context, "No Acivity found", AppMsg.STYLE_ALERT);
			appMsg.show();
		}
		
	}
	
	public static void showOnMap(Context context, String query){
		try {
			intent = IntentUtils.findLocation(query);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(Intent.createChooser(intent, "Open Map Using.."));
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			appMsg = AppMsg.makeText((Activity) context, "No Acivity found", AppMsg.STYLE_ALERT);
			appMsg.show();
		}
		
	}
	
	public static void openLink(Context context,String url){
		try {
			intent = IntentUtils.openLink(url);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
			context.startActivity(Intent.createChooser(intent, "Open Link Using..."));
		} catch (ActivityNotFoundException e) {
			e.printStackTrace();
			appMsg = AppMsg.makeText((Activity) context, "No Acivity found", AppMsg.STYLE_ALERT);
			appMsg.show();
		}
		
	}

}
