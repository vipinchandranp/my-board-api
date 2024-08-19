package com.myboard.userservice.entity;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.Id;

@Data
public class Base {

    @Id
    private Long id;

    private boolean active = true;

    private List<Rating> ratings = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

    boolean validated = false;

    private Media media;

    public void Base(){

    }

    public Base(Media media){
        this.media = media;
    }

    @CreatedDate
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime lastModifiedAt;

    @DBRef
    @CreatedBy
    private User createdBy;

    @DBRef
    @LastModifiedBy
    private User modifiedBy;

}
