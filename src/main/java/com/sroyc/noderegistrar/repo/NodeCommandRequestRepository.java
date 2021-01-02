package com.sroyc.noderegistrar.repo;

import java.util.List;

import com.sroyc.noderegistrar.repo.NodeCommandRequest.CommunicationStatus;

public interface NodeCommandRequestRepository {

	public NodeCommandRequest save(NodeCommandRequest request);

	public List<NodeCommandRequest> findByNodeIdAndStatus(String nodeId, CommunicationStatus status);

	/** Updates `status` and `lastUpdated` attributes */
	public NodeCommandRequest updateStatus(NodeCommandRequest request);

	/**
	 * Delete all requests having given nodeId
	 */
	public void deleteByNodeId(String nodeId);

}
