package com.myboard.userservice.entity;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "board")
public class Board extends Base {

    private String name;

    private List<Rating> ratings = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

    private Content content;
}
