package com.myboard.userservice.controller;

import com.myboard.userservice.controller.model.common.MainResponse;
import com.myboard.userservice.controller.model.payment.request.PaymentRequest;
import com.myboard.userservice.controller.model.payment.request.PriceCalculationRequest;
import com.myboard.userservice.controller.model.payment.response.PaymentResponse;
import com.myboard.userservice.exception.MBException;
import com.myboard.userservice.service.PaymentService;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@CrossOrigin
@RequestMapping("/payment")
public class PaymentController extends BaseController {

    @Autowired
    private PaymentService paymentService;

    // Endpoint to initiate a payment
    @PostMapping("/initiate")
    public MainResponse<String> initiatePayment(@RequestBody PaymentRequest request) throws MBException {
        String txId = paymentService.initiatePayment(request);
        return buildResponse(txId);
    }

    // Endpoint to check the status of a payment
    @GetMapping("/status/{transactionId}")
    public MainResponse<PaymentResponse> getPaymentStatus(@PathVariable String transactionId) throws MBException {
        PaymentResponse response = paymentService.getPaymentStatus(transactionId);
        return buildResponse(response);
    }

    // Endpoint to process the payment and charge the user
    @PostMapping("/process")
    public MainResponse<String> processPayment(@RequestBody PaymentRequest request) throws MBException {
        boolean paymentProcessed = paymentService.processPayment(request);
        if (paymentProcessed) {
            return buildResponse("Payment processed successfully");
        } else {
            throw new MBException("Payment processing failed");
        }
    }

    // New endpoint to update payment status (e.g., if admin needs to mark as successful)
    @PutMapping("/update-status/{transactionId}")
    public MainResponse<String> updatePaymentStatus(@PathVariable String transactionId, @RequestParam StatusType newStatus) throws MBException {
        paymentService.updatePaymentStatus(transactionId, newStatus);
        return buildResponse("Payment status updated successfully");
    }
    @PostMapping("/calculate-price")
    public MainResponse<Double> calculatePrice(@RequestBody PriceCalculationRequest request) throws MBException {
        try {
            double calculatedPrice = paymentService.calculatePrice(request.getDisplayId(), request.getTimeSlots(), request.getDate());
            return buildResponse(calculatedPrice);
        } catch (Exception e) {
            throw new MBException("Error calculating price: " + e.getMessage());
        }
    }

    @GetMapping("/is-completed/{transactionId}")
    public MainResponse<Boolean> isPaymentCompleted(@PathVariable String transactionId) throws MBException {
        try {
            boolean isCompleted = paymentService.isPaymentCompleted(transactionId);
            return buildResponse(isCompleted);
        } catch (Exception e) {
            throw new MBException("Error checking payment status: " + e.getMessage());
        }
    }
}
