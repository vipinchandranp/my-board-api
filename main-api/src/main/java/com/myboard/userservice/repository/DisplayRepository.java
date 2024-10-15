package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.myboard.userservice.entity.Display;

import java.util.List;
import java.util.Optional;

public interface DisplayRepository extends MongoRepository<Display, String> {

    boolean existsByName(String name);

    List<Display> findByCreatedBy(User user);

    Optional<Display> findByName(String dipsplayName);

    Page<Display> findAll(Pageable pageable);

}