package com.robo.models;

import java.util.Collection;
import java.util.List;

import com.iamdonor.model.EmergencyBean;
import com.iamdonor.model.EventBean;
import com.iamdonor.model.GroupBean;
import com.iamdonor.model.UserBean;
import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.field.ForeignCollectionField;
import com.j256.ormlite.table.DatabaseTable;

@DatabaseTable
public class WebServiceResponse {
	
	@DatabaseField(generatedId = true)
	private int eventResponseId;
	@DatabaseField
	private boolean error;
	@DatabaseField
	private int pages;
	@DatabaseField
	private String message;
	@ForeignCollectionField(eager = false)
	private Collection<EventBean> events;
	@ForeignCollectionField(eager = false)
	private Collection<UserBean> donors;
	@ForeignCollectionField(eager = false)
	private Collection<GroupBean> groups;
	@ForeignCollectionField(eager = false)
	private Collection<EmergencyBean> needs;
	
	public int getEventResponseId() {
		return eventResponseId;
	}
	public void setEventResponseId(int eventResponseId) {
		this.eventResponseId = eventResponseId;
	}
	public boolean isError() {
		return error;
	}
	public void setError(boolean error) {
		this.error = error;
	}
	
	public int getPages() {
		return pages;
	}
	public void setPages(int pages) {
		this.pages = pages;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public Collection<EventBean> getEvents() {
		return events;
	}
	public void setEvents(Collection<EventBean> events) {
		this.events = events;
	}
	public Collection<UserBean> getDonors() {
		return donors;
	}
	public void setDonors(Collection<UserBean> donors) {
		this.donors = donors;
	}
	public Collection<GroupBean> getGroups() {
		return groups;
	}
	public void setGroups(Collection<GroupBean> groups) {
		this.groups = groups;
	}
	public Collection<EmergencyBean> getNeeds() {
		return needs;
	}
	public void setNeeds(Collection<EmergencyBean> needs) {
		this.needs = needs;
	}
	
	
	
}
