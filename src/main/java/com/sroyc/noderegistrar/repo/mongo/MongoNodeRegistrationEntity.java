package com.sroyc.noderegistrar.repo.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sroyc.noderegistrar.repo.NodeRegistration;

@Document(collection = "node_registration")
public class MongoNodeRegistrationEntity extends NodeRegistration {

	/**
	 * 
	 */
	private static final long serialVersionUID = -8295512175219518573L;

	@Id
	private String nodeId;

	@Override
	public String getNodeId() {
		return nodeId;
	}

	@Override
	public void setNodeId(String nodeId) {
		this.nodeId = nodeId;
	}

}
