package com.robo.request;

import android.util.Log;

import com.iamdonor.model.EmergencyBean;
import com.iamdonor.model.EventBean;
import com.iamdonor.model.SearchBean;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.robo.loading.MyApplication;
import com.robo.models.WebServiceResponse;
import com.robo.userinterface.UserService;

public class WebServiceRequest extends RetrofitSpiceRequest<WebServiceResponse, UserService>{
	
	private int choice;
	private WebServiceResponse webServiceResponse;
	private EventBean eventBean;
	private SearchBean searchBean;
	private String category;
	private EmergencyBean emergencyBean;
	private int currentpage;
	private String groupName;
	private int groupId;
	
	
	public WebServiceRequest(int choice,int currentpage) {
		super(WebServiceResponse.class, UserService.class);
		this.choice = choice;
		this.currentpage = currentpage;
	}

	public WebServiceRequest(int choice,EventBean eventBean) {
		super(WebServiceResponse.class, UserService.class);
		this.choice = choice;
		this.eventBean = eventBean;
	}
	
	public WebServiceRequest(int choice,SearchBean searchBean,int currentpage) {
		super(WebServiceResponse.class, UserService.class);
		this.choice = choice;
		this.searchBean = searchBean;
		this.currentpage = currentpage;
	}
	
	public WebServiceRequest(int choice,String category,int currentpage) {
		super(WebServiceResponse.class, UserService.class);
		this.choice = choice;
		this.category = category;
		this.currentpage = currentpage;
	}
	
	
	public WebServiceRequest(int choice,EmergencyBean emergencyBean) {
		super(WebServiceResponse.class, UserService.class);
		this.choice = choice;
		this.emergencyBean = emergencyBean;
	}

	public WebServiceRequest(int choice, String groupName) {
		super(WebServiceResponse.class, UserService.class);
		this.choice = choice;
		this.groupName = groupName;
	}

	public WebServiceRequest(int choice, int groupId, int currentpage) {
		super(WebServiceResponse.class, UserService.class);
		this.choice = choice;
		this.currentpage = currentpage;
		this.groupId = groupId;
	}

	@Override
	public WebServiceResponse loadDataFromNetwork() throws Exception {
		try {
			String autho = MyApplication.getDonorSingleton().getDonorBean().getApiKey();
			System.out.println("authoooooooooooooo"+autho);
			int regId = MyApplication.getDonorSingleton().getDonorBean().getRegId();
			System.out.println("regIDDDDDDDDDDDDDD"+regId);
			
			switch (choice) {
			case 1:
				setWebServiceResponse(getService().getEvents(autho, currentpage));
				break;
			case 2:
				setWebServiceResponse(getService().createEvent(autho, eventBean.getEventName(), eventBean.getEventDesc(), eventBean.getEventVenue(),
					eventBean.getEventContactNum(), eventBean.getEventDate()));
				break;
			case 3:
				setWebServiceResponse(getService().searchDonors(autho, searchBean.getCountry(), searchBean.getCity(), searchBean.getBloodGroup(),currentpage));
				break;
			case 4:
				setWebServiceResponse(getService().getGroup(autho, category, regId, currentpage));
				break;
			case 5:
				setWebServiceResponse(getService().createGroup(autho, groupName, regId));
				break;
			case 6:
				setWebServiceResponse(getService().getEmergency(autho,currentpage));
				break;
			case 7:
				setWebServiceResponse(getService().postEmergency(autho, emergencyBean.getBloodGroup(), emergencyBean.getPlace(), emergencyBean.getContactNum(), emergencyBean.getTillDate()));
				break;
			case 8:
				setWebServiceResponse(getService().getGroupMembers(autho, groupId, currentpage));
				break;
			case 9:
				setWebServiceResponse(getService().postJoinGroup(autho, currentpage , regId));
				break;
			}
			return getWebServiceResponse();
		} catch (Exception excep) {
			excep.printStackTrace();
			throw new IllegalAccessError("What the fuck!");
		}
		
	}

	public WebServiceResponse getWebServiceResponse() {
		return webServiceResponse;
	}

	public void setWebServiceResponse(WebServiceResponse webServiceResponse) {
		this.webServiceResponse = webServiceResponse;
	}
	
	
}
