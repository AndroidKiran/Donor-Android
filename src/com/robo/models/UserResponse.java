package com.robo.models;

import java.util.Collection;

import com.iamdonor.model.DonorBean;
import com.iamdonor.model.EventBean;
import com.iamdonor.model.UserBean;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class UserResponse {
	
	@DatabaseField(generatedId = true)
	private int userResponseId;
	@DatabaseField
	private boolean error;
	@DatabaseField
	private String message;
	private DonorBean users;
	public int getUserResponseId() {
		return userResponseId;
	}
	public void setUserResponseId(int userResponseId) {
		this.userResponseId = userResponseId;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public DonorBean getUsers() {
		return users;
	}
	public void setUsers(DonorBean users) {
		this.users = users;
	}
	
	
	
	
}
	
