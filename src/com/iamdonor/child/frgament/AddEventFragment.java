package com.iamdonor.child.frgament;

import java.net.SocketTimeoutException;
import java.util.Calendar;

import android.app.DatePickerDialog.OnDateSetListener;
import android.app.Dialog;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TableLayout;

import com.actionbarsherlock.app.SherlockFragmentActivity;
import com.cengalabs.flatui.views.FlatButton;
import com.cengalabs.flatui.views.FlatEditText;
import com.devspark.appmsg.AppMsg;
import com.iamadonar.utils.ConnectionDetector;
import com.iamadonar.utils.DatePickerFragment;
import com.iamadonar.utils.Validation;
import com.iamadonor.splash.R;
import com.iamdonor.model.EventBean;
import com.octo.android.robospice.exception.NoNetworkException;
import com.octo.android.robospice.persistence.exception.SpiceException;
import com.robo.loading.MyApplication;
import com.robo.models.WebServiceResponse;
import com.robo.request.WebServiceRequest;
import com.robo.utils.AbstractLoadingRequestListener;



public class AddEventFragment extends BaseAbstractFragment implements OnClickListener {
	
	
	private Resources mResource;
	private View mAddEventView;
	private DatePickerFragment date;
	private Button mPostEventButton;
	private EditText mEditTextEventName;
	private EditText mEditTextEventDesc;
	private EditText mEditTextEventVenue;
	private EditText mEditTextEventContactNum;
	private EditText mEditTextEventDate;
	private Button mDatePickerButton;
	private EventBean eventBean;
	private WebServiceRequest getPostEventRequest;
	private SherlockFragmentActivity mContext;
	private ConnectionDetector connectionDetector;
	private AppMsg appMsg;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mContext = getSherlockActivity();
		mResource = getResources();
		mContext.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);	
		connectionDetector = new ConnectionDetector(mContext);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
 
        mAddEventView = inflater.inflate(R.layout.fragment_add_event, container, false);
       
        registerTextView();
        mPostEventButton = (FlatButton)mAddEventView.findViewById(R.id.button_AddEvent);
        mPostEventButton.setOnClickListener(this);
        mDatePickerButton = (Button)mAddEventView.findViewById(R.id.button_Date);
        mDatePickerButton.setOnClickListener(this);
        return mAddEventView;
    }
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		setMenuVisibility(true);
	}
	
	private void registerTextView(){
		mEditTextEventName = (FlatEditText)mAddEventView.findViewById(R.id.editText_EventName);
		
		mEditTextEventDesc = (FlatEditText)mAddEventView.findViewById(R.id.editText_EventDesc);
		
		mEditTextEventVenue = (FlatEditText)mAddEventView.findViewById(R.id.editText_EventVenue);
		
		mEditTextEventContactNum = (FlatEditText)mAddEventView.findViewById(R.id.editText_EventContactNum);
		
		mEditTextEventDate = (FlatEditText) mAddEventView.findViewById(R.id.editText_Date);
	}
	
	@Override
	public void onClick(View view) {
		switch (view.getId()) {
		case R.id.button_AddEvent:
			if (checkValidation()) {
				setEventBean(createEventBean());
				getPostEventRequest = new WebServiceRequest(2,getEventBean());
				if (getPostEventRequest != null && connectionDetector.isConnectingToInternet()) {
					getSpiceManager().execute(getPostEventRequest, new PostEventRequestListner());
				}else{
					appMsg = AppMsg.makeText(mContext, "No Network", AppMsg.STYLE_ALERT);
					appMsg.show();
				}
			}
			break;
		case R.id.button_Date:
			showDatePicker();
			break;
		default:
			break;
		}
	}
	
	
	 private boolean checkValidation() {
		 boolean ret = true;
		 if (!Validation.isAlphabets(mEditTextEventName, true)) ret = false;
		 if (!Validation.isAlphabets(mEditTextEventDesc, true)) ret = false;
		 if (!Validation.isAlphabets(mEditTextEventVenue, true)) ret = false;
		 if (!Validation.isPhoneNum(mEditTextEventContactNum, true)) ret = false;
		 if (!Validation.isNull(mEditTextEventDate, true)) ret = false;
		 return ret;
	 }
	 
	 private EventBean createEventBean(){
		 String eventName = mEditTextEventName.getText().toString();
		 String eventDesc = mEditTextEventDesc.getText().toString();
		 String eventVenue = mEditTextEventVenue.getText().toString();
		 String eventContactNum = mEditTextEventContactNum.getText().toString();
		 String eventDate = mEditTextEventDate.getText().toString();
		 
		 EventBean eventBean = new EventBean(eventName, eventDesc, eventVenue, eventContactNum, eventDate);
		 return eventBean;
	 }
	 

		private void showDatePicker() {
			  date = new DatePickerFragment();
			  
			  Calendar calender = Calendar.getInstance();
			  Bundle args = new Bundle();
			  args.putInt("year", calender.get(Calendar.YEAR));
			  args.putInt("month", calender.get(Calendar.MONTH));
			  args.putInt("day", calender.get(Calendar.DAY_OF_MONTH));
			  date.setArguments(args);
			  
			  date.setCallBack(ondate);
			  date.show(getChildFragmentManager(), "Date Picker");
			 }

			 OnDateSetListener ondate = new OnDateSetListener() {
				 
			  @Override
			  public void onDateSet(DatePicker view, int year, int monthOfYear,int dayOfMonth) {
				  String strDate = String.valueOf(year) + "-" + String.valueOf(monthOfYear+1) + "-" + String.valueOf(dayOfMonth);
				  mEditTextEventDate.setText(strDate);
			}
		};
		
		private class PostEventRequestListner extends AbstractLoadingRequestListener<WebServiceResponse>{
			
			private Dialog progressDialog;

			@Override
			public void onRequestFailure(SpiceException spiceException) {
				if (spiceException instanceof NoNetworkException && MyApplication.getDonorSingleton().isPostEventInCache()) {
					return;
				}else if (spiceException.getCause() instanceof SocketTimeoutException && MyApplication.getDonorSingleton().isPostEventInCache()) {
					return;
				}
			}
			
			@Override
			public void onRequestSuccess(WebServiceResponse mPostEventResponse) {
				if (mPostEventResponse != null) {
					MyApplication.getDonorSingleton().setPostEventInCache(true);
					if (!mPostEventResponse.isError()) {
						appMsg = AppMsg.makeText(mContext, mPostEventResponse.getMessage(), AppMsg.STYLE_INFO);
						appMsg.show();
					}else{
						appMsg = AppMsg.makeText(mContext, mPostEventResponse.getMessage(), AppMsg.STYLE_ALERT);
						appMsg.show();
					}
				}else{
					appMsg = AppMsg.makeText(mContext, "Network Error", AppMsg.STYLE_ALERT);
					appMsg.show();
				}
				
				TableLayout tableLayout = (TableLayout) mAddEventView.findViewById(R.id.tableLayout_AddEvent);
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
		
		@Override
		public void onDestroyView() {
			super.onDestroyView();
			if (appMsg != null) {
				appMsg.cancelAll(mContext);
				appMsg = null;
			}
		}
		
		
		
		
		public EventBean getEventBean() {
			return eventBean;
		}

		public void setEventBean(EventBean eventBean) {
			this.eventBean = eventBean;
		}
		
		
}
