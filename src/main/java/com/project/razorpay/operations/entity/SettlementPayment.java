package com.project.razorpay.operations.entity;

import com.project.razorpay.common.entity.BaseEntity;
import jakarta.persistence.*;

@Entity
@Table(name = "settlement_payment")
public class SettlementPayment extends BaseEntity {

    @EmbeddedId
    private SettlementPaymentId id; //S1_P1

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("settlementId")
    @JoinColumn(name = "settlement_id")
    private Settlement settlement; //s1

    @ManyToOne(fetch = FetchType.LAZY)
    @MapsId("paymentId")
    @JoinColumn(name = "payment_id")
    private Settlement payment; //p1

}
