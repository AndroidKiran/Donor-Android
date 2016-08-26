package com.iamdonor.activity;


import java.net.SocketTimeoutException;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;

import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatEditText;
import com.devspark.appmsg.AppMsg;
import com.iamadonar.utils.ConnectionDetector;
import com.iamadonar.utils.DonorSingleton;
import com.iamadonar.utils.Validation;
import com.iamadonor.splash.R;
import com.iamdonor.model.DonorBean;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.ormlite.utils.DonorRepository;
import com.robo.loading.MyApplication;
import com.robo.models.UserResponse;
import com.robo.request.LoginRequest;
import com.robo.utils.AbstractLoadingRequestListener;

public class LoginActivity extends BaseAbstractActivity implements OnClickListener{
	
	private EditText editTextEmailId;
	private EditText editTextPassword;
	private Button loginButton;
	private Button registerButton;
	private Intent intent;
	private LoginRequest getLoginRequest;
	private Context mContext;
	private DonorSingleton donorSingleton;
	private ConnectionDetector connectionDetector;
	private AppMsg appMsg;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_login_layout);
		mContext = LoginActivity.this;
		connectionDetector = new ConnectionDetector(mContext);
		registerEditText();
		
		donorSingleton = new DonorSingleton();
		MyApplication.setDonorSingleton(donorSingleton);
		
		loginButton = (FlatButton) findViewById(R.id.button_login);
		loginButton.setOnClickListener(this);
		registerButton = (Button) findViewById(R.id.button_register);
		registerButton.setOnClickListener(this);
		
	}
	
	private void registerEditText(){
		
		editTextEmailId = (FlatEditText) findViewById(R.id.editText_Email);
		editTextEmailId.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
               Validation.isEmailAddress(editTextEmailId, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
		
		editTextPassword = (FlatEditText) findViewById(R.id.editText_Password);
		editTextPassword.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
               Validation.isNull(editTextPassword, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
		
		
		
	}
	
	 private boolean checkValidation() {
	        boolean ret = true;
	        if (!Validation.isEmailAddress(editTextEmailId, true)) ret = false;
	        if (!Validation.isNull(editTextPassword, true)) ret = false;
	       
	        return ret;
	    }

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_login:
			if (checkValidation()) {
				DonorBean donorBean = new DonorBean();
				donorBean.setEmailId(editTextEmailId.getText().toString());
				donorBean.setPassword(editTextPassword.getText().toString());
				getLoginRequest = new LoginRequest(donorBean);
				
				if (getLoginRequest != null && connectionDetector.isConnectingToInternet()) {
					getSpiceManager().execute(getLoginRequest, new LoginRequestListner());
				}else{
					appMsg = AppMsg.makeText(LoginActivity.this, "NO NETWORK..!", AppMsg.STYLE_ALERT);
					appMsg.show();
				}
				
			}
			break;
			
		case R.id.button_register:
			intent = new Intent(LoginActivity.this, RegistrationActivity.class);
			startActivityForResult(intent, 1);
			break;

		default:
			break;
		}
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		super.onActivityResult(requestCode, resultCode, intent);
		
		if (requestCode == 1) {
			
			Bundle bundle = intent.getExtras();
			if (bundle != null) {
				editTextEmailId.setText(bundle.getString("email"));
				editTextPassword.setText(bundle.getString("password"));
				appMsg = AppMsg.makeText(LoginActivity.this, bundle.getString("message"), AppMsg.STYLE_INFO);
				appMsg.show();
			}
		}
	}
	
	private class LoginRequestListner extends AbstractLoadingRequestListener<UserResponse>{
		
		private Dialog progressDialog;
		private DonorRepository donorRepository;
		
		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isLoginDataInCache()) {
				return;
			}else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isLoginDataInCache()) {
				return;
			}
		}
		
		@Override
		public void onRequestSuccess(UserResponse userResponse) {
			if (userResponse != null) {
				if (!userResponse.isError()) {
					MyApplication.getDonorSingleton().setLoginDataInCache(true);
					try {
						appMsg = AppMsg.makeText(LoginActivity.this, userResponse.getMessage(), AppMsg.STYLE_INFO);
						appMsg.show();
						donorRepository = new DonorRepository(mContext);
						donorRepository.saveOrUpdateDonor(userResponse.getUsers());
						MyApplication.getDonorSingleton().setDonorBean(userResponse.getUsers());
						intent = new Intent(LoginActivity.this, DrawerMainActivity.class);
						startActivity(intent);
						finish();
					} catch (Exception e) {
						e.printStackTrace();
					}
					
				}else{
					appMsg = AppMsg.makeText(LoginActivity.this, userResponse.getMessage(), AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			} else {
				appMsg = AppMsg.makeText(LoginActivity.this, "NETWORK ERROR..!", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
		}

		@Override
		public void showLoading() {
			progressDialog = MyApplication.showLoading(LoginActivity.this);
		}

		@Override
		public void hideLoading() {
			MyApplication.dismissLoading(progressDialog);
			progressDialog = null;
		}
	}
	
	@Override
	protected void onDestroy() {
		super.onDestroy();
		if (appMsg != null) {
			appMsg.cancelAll(LoginActivity.this);
			appMsg = null;
		}
	}

}
