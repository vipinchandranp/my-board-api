package com.myboard.userservice.controller.model.payment.response;

import com.myboard.userservice.types.StatusType;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PaymentResponse {
    private String transactionId;  // The unique identifier for the payment transaction
    private StatusType status;     // The status of the payment (e.g., PAYMENT_INITIATED, PAYMENT_COMPLETED, etc.)

    // Additional fields for payment details
    private double amount;                // The amount of the payment
    private String paymentMethod;         // e.g., Credit Card, PayPal, etc.
    private LocalDateTime paymentDate;    // Timestamp of the payment
    private String payerName;             // Name of the person making the payment
    private String payerEmail;            // Email of the payer
    private String currency;              // Currency code (e.g., USD, EUR)
    private String receiptNumber;         // Unique receipt number for tracking
    private LocalDateTime dueDate;        // Payment due date, if applicable
    private String paymentDescription;    // Description or notes about the payment
    private boolean isRefundable;         // Indicates if the payment can be refunded
    private LocalDateTime refundDate;     // Date of refund, if applicable
    private double refundAmount;          // Amount refunded, if applicable
}
