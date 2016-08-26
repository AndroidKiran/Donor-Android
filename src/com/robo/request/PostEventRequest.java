package com.robo.request;

import com.iamdonor.model.EventBean;
import com.octo.android.robospice.request.retrofit.RetrofitSpiceRequest;
import com.robo.models.EventResponse;
import com.robo.user.service.UserService;

public class PostEventRequest extends RetrofitSpiceRequest<EventResponse, UserService>{
	
	private EventBean eventBean;
	public PostEventRequest(EventBean eventBean) {
		super(EventResponse.class, UserService.class);
		this.eventBean = eventBean;
	}
	
	@Override
	public EventResponse loadDataFromNetwork() throws Exception {
		
		return getService().createEvent(eventBean.getEventName(), eventBean.getEventDesc(), eventBean.getEventVenue(),
				eventBean.getEventContactNum(), eventBean.getEventDate());
	}

}
