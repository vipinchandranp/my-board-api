package com.myboard.userservice.types;

public enum MediaType {
    VIDEO("VIDEO"),
    TEXT("TEXT"),
    PICTURE("PICTURE");


    private final String value;

    MediaType(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return this.value;
    }
}
