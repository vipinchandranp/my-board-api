package com.myboard.userservice.service;

import com.myboard.userservice.entity.PaymentDetails;
import com.myboard.userservice.repository.PaymentDetailsRepository;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class PaymentService {

    private final PaymentDetailsRepository paymentDetailsRepository;

    @Autowired
    public PaymentService(PaymentDetailsRepository paymentDetailsRepository) {
        this.paymentDetailsRepository = paymentDetailsRepository;
    }

    // Store or update payment details
    public PaymentDetails savePaymentDetails(PaymentDetails paymentDetails) {
        return paymentDetailsRepository.save(paymentDetails);
    }

    // Retrieve payment by ID
    public Optional<PaymentDetails> getPaymentById(String paymentId) {
        return paymentDetailsRepository.findById(paymentId);
    }

    // Retrieve payments by payerId
    public List<PaymentDetails> getPaymentsByPayer(String payerId) {
        return paymentDetailsRepository.findByPayerId(payerId);
    }

    // Retrieve payments by payeeId
    public List<PaymentDetails> getPaymentsByPayee(String payeeId) {
        return paymentDetailsRepository.findByPayeeId(payeeId);
    }

    // Retrieve payments by status
    public List<PaymentDetails> getPaymentsByStatus(StatusType status) {
        return paymentDetailsRepository.findByStatus(status.getValue());
    }

    // Retrieve payments within a date range
    public List<PaymentDetails> getPaymentsWithinDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return paymentDetailsRepository.findByPaymentInitiatedAtBetween(startDate, endDate);
    }

    // Delete a payment by ID
    public void deletePaymentById(String paymentId) {
        paymentDetailsRepository.deleteById(paymentId);
    }
}
