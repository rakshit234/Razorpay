package com.project.razorpay.payment.service.impl;

import com.project.razorpay.common.enums.OrderStatus;
import com.project.razorpay.common.exceptions.DuplicateResourceException;
import com.project.razorpay.payment.dto.request.CreateOrderRequest;
import com.project.razorpay.payment.dto.response.OrderResponse;
import com.project.razorpay.payment.entity.OrderRecord;
import com.project.razorpay.payment.repository.OrderRepository;
import com.project.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultExpiryMinutes;

    @Override
    public OrderResponse create(UUID merchantId, CreateOrderRequest request) {
        if(request.receipt()!=null && orderRepository.existsByMerchantIdAndReceipt(merchantId, request.receipt())){
            throw new DuplicateResourceException("ORDER_RECEIPT_DUPLICATE","Order with receipt already exists:"+request.receipt());
        }
        OrderRecord orderRecord = OrderRecord.builder()
                .receipt(request.receipt())
                .amount(request.amount())
                .notes(request.notes())
                .merchantId(merchantId)
                .orderStatus(OrderStatus.CREATED)
                .expiresAt(request.expiresAt() != null ? request.expiresAt(): LocalDateTime.now().plusMinutes(defaultExpiryMinutes))
                .build();

        orderRepository.save(orderRecord);
        // TODO: send kafka event about order creation

        log.info(orderRecord.toString());

        return new OrderResponse(
                orderRecord.getId(),
                orderRecord.getMerchantId(),
                orderRecord.getReceipt(),
                orderRecord.getAmount(),
                orderRecord.getOrderStatus(),
                orderRecord.getAttempts(),
                orderRecord.getNotes(),
                orderRecord.getExpiresAt(),
                null
                );
    }
}
