package com.project.razorpay.merchant.service;

import com.project.razorpay.merchant.dto.request.MerchantSignUpRequest;
import com.project.razorpay.merchant.dto.response.MerchantResponse;
import jakarta.validation.Valid;
import org.jspecify.annotations.Nullable;

public interface AuthService {
    @Nullable MerchantResponse signup(@Valid MerchantSignUpRequest request);
}
