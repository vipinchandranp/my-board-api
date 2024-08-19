package com.myboard.userservice.exception;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MBException extends RuntimeException {

    private String message;
    public MBException(Exception ex) {
        super(ex);
    }


    public MBException(String message) {
        super(message);
        this.message = message;
    }

    public MBException(Exception ex, String message) {
        super(message, ex);
    }

    public MBException() {

    }
}
