package com.sroyc.noderegistrar.repo.mongo;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sroyc.noderegistrar.exception.PrimaryLockException;
import com.sroyc.noderegistrar.main.NodeConstant;
import com.sroyc.noderegistrar.repo.PrimaryNodeLocker;
import com.sroyc.noderegistrar.repo.PrimaryNodeLockerRepository;

@Component
public class PrimaryNodeLockerRepositoryMongoImpl implements PrimaryNodeLockerRepository {

	private MongoPrimaryNodeLockerRepository lockerRepo;

	@Autowired
	public PrimaryNodeLockerRepositoryMongoImpl(MongoPrimaryNodeLockerRepository lockerRepo) {
		super();
		this.lockerRepo = lockerRepo;
	}

	@Override
	public PrimaryNodeLocker createLock(String nodeId) throws PrimaryLockException {
		MongoPrimaryNodeLockerEntity locker = new MongoPrimaryNodeLockerEntity();
		locker.setLockedOn(LocalDateTime.now(Clock.systemUTC()));
		locker.setNodeId(nodeId);
		locker.setLockId(NodeConstant.LOCK_ID);
		return this.lockerRepo.insert(locker);
	}

	@Override
	public Optional<PrimaryNodeLocker> get() {
		Optional<MongoPrimaryNodeLockerEntity> entity = this.lockerRepo.findById(NodeConstant.LOCK_ID);
		if (entity.isPresent()) {
			return Optional.of(entity.get());
		}
		return Optional.empty();
	}

	@Override
	public void deleteLock() {
		this.lockerRepo.deleteById(NodeConstant.LOCK_ID);
	}

}
