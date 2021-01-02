package com.sroyc.noderegistrar.repo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface NodeRegistrationRepository {

	/** Persists {@linkplain NodeRegistration}. */
	public NodeRegistration register(NodeRegistration registration);

	/** Find {@linkplain NodeRegistration} by nodeId */
	public Optional<NodeRegistration> findByNodeId(String nodeUid);

	/** Return list of all registered nodes */
	public List<NodeRegistration> findAll();

	/**
	 * Update the `lastUpdated` attribute.
	 * 
	 * @return true if success, false otherwise
	 */
	public boolean heartbeat(String nodeUid, LocalDateTime beatTime);

	/** Remove node entry */
	public void delete(String nodeUid);

}
