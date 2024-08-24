package com.myboard.userservice.entity;

import java.util.ArrayList;
import java.util.List;

import com.myboard.userservice.types.StatusType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "board")
public class Board extends Base {

    private String name;

    private List<Rating> ratings = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

    private StatusType validationStatus = StatusType.WAITING_FOR_APPROVAL;

    private Media media;

    public Board(Media media) {
        this.media = media;
    }

}
