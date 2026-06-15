package com.project.razorpay.common.enums;

public enum PaymentEvent {
    AUTHORIZE_ATTEMPT,
    AUTHORIZE_SUCCESS,
    AUTHORIZE_FAIL,
    CAPTURE_REQUEST,
    CAPTURE_SUCCESS,
    CAPTURE_FAIL,
    CAPTURE_TIMEOUT,
    REFUND_INIT,
    REFUND_COMPLETE,
    SETTLE,
    CANCEL
}
