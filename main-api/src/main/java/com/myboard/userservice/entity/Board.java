package com.myboard.userservice.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "board")
public class Board extends Base {


    public Board(Media media) {
        super(media);
    }
}
