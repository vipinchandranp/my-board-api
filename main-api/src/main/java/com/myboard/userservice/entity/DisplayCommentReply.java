package com.myboard.userservice.entity;
import lombok.Data;

import javax.persistence.*;
@Data
@Entity
public class DisplayCommentReply {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private String username;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;

    // Constructors, getters, and setters

    // Constructors
    public DisplayCommentReply() {
        // Default constructor required by JPA
    }

    public DisplayCommentReply(String text, String username, java.util.Date date) {
        this.text = text;
        this.username = username;
        this.date = date;
    }

}
