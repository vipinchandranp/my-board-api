package com.myboard.userservice.entity;

import com.myboard.userservice.types.MediaType;
import lombok.Data;
@Data
public class Media {

    private MediaType mediaType;

    private String mediaLocation;

    private String mediaName;

    private byte[] mediaContent;

    public Media mediaType(MediaType mediaType) {
        this.mediaType = mediaType;
        return this;
    }

    public Media mediaLocation(String mediaLocation) {
        this.mediaLocation = mediaLocation;
        return this;
    }

    public Media mediaName(String mediaName) {
        this.mediaName = mediaName;
        return this;
    }

    public Media mediaContent(byte[] mediaContent) {
        this.mediaContent = mediaContent;
        return this;
    }

    public static Media builder() {
        return new Media();
    }

    public Media build() {
        return this;
    }

}
