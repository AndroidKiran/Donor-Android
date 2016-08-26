package com.iamdonor.model;

import java.io.Serializable;

public class SearchBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String country;
	private String city;
	private String bloodGroup;
	public SearchBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	public SearchBean(String country, String city, String bloodGroup) {
		super();
		this.country = country;
		this.city = city;
		this.bloodGroup = bloodGroup;
	}
	public String getCountry() {
		return country;
	}
	public void setCountry(String country) {
		this.country = country;
	}
	public String getCity() {
		return city;
	}
	public void setCity(String city) {
		this.city = city;
	}
	public String getBloodGroup() {
		return bloodGroup;
	}
	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	
	
}
