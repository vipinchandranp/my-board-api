package com.myboard.userservice.controller.model.payment.response;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class PaytmTransactionResponse {

    private String transactionId;
    private String status;
    private String payerId;
    private String payeeId;
    private double amount;
    private String currency;
    private LocalDateTime paymentInitiatedAt;
    private String refundTransactionId;
    private LocalDateTime refundProcessedAt;
    private String refundStatus;
}
