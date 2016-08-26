package com.iamadonar.utils;

import com.iamdonor.model.DonorBean;

public class DonorSingleton {
	
	private DonorBean donorBean;
	private boolean emergencyDataInCache;
	private boolean eventDataInCache;
	private boolean groupsDataInCache;
	private boolean myGroupsDataInCache;
	private boolean donorDataInCache;
	private boolean loginDataInCache;
	private boolean registerInCache;
	private boolean postEventInCache;
	private boolean postNewGroupInCache;
	private boolean postNeedInCache;
	private boolean searchInCache;
	private boolean joinInCache;

	public DonorBean getDonorBean() {
		return donorBean;
	}

	public void setDonorBean(DonorBean donorBean) {
		this.donorBean = donorBean;
	}

	public boolean isEmergencyDataInCache() {
		return emergencyDataInCache;
	}

	public void setEmergencyDataInCache(boolean emergencyDataInCache) {
		this.emergencyDataInCache = emergencyDataInCache;
	}

	public boolean isEventDataInCache() {
		return eventDataInCache;
	}

	public void setEventDataInCache(boolean eventDataInCache) {
		this.eventDataInCache = eventDataInCache;
	}

	public boolean isGroupsDataInCache() {
		return groupsDataInCache;
	}

	public void setGroupsDataInCache(boolean groupsDataInCache) {
		this.groupsDataInCache = groupsDataInCache;
	}

	public boolean isMyGroupsDataInCache() {
		return myGroupsDataInCache;
	}

	public void setMyGroupsDataInCache(boolean myGroupsDataInCache) {
		this.myGroupsDataInCache = myGroupsDataInCache;
	}

	public boolean isDonorDataInCache() {
		return donorDataInCache;
	}

	public void setDonorDataInCache(boolean donorDataInCache) {
		this.donorDataInCache = donorDataInCache;
	}

	public boolean isLoginDataInCache() {
		return loginDataInCache;
	}

	public void setLoginDataInCache(boolean loginDataInCache) {
		this.loginDataInCache = loginDataInCache;
	}

	public boolean isRegisterInCache() {
		return registerInCache;
	}

	public void setRegisterInCache(boolean registerInCache) {
		this.registerInCache = registerInCache;
	}

	public boolean isPostEventInCache() {
		return postEventInCache;
	}

	public void setPostEventInCache(boolean postEventInCache) {
		this.postEventInCache = postEventInCache;
	}

	public boolean isPostNewGroupInCache() {
		return postNewGroupInCache;
	}

	public void setPostNewGroupInCache(boolean postNewGroupInCache) {
		this.postNewGroupInCache = postNewGroupInCache;
	}

	public boolean isPostNeedInCache() {
		return postNeedInCache;
	}

	public void setPostNeedInCache(boolean postNeedInCache) {
		this.postNeedInCache = postNeedInCache;
	}

	public boolean isSearchInCache() {
		return searchInCache;
	}

	public void setSearchInCache(boolean searchInCache) {
		this.searchInCache = searchInCache;
	}

	public boolean isJoinInCache() {
		return joinInCache;
	}

	public void setJoinInCache(boolean joinInCache) {
		this.joinInCache = joinInCache;
	}
	
	
	
}
