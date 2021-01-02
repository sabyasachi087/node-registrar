package com.sroyc.noderegistrar.repo.mongo;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import com.sroyc.noderegistrar.repo.NodeCommandRequest;

@Document(collection = "node_command_request")
public class MongoNodeCommandRequestEntity extends NodeCommandRequest {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6145812950758718322L;

	@Id
	private String requestId;

	@Override
	public String getRequestId() {
		return requestId;
	}

	@Override
	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

}
