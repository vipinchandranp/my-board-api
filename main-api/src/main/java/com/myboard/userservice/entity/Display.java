package com.myboard.userservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;


@Document(collection = "display")
public class Display extends Base {
    private Availability availability;
    private Location location;
    public Display(Media media) {
        super(media);
    }
}
