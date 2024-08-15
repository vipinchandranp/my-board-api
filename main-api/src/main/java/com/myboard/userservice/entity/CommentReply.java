package com.myboard.userservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "comment_reply")
public class CommentReply extends Base {

    private String reply;

    @DBRef
    private User repliedBy;

}
