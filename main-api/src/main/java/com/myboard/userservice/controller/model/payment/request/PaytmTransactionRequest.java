package com.myboard.userservice.controller.model.payment.request;

import lombok.Data;

@Data
public class PaytmTransactionRequest {

    private String transactionId;
    private double amount;
    private String payerId;
    private String payeeId;
    private String merchantKey;

    public PaytmTransactionRequest(String transactionId, double amount, String merchantKey) {
        this.transactionId = transactionId;
        this.amount = amount;
        this.merchantKey = merchantKey;
    }
}
