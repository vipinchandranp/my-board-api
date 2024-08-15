package com.myboard.userservice.repository;

import com.myboard.userservice.entity.CommentReply;
import com.myboard.userservice.entity.Content;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface CommentReplyRepository extends MongoRepository<CommentReply, Long> {
}