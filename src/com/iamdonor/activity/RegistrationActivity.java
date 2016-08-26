package com.iamdonor.activity;


import java.net.SocketTimeoutException;
import java.util.Calendar;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;

import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatEditText;
import com.devspark.appmsg.AppMsg;
import com.iamadonar.utils.ConnectionDetector;
import com.iamadonar.utils.DonorSingleton;
import com.iamadonar.utils.Validation;
import com.iamadonor.splash.R;
import com.iamdonor.model.City;
import com.iamdonor.model.Country;
import com.iamdonor.model.DonorBean;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.robo.loading.MyApplication;
import com.robo.models.UserResponse;
import com.robo.request.RegisterRequest;
import com.robo.utils.AbstractLoadingRequestListener;

public class RegistrationActivity extends BaseAbstractActivity implements OnClickListener, OnItemSelectedListener {
	
	private Button registerBtn;
	private EditText editTextFirstName;
	private EditText editTextLastName;
	private EditText editTextMobileNum;
	private EditText editTextEmailId;
	private EditText editTextDOB;
	private EditText editTextCity;
	private int year;
	private int month;
	private int day;
	private Button datePickerBtn;
	private Intent intent;
	private Button cancelBtn;
	private EditText editTextPassword;
	private Spinner bloodGroupSpinner;
	private Spinner statusSpinner;
	private int myStatus;
	private String bloodGroup;
	private String status;
	private RegisterRequest getRegisterRequest;
	private DonorBean donorBean;
	private EditText editTextCountry;
	private Country country;
	private City city;
	private Bundle bundle;
	private RegistrationActivity mContext;
	
	private static final int ACONSTANT = 2;
	private static final int DATE_PICKER_ID = 1111; 
	private static final String COUNTRY = "country";
	private static final String CITY = "city";
	private static final String IDENTIFIER = "identifier";

