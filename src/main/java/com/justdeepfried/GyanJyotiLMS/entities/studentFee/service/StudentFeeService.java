package com.justdeepfried.GyanJyotiLMS.entities.studentFee.service;

import com.justdeepfried.GyanJyotiLMS.entities.academicYear.model.AcademicYearModel;
import com.justdeepfried.GyanJyotiLMS.entities.academicYear.repository.AcademicYearRepo;
import com.justdeepfried.GyanJyotiLMS.entities.school.context.SchoolContext;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.school.repository.SchoolRepo;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.repository.StudentRepo;
import com.justdeepfried.GyanJyotiLMS.entities.studentFee.dtos.FeePaymentCreate;
import com.justdeepfried.GyanJyotiLMS.entities.studentFee.dtos.StudentFeeCreate;
import com.justdeepfried.GyanJyotiLMS.entities.studentFee.model.FeePaymentModel;
import com.justdeepfried.GyanJyotiLMS.entities.studentFee.model.StudentFeeModel;
import com.justdeepfried.GyanJyotiLMS.entities.studentFee.repository.FeePaymentRepo;
import com.justdeepfried.GyanJyotiLMS.entities.studentFee.repository.StudentFeeRepo;
import com.justdeepfried.GyanJyotiLMS.enums.FEE_STATUS;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceNotFoundException;
import com.justdeepfried.GyanJyotiLMS.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentFeeService {

    private final StudentFeeRepo studentFeeRepo;
    private final FeePaymentRepo feePaymentRepo;

    private final StudentRepo studentRepo;
    private final AcademicYearRepo academicYearRepo;
    private final SchoolRepo schoolRepo;

    public void createStudentFee(Long studentId, Long academicYearId, StudentFeeCreate feeCreate) {
        StudentFeeModel studentFee = new StudentFeeModel();

        studentFee.setFeeType(feeCreate.feeType());
        studentFee.setOriginalAmount(feeCreate.originalAmount());
        studentFee.setDiscountPercentage(feeCreate.discountPercentage());

        studentFee.setFeeStatus(FEE_STATUS.UNPAID);

        StudentModel student = studentRepo.findById(studentId).orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + studentId));
        AcademicYearModel academicYear = academicYearRepo.findById(academicYearId).orElseThrow(() -> new ResourceNotFoundException("Academic Year not found with id: " + academicYearId));
        SchoolModel school = schoolRepo.findById(SchoolContext.get()).orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + SchoolContext.get()));

        studentFee.setStudent(student);
        studentFee.setAcademicYear(academicYear);
        studentFee.setSchool(school);

        studentFeeRepo.save(studentFee);
    }

    public List<StudentFeeModel> findAllByStudentId(Long studentId) {
        return studentFeeRepo.findAllByStudentId(studentId);
    }

    public void createFeePayment(Long studentFeeId, FeePaymentCreate feePaymentCreate) {
        StudentFeeModel studentFeeModel = studentFeeRepo.findById(studentFeeId).orElseThrow(() -> new ResourceNotFoundException("Student fee not found with id: " + studentFeeId));

        if (studentFeeModel.getFeeStatus() == FEE_STATUS.PAID) throw new ValidationException("Fee is already fully paid!");

        SchoolModel school = schoolRepo.findById(SchoolContext.get()).orElseThrow(() -> new ResourceNotFoundException("School not found with id: " + SchoolContext.get()));

        BigDecimal newTotalPaid = feePaymentRepo.sumTotalPaymentByStudentFeeId(studentFeeId).add(feePaymentCreate.amountPaid());

        BigDecimal netFee = studentFeeModel.getNetFee();

        if (newTotalPaid.compareTo(netFee) > 0) {
            throw new ValidationException("Payment cannot exceed remaining amount!");
        } else if (newTotalPaid.compareTo(netFee) < 0) {
            studentFeeModel.setFeeStatus(FEE_STATUS.PARTIAL);
        } else {
            studentFeeModel.setFeeStatus(FEE_STATUS.PAID);
        }

        FeePaymentModel feePayment = new FeePaymentModel();

        feePayment.setAmountPaid(feePaymentCreate.amountPaid());
        feePayment.setPaidBy(feePaymentCreate.paidBy());
        feePayment.setPhoneNumber(feePaymentCreate.phoneNumber());
        feePayment.setPaymentType(feePaymentCreate.paymentType());

        feePayment.setSchool(school);
        feePayment.setStudentFee(studentFeeModel);

        feePaymentRepo.save(feePayment);
    }

    public void deleteStudentFee(Long studentFeeId) {
        studentFeeRepo.deleteById(studentFeeId);
    }

    public void deleteFeePayment(Long feePaymentId) {
        feePaymentRepo.deleteById(feePaymentId);
    }
}
