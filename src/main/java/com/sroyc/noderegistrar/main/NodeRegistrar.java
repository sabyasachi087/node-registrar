package com.sroyc.noderegistrar.main;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.stereotype.Component;

import com.sroyc.noderegistrar.repo.NodeRegistration;
import com.sroyc.noderegistrar.repo.NodeRegistration.InstanceStatus;
import com.sroyc.noderegistrar.repo.NodeRegistrationRepository;

@Component
public class NodeRegistrar implements ApplicationListener<ContextRefreshedEvent> {

	private static final Logger LOGGER = Logger.getLogger(NodeRegistrar.class.getCanonicalName());

	private NodeRegistrationRepository nodeRepo;

	@Autowired
	public NodeRegistrar(NodeRegistrationRepository nodeRepo) {
		this.nodeRepo = nodeRepo;
	}

	protected void register() {
		Optional<NodeRegistration> data = this.nodeRepo.findByNodeId(NodeConstant.NODE_ID);
		if (data.isEmpty()) {
			NodeRegistration reg = new NodeRegistration();
			reg.setNodeId(NodeConstant.NODE_ID);
			reg.setStatus(InstanceStatus.RUNNIG);
			reg.setLastUpdated(LocalDateTime.now(Clock.systemUTC()));
			this.nodeRepo.register(reg);
			LOGGER.log(Level.INFO, String.format("Node [%s] has been registered", NodeConstant.NODE_ID));
		} else {
			this.nodeRepo.heartbeat(NodeConstant.NODE_ID, LocalDateTime.now(Clock.systemUTC()));
		}
	}

	@Override
	public void onApplicationEvent(ContextRefreshedEvent arg0) {
		this.register();
	}

}
