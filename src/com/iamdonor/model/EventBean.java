package com.iamdonor.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.robo.models.WebServiceResponse;

@DatabaseTable
public class EventBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	@DatabaseField(id = true)
	private int eventId;
	@DatabaseField
	private String eventName;
	@DatabaseField
	private String eventDesc;
	@DatabaseField
	private String eventVenue;
	@DatabaseField
	private String eventContactNum;
	@DatabaseField
	private String eventDate;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "event_res_id")
	private WebServiceResponse webServiceResponse;
	
	public EventBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EventBean(String eventName, String eventDesc, String eventVenue,
			String eventContactNum, String eventDate) {
		super();
		this.eventName = eventName;
		this.eventDesc = eventDesc;
		this.eventVenue = eventVenue;
		this.eventContactNum = eventContactNum;
		this.eventDate = eventDate;
	}

	public int getEventId() {
		return eventId;
	}

	public void setEventId(int eventId) {
		this.eventId = eventId;
	}

	public String getEventName() {
		return eventName;
	}

	public void setEventName(String eventName) {
		this.eventName = eventName;
	}

	public String getEventDesc() {
		return eventDesc;
	}

	public void setEventDesc(String eventDesc) {
		this.eventDesc = eventDesc;
	}

	public String getEventVenue() {
		return eventVenue;
	}

	public void setEventVenue(String eventVenue) {
		this.eventVenue = eventVenue;
	}

	public String getEventContactNum() {
		return eventContactNum;
	}

	public void setEventContactNum(String eventContactNum) {
		this.eventContactNum = eventContactNum;
	}

	public String getEventDate() {
		return eventDate;
	}

	public void setEventDate(String eventDate) {
		this.eventDate = eventDate;
	}

	public WebServiceResponse getEventResponse() {
		return webServiceResponse;
	}

	public void setEventResponse(WebServiceResponse webServiceResponse) {
		this.webServiceResponse = webServiceResponse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((eventContactNum == null) ? 0 : eventContactNum.hashCode());
		result = prime * result
				+ ((eventDate == null) ? 0 : eventDate.hashCode());
		result = prime * result
				+ ((eventDesc == null) ? 0 : eventDesc.hashCode());
		result = prime * result + eventId;
		result = prime * result
				+ ((eventName == null) ? 0 : eventName.hashCode());
		result = prime * result
				+ ((webServiceResponse == null) ? 0 : webServiceResponse.hashCode());
		result = prime * result
				+ ((eventVenue == null) ? 0 : eventVenue.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		EventBean other = (EventBean) obj;
		if (eventContactNum == null) {
			if (other.eventContactNum != null)
				return false;
		} else if (!eventContactNum.equals(other.eventContactNum))
			return false;
		if (eventDate == null) {
			if (other.eventDate != null)
				return false;
		} else if (!eventDate.equals(other.eventDate))
			return false;
		if (eventDesc == null) {
			if (other.eventDesc != null)
				return false;
		} else if (!eventDesc.equals(other.eventDesc))
			return false;
		if (eventId != other.eventId)
			return false;
		if (eventName == null) {
			if (other.eventName != null)
				return false;
		} else if (!eventName.equals(other.eventName))
			return false;
		if (webServiceResponse == null) {
			if (other.webServiceResponse != null)
				return false;
		} else if (!webServiceResponse.equals(other.webServiceResponse))
			return false;
		if (eventVenue == null) {
			if (other.eventVenue != null)
				return false;
		} else if (!eventVenue.equals(other.eventVenue))
			return false;
		return true;
	}
	
	
	
	
}
