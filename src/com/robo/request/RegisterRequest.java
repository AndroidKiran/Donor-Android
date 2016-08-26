package com.robo.request;

import android.util.Log;

import com.iamdonor.model.DonorBean;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.robo.models.UserResponse;
import com.robo.userinterface.UserService;

public class RegisterRequest extends RetrofitSpiceRequest<UserResponse, UserService>{
	
	private DonorBean donorBean;
	
	public RegisterRequest(DonorBean donorBean) {
		super(UserResponse.class, UserService.class);
		this.donorBean = donorBean;
	}
	
	@Override
	public UserResponse loadDataFromNetwork() throws Exception {
		Log.i("dataaaaaaaaaaaaaaaaa", ""+donorBean.getFirstName()+"\n"+ donorBean.getLastName()+"\n"+ donorBean.getPassword()+"\n"+ 
					donorBean.getMobNum()+"\n"+donorBean.getEmailId()+"\n"+ donorBean.getDob()+"\n"+ donorBean.getCountry()+"\n"+ donorBean.getCity()+"\n"+ 
					donorBean.getBloodGroup()+"\n"+ donorBean.getStatus());
		return getService().createUser(donorBean.getFirstName(), donorBean.getLastName(), donorBean.getPassword(), 
					donorBean.getMobNum(),donorBean.getEmailId(), donorBean.getDob(), donorBean.getCountry(), donorBean.getCity(), 
					donorBean.getBloodGroup(), donorBean.getStatus());
		
		
	}

}
