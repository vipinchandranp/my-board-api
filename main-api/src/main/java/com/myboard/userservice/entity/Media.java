package com.myboard.userservice.entity;

import com.myboard.userservice.types.MediaType;
import lombok.Data;
@Data
public class Media {

    private MediaType mediaType;

    private String mediaLocation;

    private String mediaName;

    private byte[] mediaContent;
}
