package com.justdeepfried.GyanJyotiLMS.entities.studentFee.dtos;

import com.justdeepfried.GyanJyotiLMS.enums.PAYMENT_TYPE;

import java.math.BigDecimal;

public record FeePaymentCreate (
    BigDecimal amountPaid,
    String paidBy,
    String phoneNumber,
    PAYMENT_TYPE paymentType
) {
}
