package com.myboard.userservice.entity;

import java.time.LocalDateTime;

import lombok.Data;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.mongodb.core.mapping.DBRef;

import javax.persistence.Id;

@Data
public abstract class Base {

    @Id
    private String id;

    private boolean active = true;

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
