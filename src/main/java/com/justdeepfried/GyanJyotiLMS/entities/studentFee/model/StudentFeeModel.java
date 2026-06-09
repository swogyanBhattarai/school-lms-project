package com.justdeepfried.GyanJyotiLMS.entities.studentFee.model;

import com.justdeepfried.GyanJyotiLMS.entities.academicYear.model.AcademicYearModel;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import com.justdeepfried.GyanJyotiLMS.enums.FEE_STATUS;
import com.justdeepfried.GyanJyotiLMS.enums.FEE_TYPES;
import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;

@Entity
@Setter
@Getter
@Filter(name = "schoolIdFilter", condition = "school_id = :schoolId")
public class StudentFeeModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentFeeId;

    @Enumerated(EnumType.STRING)
    private FEE_TYPES feeType;

    @Enumerated(EnumType.STRING)
    private FEE_STATUS feeStatus;

    private BigDecimal originalAmount;

    private BigDecimal discountPercentage;

    @OneToMany(mappedBy = "studentFee", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<FeePaymentModel> feePayments;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentModel student;

    @ManyToOne
    @JoinColumn(name = "academic_year_id")
    private AcademicYearModel academicYear;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;

    @Transient
    public BigDecimal getNetFee() {
        return originalAmount.subtract(
                originalAmount.multiply(discountPercentage)
                        .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_DOWN)
        );
    }
}