	private View mAcceptanceView;
	private ConnectionDetector connectionDetector;
	private AppMsg appMsg;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_registration_layout);
		mContext = RegistrationActivity.this;
		connectionDetector = new ConnectionDetector(mContext);
		final Calendar c = Calendar.getInstance();
        year  = c.get(Calendar.YEAR);
        month = c.get(Calendar.MONTH);
        day   = c.get(Calendar.DAY_OF_MONTH);
		
		registerEditText();
		
		bloodGroupSpinner = (Spinner)findViewById(R.id.spinner_BloodGroup);
		bloodGroupSpinner.setOnItemSelectedListener(this);
		
		statusSpinner = (Spinner)findViewById(R.id.spinner_Status);
		statusSpinner.setOnItemSelectedListener(this);
		
		registerBtn = (FlatButton) findViewById(R.id.button_Register);
		registerBtn.setOnClickListener(this);
		
		cancelBtn = (FlatButton)findViewById(R.id.button_Cancel);
		cancelBtn.setOnClickListener(this);
		
		datePickerBtn = (Button)findViewById(R.id.button_DatePicker);
		datePickerBtn.setOnClickListener(this);
		
		
	}
	
	@Override
	protected void onStart() {
		super.onStart();
	}

	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_Register:
			if (checkValidation()) {
				accpectDialog();
			}
			break;
		case R.id.button_DatePicker:
			showDialog(DATE_PICKER_ID);
			break;
			
		case R.id.button_Cancel:
			intent = new Intent();  
            setResult(1,intent);  
            finish();
			break;
			
		case R.id.editText_Country:
			if (editTextCity.getText().toString().trim().length() > 0) {
				editTextCity.getText().clear();
			}
			intent = new Intent(RegistrationActivity.this, ChoosePlaceActivity.class);
			startActivityForResult(intent, ACONSTANT);
			break;
			
		case R.id.editText_City:
			if (editTextCountry.getText().toString().length() > 0) {
				intent = new Intent(RegistrationActivity.this, ChoosePlaceActivity.class);
				bundle = new Bundle();
				bundle.putSerializable(COUNTRY, getCountry());
				intent.putExtras(bundle);
				startActivityForResult(intent, ACONSTANT);
			}else{
				appMsg = AppMsg.makeText(mContext, "Please Select the Country..!", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
			break;	
		default:
			break;
		}
		
	}
	
	private void registerEditText(){
		editTextFirstName = (FlatEditText) findViewById(R.id.editText_FirstName);
		editTextFirstName.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
               Validation.isAlphabets(editTextFirstName, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
		
		editTextLastName = (FlatEditText) findViewById(R.id.editText_LastName);
		editTextLastName.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
               Validation.isAlphabets(editTextLastName, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
		
		editTextPassword = (FlatEditText) findViewById(R.id.editText_MyPassword);
		editTextPassword.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
               Validation.isNull(editTextPassword, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
		
		editTextMobileNum = (FlatEditText) findViewById(R.id.editText_MobileNumber);
		editTextMobileNum.addTextChangedListener(new TextWatcher() {
            // after every change has been made to this editText, we would like to check validity
            public void afterTextChanged(Editable s) {
               Validation.isPhoneNum(editTextMobileNum, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
		
		editTextEmailId = (FlatEditText) findViewById(R.id.editText_EmailId);
		editTextEmailId.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
               Validation.isEmailAddress(editTextEmailId, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
		
		editTextDOB = (FlatEditText) findViewById(R.id.editText_DOB);
		editTextDOB.addTextChangedListener(new TextWatcher() {
            public void afterTextChanged(Editable s) {
               Validation.isNull(editTextDOB, true);
            }
            public void beforeTextChanged(CharSequence s, int start, int count, int after){}
            public void onTextChanged(CharSequence s, int start, int before, int count){}
        });
		
		editTextCity = (FlatEditText) findViewById(R.id.editText_City);
		editTextCity.setOnClickListener(this);
		
		editTextCountry = (FlatEditText) findViewById(R.id.editText_Country);
		editTextCountry.setOnClickListener(this);
		
		
	}
	
	 private boolean checkValidation() {
	        boolean ret = true;
	        if (!Validation.isAlphabets(editTextFirstName, true)) ret = false;
	        if (!Validation.isAlphabets(editTextLastName, true)) ret = false;
	        if (!Validation.isNull(editTextPassword, true)) ret = false;
	        if (!Validation.isPhoneNum(editTextMobileNum, true)) ret = false;
	        if (!Validation.isEmailAddress(editTextEmailId, true)) ret = false;
	        if (!Validation.isNull(editTextDOB, true)) ret = false;
	        if (!Validation.isNull(editTextCountry, true)) ret = false;
	        if (!Validation.isNull(editTextCity, true)) ret = false;
	        
	        return ret;
	    }
	 
	 @Override
	 protected Dialog onCreateDialog(int id) {
		 switch (id) {
		 case DATE_PICKER_ID:
	           
			 return new DatePickerDialog(this, pickerListener, year, month,day);
		 }
		 return null;
	 }
	 
	 private DatePickerDialog.OnDateSetListener pickerListener = new DatePickerDialog.OnDateSetListener() {
		 
		 @Override
		 public void onDateSet(DatePicker view, int selectedYear,int selectedMonth, int selectedDay) {
			 year  = selectedYear;
			 month = selectedMonth;
			 day   = selectedDay;
			 editTextDOB.setText(new StringBuilder().append(year).append("-").append(month+1)
					 .append("-").append(day).append(" "));
				
		 }
	 };
	
	 
	 @Override
		public void onItemSelected(AdapterView<?> parent, View view, int position,
				long id) {
			bloodGroup = (String) bloodGroupSpinner.getSelectedItem();
			status = (String) statusSpinner.getSelectedItem();
			if (status.equals("Not Ready To Donate")) {
				setMyStatus(0);
			}else{
				setMyStatus(1);
			}
			
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {
			// TODO Auto-generated method stub
			
		}
	 
	 
	 private DonorBean createDonorBean(){
		 
		 String firstName = editTextFirstName.getText().toString();
		 String lastName = editTextLastName.getText().toString();
		 String password = editTextPassword.getText().toString();
		 String mobNum = editTextMobileNum.getText().toString();
		 String emailId = editTextEmailId.getText().toString();
		 String dob = editTextDOB.getText().toString();
		 String state = editTextCountry.getText().toString();
		 String city = editTextCity.getText().toString();
		 
		 DonorBean donorBean = new DonorBean(firstName, lastName, password, mobNum, emailId, dob, state, city, bloodGroup, getMyStatus());
		 
		return donorBean;
		 
	 }
	 
	 private class RegisterRequestListner extends AbstractLoadingRequestListener<UserResponse>{

		private Dialog progressDialog;

		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isRegisterInCache()) {
				return;
			}else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isRegisterInCache()) {
				return;
			}
		}

		@Override
		public void onRequestSuccess(UserResponse userResponse) {
			if (userResponse != null) {
				if (!userResponse.isError()) {
					
						/*DonorSingleton donorSingleton = new DonorSingleton();
						MyApplication.setDonorSingleton(donorSingleton);
						MyApplication.getDonorSingleton().setRegisterInCache(true);
					*/
					callBackIntent(getDonorBean().getEmailId(),getDonorBean().getPassword(), userResponse.getMessage());
				}else{
					appMsg = AppMsg.makeText(mContext, userResponse.getMessage() , AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			} else {
				appMsg = AppMsg.makeText(mContext, "Network error", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
		}

		@Override
		public void showLoading() {
			progressDialog = MyApplication.showLoading(RegistrationActivity.this);
		}

		@Override
		public void hideLoading() {
			MyApplication.dismissLoading(progressDialog);
			progressDialog = null;
		}

		 
	 }
	 
	 private void callBackIntent(String email,String password,String message){
		 intent = new Intent();
		 bundle = new Bundle();
		 bundle.putString("email",email);
		 bundle.putString("password", password);
		 bundle.putString("message", message);
		 intent.putExtras(bundle);
         setResult(1,intent);  
         finish();
		}
	 
	 @Override
	 protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
		 super.onActivityResult(requestCode, resultCode, intent);
			
		 if (requestCode == ACONSTANT) {
			 Bundle bundle = intent.getExtras();
			 if (bundle != null) {
				if (bundle.getString(IDENTIFIER).equals(COUNTRY)) {
					setCountry((Country) bundle.getSerializable(COUNTRY));
					editTextCountry.setText(getCountry().getCountryName());
				}else{
					setCity((City)bundle.getSerializable(CITY));
					editTextCity.setText(getCity().getCityName());
				}
			}
		 }
	 }
	 
	 private void accpectDialog(){
		 
		 AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(mContext);
		 alertDialogBuilder.setIcon(R.drawable.ic_launcher);
		 alertDialogBuilder.setTitle("Terms And Condition...!");
		 alertDialogBuilder.setMessage("I agree to all the terms and norms of this" +
		 		" application and by my own will i "+
        "provide the personal required information");
		 alertDialogBuilder.setPositiveButton("I Agree", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				setDonorBean(createDonorBean());
				getRegisterRequest = new RegisterRequest(getDonorBean());
				if (getRegisterRequest != null && connectionDetector.isConnectingToInternet()) {
					getSpiceManager().execute(getRegisterRequest, new RegisterRequestListner());
				}else{
					appMsg = AppMsg.makeText(mContext, "NO NETWORK..!", AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			}
		});
		 
		 alertDialogBuilder.setNegativeButton("I Disagree", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.dismiss();
			}
		});
		 
		 alertDialogBuilder.show();
        		
	 }
	 

	public int getMyStatus() {
		return myStatus;
	}

	public void setMyStatus(int myStatus) {
		this.myStatus = myStatus;
	}
	
	

	public DonorBean getDonorBean() {
		return donorBean;
	}

	public void setDonorBean(DonorBean donorBean) {
		this.donorBean = donorBean;
	}

	public Country getCountry() {
		return country;
	}

	public void setCountry(Country country) {
		this.country = country;
	}

	public City getCity() {
		return city;
	}

	public void setCity(City city) {
		this.city = city;
	}

	@Override
	protected void onStop() {
		super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		
		if (getRegisterRequest != null) {
			getRegisterRequest = null;
		}
		if (getDonorBean() != null) {
			donorBean = null;
		}
		if (getCountry() != null) {
			country = null;
		}
		if (getCity() != null) {
			city = null;
		}
		if (appMsg != null) {
			appMsg.cancelAll(mContext);
			appMsg = null;
		}
		
		super.onDestroy();
		
	}

	
}
