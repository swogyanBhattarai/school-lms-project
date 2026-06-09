package com.justdeepfried.GyanJyotiLMS.entities.studentFee.dtos;


import com.justdeepfried.GyanJyotiLMS.enums.FEE_TYPES;

import java.math.BigDecimal;

public record StudentFeeCreate (
        BigDecimal originalAmount,
        BigDecimal discountPercentage,
        FEE_TYPES feeType
) {
}
