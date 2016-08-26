package com.iamdonor.model;


import java.io.Serializable;

import com.j256.ormlite.field.DatabaseField;
import com.j256.ormlite.table.DatabaseTable;
import com.robo.models.WebServiceResponse;
@DatabaseTable
public class GroupBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@DatabaseField(generatedId = true)
	private int generateGroupId;
	@DatabaseField
	private int groupId;
	@DatabaseField
	private String groupName;
	@DatabaseField(foreign = true, foreignAutoRefresh = true, columnName = "group_res_id")
	private WebServiceResponse webServiceResponse;
	
	public GroupBean() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	public GroupBean(int groupId, String groupName,
			WebServiceResponse webServiceResponse) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
		this.webServiceResponse = webServiceResponse;
	}
	
	

	public GroupBean(int groupId, String groupName) {
		super();
		this.groupId = groupId;
		this.groupName = groupName;
	}

	public int getGenerateGroupId() {
		return generateGroupId;
	}

	public void setGenerateGroupId(int generateGroupId) {
		this.generateGroupId = generateGroupId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public String getGroupName() {
		return groupName;
	}

	public void setGroupName(String groupName) {
		this.groupName = groupName;
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
		result = prime * result + generateGroupId;
		result = prime * result + groupId;
		result = prime * result
				+ ((groupName == null) ? 0 : groupName.hashCode());
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
		GroupBean other = (GroupBean) obj;
		if (generateGroupId != other.generateGroupId)
			return false;
		if (groupId != other.groupId)
			return false;
		if (groupName == null) {
			if (other.groupName != null)
				return false;
		} else if (!groupName.equals(other.groupName))
			return false;
		if (webServiceResponse == null) {
			if (other.webServiceResponse != null)
				return false;
		} else if (!webServiceResponse.equals(other.webServiceResponse))
			return false;
		return true;
	}

	
}
