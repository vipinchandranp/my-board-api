package com.myboard.userservice.repository;

import com.myboard.userservice.entity.PaymentDetails;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface PaymentDetailsRepository extends MongoRepository<PaymentDetails, String> {

    // Retrieve payments by payerId
    List<PaymentDetails> findByPayerId(String payerId);

    // Retrieve payments by payeeId
    List<PaymentDetails> findByPayeeId(String payeeId);

    // Retrieve payments by status
    List<PaymentDetails> findByStatus(String status);

    // Retrieve payments within a date range
    List<PaymentDetails> findByPaymentInitiatedAtBetween(
            LocalDateTime startDate, LocalDateTime endDate
    );
}
