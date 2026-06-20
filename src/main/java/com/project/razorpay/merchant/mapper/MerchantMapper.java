package com.project.razorpay.merchant.mapper;

import com.project.razorpay.merchant.dto.request.MerchantSignUpRequest;
import com.project.razorpay.merchant.dto.response.MerchantResponse;
import com.project.razorpay.merchant.entity.Merchant;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface MerchantMapper {
    Merchant toEntityFromSignUpRequest(MerchantSignUpRequest request);
    MerchantResponse toResponse(Merchant merchant);
}
