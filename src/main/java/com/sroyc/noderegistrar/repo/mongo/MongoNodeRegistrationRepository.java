package com.sroyc.noderegistrar.repo.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoNodeRegistrationRepository extends MongoRepository<MongoNodeRegistrationEntity, String> {

}
