package com.sroyc.noderegistrar.repo;

import java.util.Optional;

import com.sroyc.noderegistrar.exception.PrimaryLockException;

public interface PrimaryNodeLockerRepository {

	public PrimaryNodeLocker createLock(String nodeId) throws PrimaryLockException;

	public Optional<PrimaryNodeLocker> get();

	public void deleteLock();

}
