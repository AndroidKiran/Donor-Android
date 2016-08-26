package com.robo.user.service;

import com.robo.models.EventResponse;
import com.robo.models.UserResponse;

import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

public interface UserService {
	
	@FormUrlEncoded
	@POST("/register")
	UserResponse createUser(@Field("firstName") String firstName,@Field("lastName") String lastName,
			@Field("password") String password,@Field("mobNum") String mobNum,@Field("emailId") String emailId,
			@Field("dob") String dob,@Field("state") String state,@Field("city") String city,@Field("bloodGroup") String bloodGroup,
			@Field("status") int status);
	
	@FormUrlEncoded
	@POST("/login")
	UserResponse loginUser(@Field("emailId") String emailId, @Field("password") String password);
	
	@FormUrlEncoded
	@POST("/Events")
	EventResponse createEvent(@Field("eventName") String eventName, @Field("eventDesc") String eventDesc,
			@Field("eventVenue") String eventVenue,@Field("eventContactNum") String eventContactNum,@Field("eventDate") String eventDate);
	
}
