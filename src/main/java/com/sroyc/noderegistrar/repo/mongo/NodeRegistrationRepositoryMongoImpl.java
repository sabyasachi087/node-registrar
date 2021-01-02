package com.sroyc.noderegistrar.repo.mongo;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import com.sroyc.noderegistrar.repo.NodeRegistration;
import com.sroyc.noderegistrar.repo.NodeRegistrationRepository;

@Profile("sroyc.data.mongo")
@Component
public class NodeRegistrationRepositoryMongoImpl implements NodeRegistrationRepository {

	private MongoNodeRegistrationRepository nodeRepo;
	private MongoTemplate mongoTemplate;

	@Autowired
	public NodeRegistrationRepositoryMongoImpl(MongoNodeRegistrationRepository nodeRepo, MongoTemplate mongoTemplate) {
		super();
		this.nodeRepo = nodeRepo;
		this.mongoTemplate = mongoTemplate;
	}

	@Override
	public NodeRegistration register(NodeRegistration registration) {
		MongoNodeRegistrationEntity entity = this.cast(registration);
		return this.nodeRepo.save(entity);
	}

	protected MongoNodeRegistrationEntity cast(NodeRegistration reg) {
		if (reg instanceof MongoNodeRegistrationEntity) {
			return (MongoNodeRegistrationEntity) reg;
		} else {
			MongoNodeRegistrationEntity entity = new MongoNodeRegistrationEntity();
			entity.setLastUpdated(reg.getLastUpdated());
			entity.setNodeId(reg.getNodeId());
			entity.setStatus(reg.getStatus());
			return entity;
		}
	}

	@Override
	public Optional<NodeRegistration> findByNodeId(String nodeUid) {
		Query q = new Query();
		q.addCriteria(Criteria.where("nodeId").is(nodeUid));
		MongoNodeRegistrationEntity entity = this.mongoTemplate.findById(nodeUid, MongoNodeRegistrationEntity.class);
		return Optional.ofNullable(entity);
	}

	@Override
	public List<NodeRegistration> findAll() {
		List<MongoNodeRegistrationEntity> registrations = this.nodeRepo.findAll();
		if (!CollectionUtils.isEmpty(registrations)) {
			return registrations.stream().map(reg -> (NodeRegistration) reg).collect(Collectors.toList());
		}
		return Collections.emptyList();
	}

	@Override
	public boolean heartbeat(String nodeUid, LocalDateTime beatTime) {
		Optional<MongoNodeRegistrationEntity> data = this.nodeRepo.findById(nodeUid);
		if (data.isPresent()) {
			MongoNodeRegistrationEntity entity = data.get();
			entity.setLastUpdated(beatTime);
			this.nodeRepo.save(entity);
			return true;
		}
		return false;
	}

	@Override
	public void delete(String nodeUid) {
		this.nodeRepo.deleteById(nodeUid);
	}

}
