package com.robo.loading;

import com.iamadonar.utils.AnimatedScaleDrawable;
import com.iamadonor.splash.R;
import com.iamadonor.splash.R.drawable;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager.LayoutParams;
import android.view.animation.BounceInterpolator;
import android.widget.ProgressBar;

public class FullScreenProgressDialog extends Dialog {
	
	private Context context;
	private AnimatedScaleDrawable drawable;
	public FullScreenProgressDialog(Context context) {
		super(context);
		this.context = context;
	}
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		getWindow().setLayout(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
		getWindow().setBackgroundDrawable(new ColorDrawable(0));
		//setCancelable(false);
		setContentView(R.layout.progress_dialog);
		ProgressBar progressBar = (ProgressBar)findViewById(R.id.progressBar);
		drawable = new AnimatedScaleDrawable(context.getResources().getDrawable(R.drawable.heart));
		drawable.setInterpolator(new BounceInterpolator());
		drawable.setInvertTransformation(true);
		drawable.setDuration(500);
		progressBar.setIndeterminateDrawable(drawable);
		
	}
	public static Dialog show(Context context) {
		FullScreenProgressDialog toRet = new FullScreenProgressDialog(context);
		toRet.show();
		return toRet;
	}

}