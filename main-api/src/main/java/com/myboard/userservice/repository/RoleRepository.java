package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface RoleRepository extends MongoRepository<Role, Long> {
    Optional<Role> findByName(String name);
}