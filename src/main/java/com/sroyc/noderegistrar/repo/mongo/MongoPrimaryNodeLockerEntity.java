package com.sroyc.noderegistrar.repo.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sroyc.noderegistrar.repo.PrimaryNodeLocker;

@Document(collection = "node_locker")
public class MongoPrimaryNodeLockerEntity extends PrimaryNodeLocker {

	/**
	 * 
	 */
	private static final long serialVersionUID = -1422294294801320740L;

	@Id
	private Integer lockId;

	@Override
	public Integer getLockId() {
		return lockId;
	}

	@Override
	public void setLockId(Integer lockId) {
		this.lockId = lockId;
	}

}
