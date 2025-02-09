package com.myboard.userservice.entity;

import com.myboard.userservice.types.ItemType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Document(collection = "comment")
public class Comment extends Base{
    private String content;
    private String commentedBy; // userID
    private ItemType itemType;
    private String itemID; // Display or BoardID
}
