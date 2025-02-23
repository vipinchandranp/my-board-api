package com.myboard.userservice.controller.model.payment.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class PaymentRequest {

    private String displayId;        // ID of the display associated with the payment
    private String boardId;          // ID of the board associated with the payment
    private double amount;           // Amount to be paid

    // Additional fields for payment details
    private String paymentMethod;    // e.g., Credit Card, PayPal, etc.
    private String payerName;        // Name of the payer
    private String payerEmail;       // Email of the payer
    private String currency;         // Currency code (e.g., USD, EUR)
    private String description;      // Description or notes about the payment
    private boolean isRefundable;    // Indicates if the payment is refundable
    private LocalDateTime dueDate;   // Payment due date, if applicable
    private String transactionID;    // Unique identifier for the transaction

    private List<String> timeSlots;  // List of selected time slots (optional)
    private LocalDateTime date;      // The selected date for the payment (optional)

}
