package com.myboard.userservice.entity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "ApprovalRequest")
public class ApprovalRequest {

	private Map<String, List<Board>> approvalsRequiredMap = new HashMap<String, List<Board>>();

	public Map<String, List<Board>> getApprovalsRequiredMap() {
		return approvalsRequiredMap;
	}

	public void setApprovalsRequiredMap(Map<String, List<Board>> approvalsRequiredMap) {
		this.approvalsRequiredMap = approvalsRequiredMap;
	}

}
