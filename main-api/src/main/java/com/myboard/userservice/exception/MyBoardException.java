package com.myboard.userservice.exception;

import com.myboard.userservice.controller.apimodel.MyBoardWorkFlow;
import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class MyBoardException extends RuntimeException {

    private String message;
    public MyBoardException(Exception ex) {
        super(ex);
    }


    public MyBoardException(String message) {
        super(message);
        this.message = message;
    }

    public MyBoardException(Exception ex, String message) {
        super(message, ex);
    }

    public MyBoardException() {

    }
}
