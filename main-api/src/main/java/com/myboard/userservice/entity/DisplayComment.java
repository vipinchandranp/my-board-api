package com.myboard.userservice.entity;
import lombok.Data;

import javax.persistence.*;
import java.util.List;
@Data
@Entity
public class DisplayComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String text;
    private String username;

    @Temporal(TemporalType.TIMESTAMP)
    private java.util.Date date;

    @OneToMany(cascade = CascadeType.ALL)
    private List<BoardCommentReply> replies;

    // Constructors, getters, and setters

    // Constructors
    public DisplayComment() {
        // Default constructor required by JPA
    }

    public DisplayComment(String text, String username, java.util.Date date, List<BoardCommentReply> replies) {
        this.text = text;
        this.username = username;
        this.date = date;
        this.replies = replies;
    }

}
