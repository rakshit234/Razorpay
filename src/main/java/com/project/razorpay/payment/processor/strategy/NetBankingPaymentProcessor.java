package com.project.razorpay.payment.processor.strategy;

import com.project.razorpay.payment.processor.PaymentProcessor;
import com.project.razorpay.payment.processor.dto.PaymentProcessorRequest;
import com.project.razorpay.payment.processor.dto.PaymentProcessorResponse;

public class NetBankingPaymentProcessor implements PaymentProcessor {
    @Override
    public PaymentProcessorResponse charge(PaymentProcessorRequest request) {
        return null;
    }
}
