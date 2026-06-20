package com.project.razorpay.payment.service.impl;

import com.project.razorpay.common.enums.OrderStatus;
import com.project.razorpay.common.exceptions.BusinessRuleViolationException;
import com.project.razorpay.common.exceptions.DuplicateResourceException;
import com.project.razorpay.common.exceptions.ResourceNotFoundException;
import com.project.razorpay.payment.dto.request.CreateOrderRequest;
import com.project.razorpay.payment.dto.response.OrderMapper;
import com.project.razorpay.payment.dto.response.OrderResponse;
import com.project.razorpay.payment.dto.response.PaymentResponse;
import com.project.razorpay.payment.entity.OrderRecord;
import com.project.razorpay.payment.entity.Payment;
import com.project.razorpay.payment.mapper.PaymentMapper;
import com.project.razorpay.payment.repository.OrderRepository;
import com.project.razorpay.payment.repository.PaymentRepository;
import com.project.razorpay.payment.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final PaymentRepository paymentRepository;
    private final PaymentMapper paymentMapper;
    private final OrderMapper orderMapper;

    @Value("${payment.order.default-order-expiry-minutes:30}")
    private int defaultExpiryMinutes;

    @Override
    @Transactional
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

//        return new OrderResponse(
//                orderRecord.getId(),
//                orderRecord.getMerchantId(),
//                orderRecord.getReceipt(),
//                orderRecord.getAmount(),
//                orderRecord.getOrderStatus(),
//                orderRecord.getAttempts(),
//                orderRecord.getNotes(),
//                orderRecord.getExpiresAt(),
//                null
//                );

        return orderMapper.toResponse(orderRecord);
    }

    @Override
    public OrderResponse getById(UUID merchantId, UUID orderId) {
        // passing merchantId as we don't want a merchant to see other merchants' orders.
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId,merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("Order",orderId));
        return orderMapper.toResponse(order);
    }

    @Override
    @Transactional
    public OrderResponse cancel(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId,merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("Order",orderId));

        if(order.getOrderStatus()== OrderStatus.CANCELLED || order.getOrderStatus()== OrderStatus.PAID){
            throw new BusinessRuleViolationException("ORDER_CANNOT_BE_CANCELLED",
                    "Cannot cancel order with status: "+order.getOrderStatus().name());
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        order = orderRepository.save(order);
        return orderMapper.toResponse(order);
    }

    @Override
    public List<PaymentResponse> listPayments(UUID merchantId, UUID orderId) {
        OrderRecord order = orderRepository.findByIdAndMerchantId(orderId,merchantId)
                .orElseThrow(()-> new ResourceNotFoundException("Order",orderId));

        List<Payment> paymentList = paymentRepository.findByOrder_Id(order);

//        return paymentList.stream()
//                .map(payment -> paymentMapper.toResponse(payment))
//                .collect(Collectors.toList());

        return paymentMapper.toResponseList(paymentList);

    }
}
