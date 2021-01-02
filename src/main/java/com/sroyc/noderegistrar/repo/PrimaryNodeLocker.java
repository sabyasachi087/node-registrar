package com.sroyc.noderegistrar.repo;

import java.io.Serializable;
import java.time.LocalDateTime;

public class PrimaryNodeLocker implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3561434028056520635L;

	private Integer lockId;
	private String nodeId;
	private LocalDateTime lockedOn;

	public Integer getLockId() {
		return lockId;
	}

	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}

	public String getNodeId() {
		return nodeId;
	}

	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

	public LocalDateTime getLockedOn() {
		return lockedOn;
	}

	public void setLockedOn(LocalDateTime lockedOn) {
		this.lockedOn = lockedOn;
	}

}
