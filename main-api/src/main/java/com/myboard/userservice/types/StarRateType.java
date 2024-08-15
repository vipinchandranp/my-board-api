package com.myboard.userservice.types;

public enum StarRateType {

    ONE(1),
    TWO(2),
    THREE(3),
    FOUR(4),
    FIVE(5);

    private final Integer value;

    StarRateType(Integer value) {
        this.value = value;
    }

    public Integer getValue() {
        return value;
    }

}
