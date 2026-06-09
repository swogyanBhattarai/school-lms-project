package com.justdeepfried.GyanJyotiLMS.entities.studentFee.controller;

import com.justdeepfried.GyanJyotiLMS.entities.studentFee.dtos.FeePaymentCreate;
import com.justdeepfried.GyanJyotiLMS.entities.studentFee.dtos.StudentFeeCreate;
import com.justdeepfried.GyanJyotiLMS.entities.studentFee.model.StudentFeeModel;
import com.justdeepfried.GyanJyotiLMS.entities.studentFee.service.StudentFeeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-fee")
@RequiredArgsConstructor
public class StudentFeeController {

    private final StudentFeeService studentFeeService;

    @PostMapping("/{studentId}/{academicYearId}")
    public ResponseEntity<String> createStudentFee(
            @PathVariable Long studentId,
            @PathVariable Long academicYearId,
            @RequestBody StudentFeeCreate feeCreate
    ) {
        studentFeeService.createStudentFee(studentId, academicYearId, feeCreate);
        return ResponseEntity.ok("Student fee created successfully!");
    }

    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentFeeModel>> findAllByStudentId(@PathVariable Long studentId) {
        return ResponseEntity.ok(studentFeeService.findAllByStudentId(studentId));
    }

    @PostMapping("/payment/{studentFeeId}")
    public ResponseEntity<String> createFeePayment(
            @PathVariable Long studentFeeId,
            @RequestBody FeePaymentCreate feePaymentCreate
    ) {
        studentFeeService.createFeePayment(studentFeeId, feePaymentCreate);
        return ResponseEntity.ok("Fee payment created successfully!");
    }

    @DeleteMapping("/{studentFeeId}")
    public ResponseEntity<Void> deleteStudentFee(@PathVariable Long studentFeeId) {
        studentFeeService.deleteStudentFee(studentFeeId);
        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/payment/{feePaymentId}")
    public ResponseEntity<Void> deleteFeePayment(@PathVariable Long feePaymentId) {
        studentFeeService.deleteFeePayment(feePaymentId);
        return ResponseEntity.noContent().build();
    }
}
