package com.sroyc.noderegistrar.repo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class NodeCommandRequest implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6855146993859300296L;

	private String requestId;
	private String command;
	private String raisedBy;
	private String nodeId;
	private CommunicationStatus status;
	private LocalDateTime lastUpdated;

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public String getCommand() {
		return command;
	}

	public void setCommand(String command) {
		this.command = command;
	}

	public String getRaisedBy() {
		return raisedBy;
	}

	public void setRaisedBy(String raisedBy) {
		this.raisedBy = raisedBy;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public CommunicationStatus getStatus() {
		return status;
	}

	public void setStatus(CommunicationStatus status) {
		this.status = status;
	}

	public LocalDateTime getLastUpdated() {
		return lastUpdated;
	}

	public void setLastUpdated(LocalDateTime lastUpdated) {
		this.lastUpdated = lastUpdated;
	}

	public enum CommunicationStatus {
		PUBLISHED, CONSUMED, COMPLETED, FAILED;
	}
}
