package com.myboard.userservice.entity;

import com.myboard.userservice.types.StatusType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = false)
@Data
@Document(collection = "play")
public class Play extends Base{

    @DBRef
    private Board board;

    @DBRef
    private Display display;

    private StatusType status;

}
