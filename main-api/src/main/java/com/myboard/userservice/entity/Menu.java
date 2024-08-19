package com.myboard.userservice.entity;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import lombok.Data;

import java.util.Set;
@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Document(collection = "menu")
public class Menu extends Base {

    private String url;

    private String icon;

    @DBRef
    private Menu menu;

    @DBRef
    private Set<Role> roles;

}
