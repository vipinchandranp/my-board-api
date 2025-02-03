package com.myboard.userservice.types;

public enum StatusType {
    UNAVAILABLE("UNAVAILABLE"),
    AVAILABLE("AVAILABLE"),
    WAITING_FOR_APPROVAL("WAITING_FOR_APPROVAL"),
    APPROVED("APPROVED"),
    REJECTED("REJECTED"),
    WAITING_FOR_VALIDATION("WAITING_FOR_VALIDATION"),
    VALIDATED("VALIDATED"),
    WAITING_FOR_DISPLAY("WAITING_FOR_DISPLAY"),
    DISPLAYED("DISPLAYED"),
    TRANSACTION_COMPLETE("TRANSACTION_COMPLETE"),

    PAYMENT_COMPLETED("PAYMENT_COMPLETED"),
    PAYMENT_FAILED("PAYMENT_FAILED"),
    PAYMENT_IN_PROGRESS("PAYMENT_IN_PROGRESS"),
    PAYMENT_INITIATED("PAYMENT_INITIATED");

    private final String value;

    StatusType(String value) {
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
