package com.myboard.userservice.entity;

import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "role")
public class Role extends Base {

    private String name;

    public Role(String name) {
        this.name = name;
    }

}
