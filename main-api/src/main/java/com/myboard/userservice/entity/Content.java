package com.myboard.userservice.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "content")
public class Content extends Base{

    boolean validated;

    private Media media;
}