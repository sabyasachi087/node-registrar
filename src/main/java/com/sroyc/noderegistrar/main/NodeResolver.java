package com.sroyc.noderegistrar.main;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import org.springframework.beans.factory.DisposableBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.sroyc.noderegistrar.repo.NodeCommandRequestRepository;
import com.sroyc.noderegistrar.repo.NodeRegistration;
import com.sroyc.noderegistrar.repo.NodeRegistrationRepository;
import com.sroyc.noderegistrar.repo.PrimaryNodeLocker;
import com.sroyc.noderegistrar.repo.PrimaryNodeLockerRepository;

@Component
public class NodeResolver implements DisposableBean, ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = Logger.getLogger(NodeResolver.class.getCanonicalName());
	private final ThreadPoolExecutor resolverThread = (ThreadPoolExecutor) Executors.newFixedThreadPool(1);

	private final AtomicBoolean primary = new AtomicBoolean(false);

	private PrimaryNodeLockerRepository lockRepo;
	private NodeRegistrationRepository regRepo;
	private NodeCommandRequestRepository commandRepo;

	@Autowired
	public NodeResolver(PrimaryNodeLockerRepository lockRepo, NodeRegistrationRepository regRepo,
			NodeCommandRequestRepository commandRepo) {
		super();
		this.lockRepo = lockRepo;
		this.regRepo = regRepo;
		this.commandRepo = commandRepo;
	}

	public boolean isPrimary() {
		return this.primary.get();
	}

	protected void resolve() {
		if (this.resolverThread.getActiveCount() == 0) {
			this.resolverThread.execute(this::doResolve);
		}
	}

	private void doResolve() {
		try {
			LOGGER.log(Level.FINE, "Starting node resolver ... ");
			this.correctExpiredEntries();
			this.tryAcquireLock();
			LOGGER.log(Level.FINE, "Node resolver completed");
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, "Unable to complete node resolver", ex);
		}
	}

	private void tryAcquireLock() {
		try {
			Optional<PrimaryNodeLocker> data = this.lockRepo.get();
			if (data.isEmpty()) {
				this.lockRepo.createLock(NodeConstant.NODE_ID);
				primary.set(true);
				LOGGER.log(Level.INFO, String.format("Node Id [%s] is now primary instance", NodeConstant.NODE_ID));
			}
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
		}
	}

	private void tryReleaseLock() {
		try {
			Optional<PrimaryNodeLocker> data = this.lockRepo.get();
			if (data.isPresent() && data.get().getNodeId().equals(NodeConstant.NODE_ID)) {
				this.lockRepo.deleteLock();
				LOGGER.log(Level.INFO, String.format("Node Id [%s] has released the lock", NodeConstant.NODE_ID));
			}
		} catch (Exception ex) {
			LOGGER.log(Level.SEVERE, ex.getMessage());
		}
	}

	private List<NodeRegistration> correctExpiredEntries() {
		List<NodeRegistration> expired = this.getExpiredEntries();
		expired.forEach(reg -> {
			this.commandRepo.deleteByNodeId(reg.getNodeId());
			Optional<PrimaryNodeLocker> data = this.lockRepo.get();
			if (data.isPresent() && data.get().getNodeId().equals(reg.getNodeId())) {
				this.lockRepo.deleteLock();
			}
			this.regRepo.delete(reg.getNodeId());
		});
		return expired;
	}

	private List<NodeRegistration> getExpiredEntries() {
		List<NodeRegistration> registrations = this.regRepo.findAll();
		if (!CollectionUtils.isEmpty(registrations)) {
			return registrations.stream()
					.filter(reg -> !reg.getNodeId().equals(NodeConstant.NODE_ID)
							&& reg.getLastUpdated().isBefore(LocalDateTime.now(Clock.systemUTC()).minusMinutes(10)))
					.collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public void destroy() throws Exception {
		LOGGER.log(Level.INFO, "Closing node resolver and will release lock (if any) ");
		this.resolverThread.shutdown();
		this.tryReleaseLock();
		this.commandRepo.deleteByNodeId(NodeConstant.NODE_ID);
		this.regRepo.delete(NodeConstant.NODE_ID);
		this.resolverThread.awaitTermination(1, TimeUnit.MINUTES);
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent event) {
		this.resolve();
	}

}
