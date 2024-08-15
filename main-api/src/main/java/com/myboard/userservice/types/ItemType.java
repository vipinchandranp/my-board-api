package com.myboard.userservice.types;

public enum ItemType {

    BOARD("BOARD"),

    DISPLAY("DISPLAY");

    private final String value;

    ItemType(String value) {
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
