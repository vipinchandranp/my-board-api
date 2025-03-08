package com.myboard.userservice.repository;

import com.myboard.userservice.entity.AuditLog;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface AuditLogRepository extends MongoRepository<AuditLog, String> {
}
