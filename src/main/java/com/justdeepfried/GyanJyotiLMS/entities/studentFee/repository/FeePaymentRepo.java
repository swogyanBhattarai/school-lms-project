package com.justdeepfried.GyanJyotiLMS.entities.studentFee.repository;

import com.justdeepfried.GyanJyotiLMS.entities.studentFee.model.FeePaymentModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;

@Repository
public interface FeePaymentRepo extends JpaRepository<FeePaymentModel, Long> {
    @Query("""
    SELECT COALESCE(SUM(fp.amountPaid), 0)
    FROM FeePaymentModel fp
    WHERE fp.studentFee.studentFeeId = :studentFeeId
    """)
    BigDecimal sumTotalPaymentByStudentFeeId(@Param("studentFeeId") Long studentFeeId);
}
