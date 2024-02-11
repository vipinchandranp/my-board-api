package com.myboard.userservice.entity;
import javax.persistence.*;
import java.util.List;

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

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public String getText() {
        return text;
    }

    public String getUsername() {
        return username;
    }

    public java.util.Date getDate() {
        return date;
    }

    public List<BoardCommentReply> getReplies() {
        return replies;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setText(String text) {
        this.text = text;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setDate(java.util.Date date) {
        this.date = date;
    }

    public void setReplies(List<BoardCommentReply> replies) {
        this.replies = replies;
    }
}
