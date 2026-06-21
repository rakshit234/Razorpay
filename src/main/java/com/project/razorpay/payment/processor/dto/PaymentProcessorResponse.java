package com.project.razorpay.payment.processor.dto;

import com.project.razorpay.common.entity.Money;
import com.project.razorpay.common.enums.PaymentMethod;

import java.util.Map;

public sealed interface PaymentProcessorResponse permits
        PaymentProcessorResponse.Pending,
        PaymentProcessorResponse.Failure,
        PaymentProcessorResponse.Success {
    record Pending(String processorReference) implements PaymentProcessorResponse {}
    record Success(String processorReference, String bankReference) implements PaymentProcessorResponse {}
    record Failure(String errorCode, String errorDescription) implements PaymentProcessorResponse {}
}
