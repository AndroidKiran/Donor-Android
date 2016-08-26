package com.iamdonor.model;

import java.io.Serializable;

public class MemberBean implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private int memberId;
	private int groupId;
	private int regId;
	
	public MemberBean() {
		super();
		// TODO Auto-generated constructor stub
	}

	public MemberBean(int groupId, int regId) {
		super();
		this.groupId = groupId;
		this.regId = regId;
	}

	public int getMemberId() {
		return memberId;
	}

	public void setMemberId(int memberId) {
		this.memberId = memberId;
	}

	public int getGroupId() {
		return groupId;
	}

	public void setGroupId(int groupId) {
		this.groupId = groupId;
	}

	public int getRegId() {
		return regId;
	}

	public void setRegId(int regId) {
		this.regId = regId;
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + groupId;
		result = prime * result + memberId;
		result = prime * result + regId;
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
		MemberBean other = (MemberBean) obj;
		if (groupId != other.groupId)
			return false;
		if (memberId != other.memberId)
			return false;
		if (regId != other.regId)
			return false;
		return true;
	}
	
	
	
}
