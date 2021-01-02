package com.sroyc.noderegistrar.repo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NodeRegistration implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 944909536409665830L;

	private String nodeId;
	private InstanceStatus status;
	private LocalDateTime lastUpdated;

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public InstanceStatus getStatus() {
		return status;
	}

	public void setStatus(InstanceStatus status) {
		this.status = status;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public enum InstanceStatus {
		RUNNIG, STOPPED, LOADING;
	}

}
