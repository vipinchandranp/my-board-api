package com.myboard.userservice.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.myboard.userservice.entity.PlayAudit;

@Repository
public interface PlayAuditRepository extends MongoRepository<PlayAudit, String> {
	// Define custom query methods if needed
}
