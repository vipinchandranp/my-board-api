package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Reply;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentReplyRepository extends MongoRepository<Reply, String> {
}