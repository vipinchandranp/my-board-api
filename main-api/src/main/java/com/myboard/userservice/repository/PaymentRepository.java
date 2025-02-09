package com.myboard.userservice.repository;

import com.myboard.userservice.controller.model.payment.request.PaymentRequest;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.Display;
import com.myboard.userservice.entity.Payment;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.types.StatusType;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

// The repository interface for interacting with the 'payment' collection in MongoDB
@Repository
public interface PaymentRepository extends MongoRepository<Payment, String> {

    // Find a payment by its transactionId
    Optional<Payment> findByTransactionId(String transactionId);

    // Find all payments by their status
    List<Payment> findByStatus(StatusType status);

    // Find all payments by the display associated with them
    List<Payment> findByDisplay(Display display);

    // Find all payments by the board associated with them
    List<Payment> findByBoard(Board board);

    // Find payments within a specific date range
    List<Payment> findByPaymentDateBetween(LocalDateTime startDate, LocalDateTime endDate);

    // Find payments by payer's email
    List<Payment> findByPayerEmail(String payerEmail);

    // Find payments by currency type
    List<Payment> findByCurrency(String currency);

    // Find payments that are refundable
    List<Payment> findByIsRefundable(boolean isRefundable);

    // Find payments with a due date before a specific date
    List<Payment> findByDueDateBefore(LocalDateTime date);

    // Process payment (example: integrating with a payment gateway)
    default boolean processPayment(PaymentRepository paymentRepository, DisplayRepository displayRepository, PaymentRequest request) throws MBException {
        try {
            // Fetch the display object
            Display display = displayRepository.findById(request.getDisplayId())
                    .orElseThrow(() -> new MBException("Display not found"));

            // Process payment (e.g., call payment gateway API and confirm payment)
            Payment payment = null;// TODO
            // Update payment status after processing
            payment.setStatus(StatusType.PAYMENT_COMPLETED);
            payment.setPaymentDate(LocalDateTime.now());
            paymentRepository.save(payment);

            return true;  // Return true if payment is processed successfully
        } catch (Exception e) {
            throw new MBException("Payment processing failed", e);
        }
    }
}
