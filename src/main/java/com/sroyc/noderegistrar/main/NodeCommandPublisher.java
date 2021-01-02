package com.sroyc.noderegistrar.main;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.sroyc.noderegistrar.repo.NodeCommandRequest;
import com.sroyc.noderegistrar.repo.NodeCommandRequest.CommunicationStatus;
import com.sroyc.noderegistrar.repo.NodeCommandRequestRepository;
import com.sroyc.noderegistrar.repo.NodeRegistration;
import com.sroyc.noderegistrar.repo.NodeRegistrationRepository;

@Component
public class NodeCommandPublisher {

	private NodeCommandRequestRepository ncrRepo;
	private NodeRegistrationRepository nodeRepo;

	@Autowired
	public NodeCommandPublisher(NodeCommandRequestRepository ncrRepo, NodeRegistrationRepository nodeRepo) {
		this.ncrRepo = ncrRepo;
		this.nodeRepo = nodeRepo;
	}

	protected NodeCommandRequest create(String command, String nodeId) {
		NodeCommandRequest ncr = new NodeCommandRequest();
		ncr.setCommand(command);
		ncr.setLastUpdated(LocalDateTime.now(Clock.systemUTC()));
		ncr.setRaisedBy(nodeId);
		ncr.setRequestId(UUID.randomUUID().toString());
		ncr.setStatus(CommunicationStatus.PUBLISHED);
		ncr.setNodeId(nodeId);
		return this.ncrRepo.save(ncr);
	}

	public void publish(String command) {
		List<NodeRegistration> registrations = this.nodeRepo.findAll();
		registrations.forEach(reg -> create(command, reg.getNodeId()));
	}

}
