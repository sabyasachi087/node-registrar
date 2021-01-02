package com.sroyc.noderegistrar.repo.mongo;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MongoPrimaryNodeLockerRepository extends MongoRepository<MongoPrimaryNodeLockerEntity, Integer> {

}
