package com.iamdonor.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.robo.models.UserResponse;

@DatabaseTable(tableName = "user")
public class DonorBean {
	
	@DatabaseField(id = true,columnName = "reg_id")
	private int regId;
	
	@DatabaseField(columnName = "first_name")
	private String firstName;
	
	@DatabaseField(columnName = "last_name")
	private String lastName;
	
	@DatabaseField(columnName = "password")
	private String password;
	
	@DatabaseField(columnName = "mobile_num")
	private String mobNum;
	
	@DatabaseField(columnName = "email_id")
	private String emailId;
	
	@DatabaseField(columnName = "dob")
	private String dob;
	
	@DatabaseField(columnName = "country")
	private String country;
	
	@DatabaseField(columnName = "city")
	private String city;
	
	@DatabaseField(columnName = "blood_group")
	private String bloodGroup;
	
	@DatabaseField(columnName = "status")
	private int status;
	
	@DatabaseField(columnName = "api_key")
	private String apiKey;
	
	public DonorBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public DonorBean(int regId, String firstName, String lastName,
			String mobNum, String emailId, String dob, String country,
			String city, String bloodGroup) {
		super();
		this.regId = regId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.mobNum = mobNum;
		this.emailId = emailId;
		this.dob = dob;
		this.country = country;
		this.city = city;
		this.bloodGroup = bloodGroup;
	}
	
	

	public DonorBean(String firstName, String lastName, String password,
			String mobNum, String emailId, String dob, String country,
			String city, String bloodGroup, int status) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.mobNum = mobNum;
		this.emailId = emailId;
		this.dob = dob;
		this.country = country;
		this.city = city;
		this.bloodGroup = bloodGroup;
		this.status = status;
	}
	
	

	public DonorBean(int regId, String firstName, String lastName,
			String password, String mobNum, String emailId, String dob,
			String country, String city, String bloodGroup, int status,
			String apiKey) {
		super();
		this.regId = regId;
		this.firstName = firstName;
		this.lastName = lastName;
		this.password = password;
		this.mobNum = mobNum;
		this.emailId = emailId;
		this.dob = dob;
		this.country = country;
		this.city = city;
		this.bloodGroup = bloodGroup;
		this.status = status;
		this.apiKey = apiKey;
	}

	public int getRegId() {
		return regId;
	}

	public void setRegId(int regId) {
		this.regId = regId;
	}

	public String getFirstName() {
		return firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getMobNum() {
		return mobNum;
	}

	public void setMobNum(String mobNum) {
		this.mobNum = mobNum;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getDob() {
		return dob;
	}

	public void setDob(String dob) {
		this.dob = dob;
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

	public int getStatus() {
		return status;
	}

	public void setStatus(int status) {
		this.status = status;
	}

	public String getApiKey() {
		return apiKey;
	}

	public void setApiKey(String apiKey) {
		this.apiKey = apiKey;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((apiKey == null) ? 0 : apiKey.hashCode());
		result = prime * result
				+ ((bloodGroup == null) ? 0 : bloodGroup.hashCode());
		result = prime * result + ((city == null) ? 0 : city.hashCode());
		result = prime * result + ((country == null) ? 0 : country.hashCode());
		result = prime * result + ((dob == null) ? 0 : dob.hashCode());
		result = prime * result + ((emailId == null) ? 0 : emailId.hashCode());
		result = prime * result
				+ ((firstName == null) ? 0 : firstName.hashCode());
		result = prime * result
				+ ((lastName == null) ? 0 : lastName.hashCode());
		result = prime * result + ((mobNum == null) ? 0 : mobNum.hashCode());
		result = prime * result
				+ ((password == null) ? 0 : password.hashCode());
		result = prime * result + regId;
		result = prime * result + status;
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
		DonorBean other = (DonorBean) obj;
		if (apiKey == null) {
			if (other.apiKey != null)
				return false;
		} else if (!apiKey.equals(other.apiKey))
			return false;
		if (bloodGroup == null) {
			if (other.bloodGroup != null)
				return false;
		} else if (!bloodGroup.equals(other.bloodGroup))
			return false;
		if (city == null) {
			if (other.city != null)
				return false;
		} else if (!city.equals(other.city))
			return false;
		if (country == null) {
			if (other.country != null)
				return false;
		} else if (!country.equals(other.country))
			return false;
		if (dob == null) {
			if (other.dob != null)
				return false;
		} else if (!dob.equals(other.dob))
			return false;
		if (emailId == null) {
			if (other.emailId != null)
				return false;
		} else if (!emailId.equals(other.emailId))
			return false;
		if (firstName == null) {
			if (other.firstName != null)
				return false;
		} else if (!firstName.equals(other.firstName))
			return false;
		if (lastName == null) {
			if (other.lastName != null)
				return false;
		} else if (!lastName.equals(other.lastName))
			return false;
		if (mobNum == null) {
			if (other.mobNum != null)
				return false;
		} else if (!mobNum.equals(other.mobNum))
			return false;
		if (password == null) {
			if (other.password != null)
				return false;
		} else if (!password.equals(other.password))
			return false;
		if (regId != other.regId)
			return false;
		if (status != other.status)
			return false;
		return true;
	}
	
	

}
