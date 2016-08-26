package com.iamdonor.child.frgament;

import java.net.SocketTimeoutException;
import java.util.Calendar;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatEditText;
import com.devspark.appmsg.AppMsg;
import com.iamadonar.utils.ConnectionDetector;
import com.iamadonar.utils.DatePickerFragment;
import com.iamadonar.utils.Validation;
import com.iamadonor.splash.R;
import com.iamdonor.model.EmergencyBean;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.robo.loading.MyApplication;
import com.robo.models.WebServiceResponse;
import com.robo.request.WebServiceRequest;
import com.robo.utils.AbstractLoadingRequestListener;

import android.app.Dialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TableLayout;

public class PostEmergencyFragment extends BaseAbstractFragment implements OnItemSelectedListener, OnClickListener {

	private View mPostEmergencyView;
	private SherlockFragmentActivity mContext;
	private Spinner mSpinnerChooseBloodGroup;
	private EditText mEditTextAddress;
	private EditText mEditTextContact;
	private EditText mEditTextDate;
	private Button mButtonDate;
	
	private String mBloodGroup;
	private Button mButtonPostNeed;
	private DatePickerFragment date;
	private WebServiceRequest postBloodNeed;
	private ConnectionDetector connectionDetector;
	private AppMsg appMsg;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		mContext = getSherlockActivity();
		connectionDetector = new ConnectionDetector(mContext);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		mPostEmergencyView = inflater.inflate(R.layout.fragment_post_need, container,false);
		registerViews();
		return mPostEmergencyView;
	}
	
	private void registerViews(){
		mSpinnerChooseBloodGroup = (Spinner)mPostEmergencyView.findViewById(R.id.spinner_NeedBloodGroup);
		mSpinnerChooseBloodGroup.setOnItemSelectedListener(this);
		mEditTextAddress = (FlatEditText)mPostEmergencyView.findViewById(R.id.editText_NeedAddress);
		mEditTextContact = (FlatEditText)mPostEmergencyView.findViewById(R.id.editText_NeedContactNum);
		mEditTextDate = (FlatEditText)mPostEmergencyView.findViewById(R.id.editText_NeedDate);
		mButtonDate = (Button)mPostEmergencyView.findViewById(R.id.button_NeedDate);
		mButtonDate.setOnClickListener(this);
		mButtonPostNeed = (FlatButton)mPostEmergencyView.findViewById(R.id.button_AddNeed);
		mButtonPostNeed.setOnClickListener(this);
	}

	@Override
	public void onItemSelected(AdapterView<?> parent, View view, int position,
			long id) {
		setmBloodGroup((String)mSpinnerChooseBloodGroup.getSelectedItem());
	}

	@Override
	public void onNothingSelected(AdapterView<?> parent) {
		
	}

	@Override
	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.button_AddNeed:
			if (checkValidation()) {
				postBloodNeed = new WebServiceRequest(7, createBean());
				if (postBloodNeed != null && connectionDetector.isConnectingToInternet()) {
					getSpiceManager().execute(postBloodNeed, new PostEmergencyRequestListener());
				}else{
					appMsg = AppMsg.makeText(mContext, "No Network", AppMsg.STYLE_ALERT);
					appMsg.show();
				}
				
			}
			break;
		case R.id.button_NeedDate:
			showDatePicker();
			break;
		default:
			break;
		}
	}
	
	private EmergencyBean createBean(){
		String address = mEditTextAddress.getText().toString();
		String contactNum = mEditTextContact.getText().toString();
		String date = mEditTextDate.getText().toString();
		
		EmergencyBean emergencyBean = new EmergencyBean(getmBloodGroup(), address, contactNum, date);
		return emergencyBean;
	}
	
	
	private boolean checkValidation(){
		 boolean ret = true;
		 
		 if (!Validation.isNull(mEditTextAddress, true)) ret = false;
		 if (!Validation.isPhoneNum(mEditTextContact, true)) ret = false;
		 if (!Validation.isNull(mEditTextDate, true)) ret = false;
		 
		 return ret;
	}
	
	private void showDatePicker() {
		  date = new DatePickerFragment();
		  /**
		   * Set Up Current Date Into dialog
		   */
		  Calendar calender = Calendar.getInstance();
		  Bundle args = new Bundle();
		  args.putInt("year", calender.get(Calendar.YEAR));
		  args.putInt("month", calender.get(Calendar.MONTH));
		  args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
		  date.setArguments(args);
		  /**
		   * Set Call back to capture selected date
		   */
		  date.setCallBack(ondate);
		  date.show(getChildFragmentManager(), "Date Picker");
		 }

		 OnDateSetListener ondate = new OnDateSetListener() {
		  @Override
		  public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
			  String strDate = String.valueOf(year) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth);
			  mEditTextDate.setText(strDate);
		}
	};
	
	private class PostEmergencyRequestListener extends AbstractLoadingRequestListener<WebServiceResponse>{

		private Dialog progressDialog;
		
		@Override
		public void onRequestFailure(SpiceException spiceException) {
			if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isPostNeedInCache()) {
				return;
			}else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isPostNeedInCache()) {
				return;
			}
		}

		@Override
		public void onRequestSuccess(WebServiceResponse mPostEmergencyResponse) {
			if (mPostEmergencyResponse != null) {
				if (!mPostEmergencyResponse.isError()) {
					MyApplication.getDonorSingleton().setPostNeedInCache(true);
					appMsg = AppMsg.makeText(mContext, mPostEmergencyResponse.getMessage(), AppMsg.STYLE_INFO);
					appMsg.show();
				}else{
					appMsg = AppMsg.makeText(mContext, mPostEmergencyResponse.getMessage(), AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			}else{
				appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
				appMsg.show();
			}
			
			TableLayout tableLayout = (TableLayout) mPostEmergencyView.findViewById(R.id.tableLayout_AddNeed);
			MyApplication.clearForm(tableLayout);
		}

		@Override
		public void showLoading() {
			progressDialog = MyApplication.showLoading(mContext);
		}

		@Override
		public void hideLoading() {
			MyApplication.dismissLoading(progressDialog);
			progressDialog = null;
		}
		
	}
	
	public String getmBloodGroup() {
		return mBloodGroup;
	}

	public void setmBloodGroup(String mBloodGroup) {
		this.mBloodGroup = mBloodGroup;
	}
	
	@Override
	public void onDestroyView() {
		super.onDestroyView();
		if (appMsg != null) {
			appMsg.cancelAll(mContext);
			appMsg = null;
		}
	}
	
}
