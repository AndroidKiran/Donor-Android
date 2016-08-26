package com.robo.request;

import com.iamdonor.model.DonorBean;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.robo.models.UserResponse;
import com.robo.userinterface.UserService;

public class LoginRequest extends RetrofitSpiceRequest<UserResponse, UserService>{
	
	private DonorBean donorBean;
	public LoginRequest(DonorBean donorBean) {
		super(UserResponse.class, UserService.class);
		this.donorBean = donorBean;
	}

	@Override
	public UserResponse loadDataFromNetwork() throws Exception {
		
		return getService().loginUser(donorBean.getEmailId(), donorBean.getPassword());
		
	}

}
