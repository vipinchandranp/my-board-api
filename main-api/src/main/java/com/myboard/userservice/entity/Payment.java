package com.myboard.userservice.entity;

import com.myboard.userservice.types.StatusType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.persistence.Entity;
import javax.persistence.Id;
import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payment")
public class Payment extends Base {
    @Id
    private String transactionId;

    @DBRef(lazy = true)
    private Display display;

    @DBRef(lazy = true)
    private Board board;

    private double amount;
    private StatusType status;

    // Additional properties for payment details
    private String paymentMethod; // e.g., Credit Card, PayPal, etc.
    private LocalDateTime paymentDate; // Timestamp of the payment
    private String payerName; // Name of the person making the payment
    private String payerEmail; // Email of the payer for contact/reference
    private String currency; // Currency code (e.g., USD, EUR)
    private String receiptNumber; // Unique receipt number for tracking
    private LocalDateTime dueDate; // Payment due date, if applicable
    private String paymentDescription; // Description or notes about the payment
    private boolean isRefundable; // Indicates if the payment can be refunded
    private LocalDateTime refundDate; // Date of refund, if applicable
    private double refundAmount; // Amount refunded, if applicable
}
