package com.myboard.userservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "comment")
public class Comment extends Base{

    private String comment;

    private List<CommentReply> replies = new ArrayList<>();

}
