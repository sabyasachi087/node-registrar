package com.sroyc.noderegistrar.repo.mongo;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Component;

import com.sroyc.noderegistrar.repo.NodeCommandRequest;
import com.sroyc.noderegistrar.repo.NodeCommandRequest.CommunicationStatus;
import com.sroyc.noderegistrar.repo.NodeCommandRequestRepository;

@Profile("sroyc.data.mongo")
@Component
public class NodeCommandRequestReqpositoryMongoImpl implements NodeCommandRequestRepository {

	private MongoNodeCommandRequestRepository commandRepo;
	private MongoTemplate mongoTemplate;

	@Autowired
	public NodeCommandRequestReqpositoryMongoImpl(MongoNodeCommandRequestRepository commandRepo,
			MongoTemplate mongoTemplate) {
		super();
		this.commandRepo = commandRepo;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public NodeCommandRequest save(NodeCommandRequest request) {
		MongoNodeCommandRequestEntity entity = this.cast(request);
		return this.commandRepo.save(entity);
	}

	protected MongoNodeCommandRequestEntity cast(NodeCommandRequest ncr) {
		if (ncr instanceof MongoNodeCommandRequestEntity) {
			return (MongoNodeCommandRequestEntity) ncr;
		} else {
			MongoNodeCommandRequestEntity entity = new MongoNodeCommandRequestEntity();
			entity.setCommand(ncr.getCommand());
			entity.setLastUpdated(ncr.getLastUpdated());
			entity.setNodeId(ncr.getNodeId());
			entity.setRaisedBy(ncr.getRaisedBy());
			entity.setRequestId(ncr.getRequestId());
			entity.setStatus(ncr.getStatus());
			return entity;
		}
	}

	@Override
	public List<NodeCommandRequest> findByNodeIdAndStatus(String nodeId, CommunicationStatus status) {
		Query q = new Query();
		q.addCriteria(Criteria.where("nodeId").is(nodeId).andOperator(Criteria.where("status").is(status)));
		List<MongoNodeCommandRequestEntity> requests = this.mongoTemplate.find(q, MongoNodeCommandRequestEntity.class);
		return requests.stream().map(cmd -> (NodeCommandRequest) cmd).collect(Collectors.toList());
	}

	@Override
	public NodeCommandRequest updateStatus(NodeCommandRequest request) {
		Query q = new Query();
		q.addCriteria(Criteria.where("requestId").is(request.getRequestId()));
		Update update = new Update();
		update.set("status", request.getStatus());
		update.set("lastUpdated", LocalDateTime.now());
		return this.mongoTemplate.update(MongoNodeCommandRequestEntity.class).matching(q).apply(update)
				.findAndModifyValue();
	}

	@Override
	public void deleteByNodeId(String nodeId) {
		Query q = new Query();
		q.addCriteria(Criteria.where("nodeId").is(nodeId));
		this.mongoTemplate.remove(q, MongoNodeCommandRequestEntity.class);

	}

}
