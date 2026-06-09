package com.justdeepfried.GyanJyotiLMS.entities.studentFee.repository;

import com.justdeepfried.GyanJyotiLMS.entities.studentFee.model.StudentFeeModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface StudentFeeRepo extends JpaRepository<StudentFeeModel, Long> {
    @Query("""
    SELECT DISTINCT sf
    FROM StudentFeeModel sf
    LEFT JOIN FETCH sf.feePayments fp
    WHERE sf.student.studentId = :studentId
    """)
    List<StudentFeeModel> findAllByStudentId(@Param("studentId") Long studentId);
}
