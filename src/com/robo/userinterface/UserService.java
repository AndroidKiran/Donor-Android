package com.robo.userinterface;

import com.robo.models.WebServiceResponse;
import com.robo.models.UserResponse;

import retrofit.Callback;
import retrofit.http.EncodedPath;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Header;
import retrofit.http.POST;
import retrofit.http.Path;

public interface UserService {
	
	@FormUrlEncoded
	@POST("/register")
	UserResponse createUser(@Field("firstName") String firstName,@Field("lastName") String lastName,
			@Field("password") String password,@Field("mobNum") String mobNum,@Field("emailId") String emailId,
			@Field("dob") String dob,@Field("country") String country,@Field("city") String city,@Field("bloodGroup") String bloodGroup,
			@Field("status") int status);
	
	@FormUrlEncoded
	@POST("/login")
	UserResponse loginUser(@Field("emailId") String emailId, @Field("password") String password);
	
	@FormUrlEncoded
	@POST("/events")
	WebServiceResponse createEvent(@Header("Authorization") String authorization,@Field("eventName") String eventName, @Field("eventDesc") String eventDesc,
			@Field("eventVenue") String eventVenue,@Field("eventContactNum") String eventContactNum,@Field("eventDate") String eventDate);
	
	/*@GET("/events")
	WebServiceResponse getEvents(@Header("Authorization") String authorization);*/
	@GET("/events/page={currentpage}")
	WebServiceResponse getEvents(@Header("Authorization") String authorization,@EncodedPath("currentpage") int currentpage);
	
	
	/*@GET("/donorlist/{country}/{city}/{bloodGroup}")
	WebServiceResponse searchDonors(@Header("Authorization") String authorization,@Path("country") String country,@Path("city") String city,@EncodedPath("bloodGroup") String bloodGroup);*/
	@GET("/donorlist/country={country}/city={city}/bloodgroup={bloodGroup}/page={currentpage}")
	WebServiceResponse searchDonors(@Header("Authorization") String authorization,@Path("country") String country,@Path("city") String city,@EncodedPath("bloodGroup") String bloodGroup,@EncodedPath("currentpage") int currentpage);
	
	@FormUrlEncoded
	@POST("/groups")
	WebServiceResponse createGroup(@Header("Authorization") String authorization,@Field("groupName") String groupName, @Field("regId") int regId);
	
	/*@GET("/groups/{category}/{regId}")
	WebServiceResponse getGroup(@Header("Authorization") String authorization,@Path("category") String category, @EncodedPath("regId") int regId);*/
	@GET("/groups/category={category}/regid={regId}/page={currentpage}")
	WebServiceResponse getGroup(@Header("Authorization") String authorization,@Path("category") String category, @EncodedPath("regId") int regId,@EncodedPath("currentpage") int currentpage);
	
	/*@GET("/emergency")
	WebServiceResponse getEmergency(@Header("Authorization") String authorization);*/
	
	@GET("/emergency/page={currentpage}")
	WebServiceResponse getEmergency(@Header("Authorization") String authorization,@EncodedPath("currentpage") int currentpage);
	
	@FormUrlEncoded
	@POST("/emergency")
	WebServiceResponse postEmergency(@Header("Authorization") String authorization,@Field("bloodGroup") String bloodGroup,@Field("place") String place,@Field("contactNum") String contactNum,
			@Field("tillDate") String tillDate);
	
	/*@GET("/groups/{groupId}")
	WebServiceResponse getGroupMembers(@Header("Authorization") String authorization, @EncodedPath("groupId") int groupId);
	*/
	@GET("/groups/donor/groupid={groupId}/page={currentpage}")
	WebServiceResponse getGroupMembers(@Header("Authorization") String authorization, @EncodedPath("groupId") int groupId, @EncodedPath("currentpage") int currentpage);
	
	
	@FormUrlEncoded
	@POST("/join")
	WebServiceResponse postJoinGroup(@Header("Authorization") String authorization,@Field("groupId") int groupId,@Field("regId") int regId);
}
