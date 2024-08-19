package com.myboard.userservice.entity;

import com.myboard.userservice.types.MediaType;
import lombok.Builder;
import lombok.Data;
@Data
@Builder
public class Media {

    private MediaType mediaType;

    private String mediaLocation;

    private String mediaName;

    private byte[] mediaContent;

}
