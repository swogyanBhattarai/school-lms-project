package com.justdeepfried.GyanJyotiLMS.entities.studentFee.model;

import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.enums.PAYMENT_TYPE;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.math.BigDecimal;

@Entity
@Setter
@Getter
@Filter(name = "schoolIdFilter", condition = "school_id = :schoolId")
public class FeePaymentModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long feePaymentId;

    private BigDecimal amountPaid;

    private String paidBy;

    private String phoneNumber;

    @Enumerated(EnumType.STRING)
    private PAYMENT_TYPE paymentType;

    @ManyToOne
    @JoinColumn(name = "student_fee_id")
    private StudentFeeModel studentFee;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
