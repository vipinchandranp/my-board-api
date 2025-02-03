package com.myboard.userservice.entity;

import com.myboard.userservice.controller.model.common.MediaFile;
import com.myboard.userservice.types.StatusType;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "payment_details")
public class PaymentDetails extends Base {

    private StatusType status = StatusType.WAITING_FOR_APPROVAL;

    // The user making the payment
    private User payer;

    // The user or entity receiving the payment
    private User payee;

    // The payment method used (e.g., UPI, Credit Card, etc.)
    private String paymentMethod;

    // The unique transaction ID from the payment gateway
    private String transactionId;

    // The amount of the transaction
    private double amount;

    // The currency of the payment
    private String currency;

    // Any applicable tax or fee
    private double taxAmount;

    // Any applicable tax or fee
    private double platformFees;

    // The total amount (amount + tax or fee)
    private double totalAmount;

    // Timestamp for when the payment was initiated
    private LocalDateTime paymentInitiatedAt;

    // Timestamp for when the payment was completed
    private LocalDateTime paymentCompletedAt;

    // Timestamp for when the payment failed (if applicable)
    private LocalDateTime paymentFailedAt;

    // Description or reason for the payment
    private String description;

    // Error or failure reason (if applicable)
    private String failureReason;

    // Any associated metadata or additional information
    private String metadata;

    // The media file or content related to this payment
    private MediaFile associatedMedia;

    // A flag to indicate if this payment has been refunded
    private boolean refunded;

    // Refund details if a refund has been processed
    private String refundTransactionId;

    // Timestamp for when the refund was processed
    private LocalDateTime refundProcessedAt;

    // Status for the refund process
    private StatusType paymentStatus;

}
