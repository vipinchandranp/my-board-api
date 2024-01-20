package com.myboard.userservice.entity;
import javax.persistence.*;

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
}
