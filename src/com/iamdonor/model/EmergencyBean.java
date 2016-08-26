package com.iamdonor.model;

import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.robo.models.WebServiceResponse;

@DatabaseTable
public class EmergencyBean{

	@DatabaseField(generatedId = true)
	private int genEmergencyId;
	@DatabaseField
	private int emergerncyId;
	@DatabaseField
	private String bloodGroup;
	@DatabaseField
	private String place;
	@DatabaseField
	private String contactNum;
	@DatabaseField
	private String tillDate;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "emergency_res_id")
	private WebServiceResponse webServiceResponse;
	
	public EmergencyBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public EmergencyBean(String bloodGroup, String place, String contactNum,
			String tillDate) {
		super();
		this.bloodGroup = bloodGroup;
		this.place = place;
		this.contactNum = contactNum;
		this.tillDate = tillDate;
	}

	public EmergencyBean(int emergerncyId, String bloodGroup, String place,
			String contactNum) {
		super();
		this.emergerncyId = emergerncyId;
		this.bloodGroup = bloodGroup;
		this.place = place;
		this.contactNum = contactNum;
	}

	public int getGenEmergencyId() {
		return genEmergencyId;
	}

	public void setGenEmergencyId(int genEmergencyId) {
		this.genEmergencyId = genEmergencyId;
	}

	public int getEmergerncyId() {
		return emergerncyId;
	}

	public void setEmergerncyId(int emergerncyId) {
		this.emergerncyId = emergerncyId;
	}

	public String getBloodGroup() {
		return bloodGroup;
	}

	public void setBloodGroup(String bloodGroup) {
		this.bloodGroup = bloodGroup;
	}

	public String getPlace() {
		return place;
	}

	public void setPlace(String place) {
		this.place = place;
	}

	public String getContactNum() {
		return contactNum;
	}

	public void setContactNum(String contactNum) {
		this.contactNum = contactNum;
	}

	public String getTillDate() {
		return tillDate;
	}

	public void setTillDate(String tillDate) {
		this.tillDate = tillDate;
	}

	public WebServiceResponse getWebServiceResponse() {
		return webServiceResponse;
	}

	public void setWebServiceResponse(WebServiceResponse webServiceResponse) {
		this.webServiceResponse = webServiceResponse;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((bloodGroup == null) ? 0 : bloodGroup.hashCode());
		result = prime * result
				+ ((contactNum == null) ? 0 : contactNum.hashCode());
		result = prime * result + emergerncyId;
		result = prime * result + ((place == null) ? 0 : place.hashCode());
		result = prime * result
				+ ((tillDate == null) ? 0 : tillDate.hashCode());
		result = prime
				* result
				+ ((webServiceResponse == null) ? 0 : webServiceResponse
						.hashCode());
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
		EmergencyBean other = (EmergencyBean) obj;
		if (bloodGroup == null) {
			if (other.bloodGroup != null)
				return false;
		} else if (!bloodGroup.equals(other.bloodGroup))
			return false;
		if (contactNum == null) {
			if (other.contactNum != null)
				return false;
		} else if (!contactNum.equals(other.contactNum))
			return false;
		if (emergerncyId != other.emergerncyId)
			return false;
		if (place == null) {
			if (other.place != null)
				return false;
		} else if (!place.equals(other.place))
			return false;
		if (tillDate == null) {
			if (other.tillDate != null)
				return false;
		} else if (!tillDate.equals(other.tillDate))
			return false;
		if (webServiceResponse == null) {
			if (other.webServiceResponse != null)
				return false;
		} else if (!webServiceResponse.equals(other.webServiceResponse))
			return false;
		return true;
	}
}
