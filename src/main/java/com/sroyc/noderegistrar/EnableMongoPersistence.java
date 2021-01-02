package com.sroyc.noderegistrar;

import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@Profile("sroyc.data.mongo")
@Configuration
@EnableMongoRepositories(basePackages = "com.sroyc.noderegistrar.repo.mongo")
public class EnableMongoPersistence {

}
