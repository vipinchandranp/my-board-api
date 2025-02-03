package com.myboard.userservice.service;

import com.myboard.userservice.controller.model.payment.request.PaytmTransactionRequest;
import com.myboard.userservice.controller.model.payment.response.PaytmTransactionResponse;
import com.myboard.userservice.entity.PaymentDetails;
import com.myboard.userservice.entity.User;
import com.myboard.userservice.repository.PaymentDetailsRepository;
import com.myboard.userservice.repository.UserRepository;
import com.myboard.userservice.types.StatusType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class PaytmIntegrationService {

    @Value("${myboard.payment.paytm.merchant.key}")
    private String merchantKey;

    @Value("${myboard.payment.paytm.merchant.id}")
    private String merchantId;

    @Value("${myboard.payment.paytm.environment}")
    private String paytmEnvironment;
    @Autowired
    private PaymentDetailsRepository paymentDetailsRepository;
    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private UserRepository userRepository;

    public PaytmTransactionResponse initiatePayment(PaytmTransactionRequest transactionRequest) {
        // Paytm API endpoint for initiating payment
        String url = paytmEnvironment.equals("production") 
            ? "https://secure.paytm.in/oltp-web/processTransaction" 
            : "https://pguat.paytm.com/oltp-web/processTransaction";

        // Send a POST request to Paytm with the payment details
        PaytmTransactionResponse response = restTemplate.postForObject(url, transactionRequest, PaytmTransactionResponse.class);

        // If payment initiated successfully, store the payment details
        if (response != null && "TXN_SUCCESS".equals(response.getStatus())) {
            storePaymentDetails(response);
        }

        return response;
    }

    public PaytmTransactionResponse verifyPayment(String transactionId) {
        // Paytm API endpoint for verifying the payment status
        String url = paytmEnvironment.equals("production")
            ? "https://secure.paytm.in/oltp/HANDLER_INTERNAL/confirmTransaction" 
            : "https://pguat.paytm.com/oltp/HANDLER_INTERNAL/confirmTransaction";

        // Send GET request to Paytm with the transaction ID for verification
        PaytmTransactionResponse response = restTemplate.getForObject(
            url + "?transactionId=" + transactionId, 
            PaytmTransactionResponse.class
        );

        return response;
    }

    public PaytmTransactionResponse initiateRefund(String transactionId, double refundAmount) {
        // Paytm API endpoint for initiating refunds
        String url = paytmEnvironment.equals("production")
            ? "https://secure.paytm.in/oltp-web/transactionRefund" 
            : "https://pguat.paytm.com/oltp-web/transactionRefund";

        // Refund request object (you'll need to pass the appropriate details)
        PaytmTransactionRequest refundRequest = new PaytmTransactionRequest(transactionId, refundAmount, merchantKey);

        // Send POST request to Paytm to initiate a refund
        PaytmTransactionResponse response = restTemplate.postForObject(url, refundRequest, PaytmTransactionResponse.class);

        // Store refund details if the refund is successful
        if (response != null && "REFUND_SUCCESS".equals(response.getStatus())) {
            PaymentDetails paymentDetails = paymentDetailsRepository.findById(transactionId).orElse(null);
            if (paymentDetails != null) {
                paymentDetails.setRefunded(true);
                paymentDetails.setRefundTransactionId(response.getRefundTransactionId());
                paymentDetails.setRefundProcessedAt(response.getRefundProcessedAt());
                paymentDetails.setPaymentStatus(StatusType.PAYMENT_COMPLETED);
                paymentDetailsRepository.save(paymentDetails);
            }
        }

        return response;
    }

    public PaymentDetails storePaymentDetails(PaytmTransactionResponse paytmTransactionResponse) {
        User payer = userRepository.findById(paytmTransactionResponse.getPayerId()).orElse(null);
        User payee = userRepository.findById(paytmTransactionResponse.getPayeeId()).orElse(null);
        PaymentDetails paymentDetails = new PaymentDetails();
        paymentDetails.setTransactionId(paytmTransactionResponse.getTransactionId());
        paymentDetails.setPayer(payer);
        paymentDetails.setPayee(payee);
        paymentDetails.setAmount(paytmTransactionResponse.getAmount());
        paymentDetails.setCurrency(paytmTransactionResponse.getCurrency());
        paymentDetails.setPaymentInitiatedAt(paytmTransactionResponse.getPaymentInitiatedAt());
        paymentDetails.setStatus(StatusType.PAYMENT_INITIATED);  // Assuming payment is initiated
        return paymentDetailsRepository.save(paymentDetails);
    }
}
