package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.payment.request.PaymentRequest;
import com.myboard.userservice.controller.model.payment.response.PaymentResponse;
import com.myboard.userservice.entity.Board;
import com.myboard.userservice.entity.Display;
import com.myboard.userservice.entity.Payment;
import com.myboard.userservice.entity.Timeslot;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.repository.BoardRepository;
import com.myboard.userservice.repository.DisplayRepository;
import com.myboard.userservice.repository.PaymentRepository;
import com.myboard.userservice.repository.TimeslotRepository;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class PaymentService {

    @Autowired
    private DisplayRepository displayRepository;

    @Autowired
    private BoardRepository boardRepository;

    @Autowired
    private PaymentRepository paymentRepository;

    @Autowired
    private TimeslotRepository timeslotRepository;

    // Initiate a payment (store payment request in the database, could be in a pending state)
    public String initiatePayment(PaymentRequest request) throws MBException {
        try {
            // Get the display object by ID
            Display display = displayRepository.findById(request.getDisplayId())
                    .orElseThrow(() -> new MBException("Display not found"));

            // Generate the transaction ID
            String transactionId = generateTransactionId();

            // Create and save the payment as pending
            Payment payment = Payment.builder()
                    .transactionId(transactionId)
                    .display(display)
                    .amount(request.getAmount())
                    .paymentMethod(request.getPaymentMethod())  // Set payment method
                    .payerName(request.getPayerName())         // Set payer name
                    .payerEmail(request.getPayerEmail())       // Set payer email
                    .currency(request.getCurrency())           // Set currency
                    .paymentDescription(request.getDescription()) // Set description
                    .paymentDate(LocalDateTime.now())          // Set current timestamp
                    .status(StatusType.PAYMENT_INITIATED)      // Set status to initiated
                    .isRefundable(request.isRefundable())      // Set refundable status
                    .dueDate(request.getDueDate())             // Set due date if provided
                    .build();

            paymentRepository.save(payment);

            // Return the transaction ID
            return transactionId;
        } catch (Exception e) {
            throw new MBException("Error initiating payment", e);
        }
    }


    // Get the status of a payment
    public PaymentResponse getPaymentStatus(String transactionId) throws MBException {
        // Fetch payment by transaction ID
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new MBException("Payment not found"));

        // Return the payment status in the response
        return new PaymentResponse(
                payment.getTransactionId(),
                payment.getStatus(),
                payment.getAmount(),
                payment.getPaymentMethod(),
                payment.getPaymentDate(),
                payment.getPayerName(),
                payment.getPayerEmail(),
                payment.getCurrency(),
                payment.getReceiptNumber(),
                payment.getDueDate(),
                payment.getPaymentDescription(),
                payment.isRefundable(),
                payment.getRefundDate(),
                payment.getRefundAmount()
        );
    }

    // Process payment (example: integrating with a payment gateway)
    public boolean processPayment(PaymentRequest request) throws MBException {
        try {
            // Fetch the display object
            Display display = displayRepository.findById(request.getDisplayId())
                    .orElseThrow(() -> new MBException("Display not found"));

            Board board = boardRepository.findById(request.getBoardId())
                    .orElseThrow(() -> new MBException("Board not found"));

            // Validate the payment amount (example: match display price)
            if (request.getAmount() != display.getPrice()) {
                // Optionally, you can throw an exception here if the amount does not match the display's price
                // throw new MBException("Payment amount does not match the display price");
            }

            // Simulate a call to a payment gateway API
            boolean isPaymentSuccessful = simulatePaymentGateway(request);

            if (!isPaymentSuccessful) {
                throw new MBException("Payment failed during gateway processing");
            }

            // Create the payment record
            Payment payment = new Payment();
            payment.setTransactionId(request.getTransactionID() != null ? request.getTransactionID() : generateTransactionId()); // Use existing or generate new
            payment.setDisplay(display);
            payment.setBoard(board);
            payment.setAmount(request.getAmount());
            payment.setCurrency(request.getCurrency());
            payment.setPayerName(request.getPayerName());
            payment.setPayerEmail(request.getPayerEmail());
            payment.setStatus(StatusType.PAYMENT_COMPLETED);
            payment.setPaymentDate(LocalDateTime.now());
            payment.setPaymentMethod(request.getPaymentMethod());
            payment.setRefundable(request.isRefundable());

            // If date is provided, set it in the payment
            if (request.getDate() != null) {
                payment.setPaymentDate(request.getDate());  // Use the selected date
            }

            // Save the payment record
            paymentRepository.save(payment);

            // After successful payment, always save the timeslot details
            if (request.getTimeSlots() != null && !request.getTimeSlots().isEmpty()) {
                for (String timeSlotRange : request.getTimeSlots()) {
                    // Parse the timeslot range (e.g., "00:00 - 00:59")
                    String[] timeslotParts = timeSlotRange.split(" - ");
                    if (timeslotParts.length == 2) {
                        // Parse the start and end times
                        String startTimeStr = timeslotParts[0];
                        String endTimeStr = timeslotParts[1];

                        // If date is provided, set the timeslot date to the selected date
                        LocalDateTime startTime = request.getDate() != null
                                ? request.getDate().withHour(Integer.parseInt(startTimeStr.split(":")[0]))
                                .withMinute(Integer.parseInt(startTimeStr.split(":")[1]))
                                : LocalDateTime.parse(startTimeStr, DateTimeFormatter.ofPattern("HH:mm"));

                        LocalDateTime endTime = request.getDate() != null
                                ? request.getDate().withHour(Integer.parseInt(endTimeStr.split(":")[0]))
                                .withMinute(Integer.parseInt(endTimeStr.split(":")[1]))
                                : LocalDateTime.parse(endTimeStr, DateTimeFormatter.ofPattern("HH:mm"));

                        // If the end time is before the start time, adjust the end time to the next hour
                        if (endTime.isBefore(startTime)) {
                            endTime = endTime.plus(1, ChronoUnit.HOURS); // Adjust end time by 1 hour
                        }

                        // Create and save new Timeslot entity for each timeslot
                        Timeslot timeslot = new Timeslot();
                        timeslot.setDisplay(display); // Associate the timeslot with the display
                        timeslot.setBoard(board);     // Associate the timeslot with the board
                        timeslot.setPayment(payment); // Associate the timeslot with the payment
                        timeslot.setStartTime(startTime); // Set the start time of the timeslot
                        timeslot.setEndTime(endTime);   // Set the end time of the timeslot
                        timeslot.setStatus(StatusType.PAYMENT_COMPLETED);
                        // Save the new timeslot record
                        timeslotRepository.save(timeslot);
                    }
                }
            }

            return true; // Payment processed successfully
        } catch (Exception e) {
            throw new MBException("Payment processing failed", e);
        }
    }

    // Simulated payment gateway API call (replace with actual integration)
    private boolean simulatePaymentGateway(PaymentRequest request) {
        // Simulate success for now
        return true;
    }

    // Update the status of a payment (e.g., from 'pending' to 'completed' by admin or backend process)
    public void updatePaymentStatus(String transactionId, StatusType newStatus) throws MBException {
        // Fetch the payment by transaction ID
        Payment payment = paymentRepository.findByTransactionId(transactionId)
                .orElseThrow(() -> new MBException("Payment not found"));

        // Update the payment status
        payment.setStatus(newStatus);
        paymentRepository.save(payment);
    }

    // Method to calculate the price based on displayId, selected time slots, and date
    public double calculatePrice(String displayId, List<String> timeSlots, String date) {
        // Logic to calculate the price (e.g., based on the display and selected time)
        double price = 0.0;

        // Example logic: for each time slot, add a fixed amount to the price
        for (String timeSlot : timeSlots) {
            price += 10.0;  // Assume each time slot costs $10
        }

        // If there is any date-specific pricing logic, you can add it here

        return price;
    }

    // Helper method to generate a unique transaction ID (replace with your implementation)
    private String generateTransactionId() {
        return "TXN" + System.currentTimeMillis();  // Simple example; could be replaced with UUID
    }

    // Check if payment is successfully completed
    public boolean isPaymentCompleted(String transactionId) throws MBException {
        try {
            // Fetch the payment by transaction ID
            Payment payment = paymentRepository.findByTransactionId(transactionId)
                    .orElseThrow(() -> new MBException("Payment not found"));

            // Check if the status is 'PAYMENT_COMPLETED'
            return payment.getStatus() == StatusType.PAYMENT_COMPLETED;
        } catch (Exception e) {
            throw new MBException("Error checking payment status", e);
        }
    }

}
