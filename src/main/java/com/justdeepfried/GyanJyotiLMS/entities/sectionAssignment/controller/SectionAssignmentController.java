package com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.controller;

import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.dtos.SectionAssignmentCreate;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.dtos.SectionAssignmentResponse;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.service.SectionAssignmentService;
import com.justdeepfried.GyanJyotiLMS.entities.student.dtos.StudentCreate;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/section-assignment")
@RequiredArgsConstructor
public class SectionAssignmentController {
    private final SectionAssignmentService sectionAssignmentService;

    @PostMapping
    public ResponseEntity<SectionAssignmentResponse> create(@RequestBody SectionAssignmentCreate create) {
        return ResponseEntity.ok(sectionAssignmentService.createSectionAssignment(create));
    }

    @PostMapping("/create-and-assign-to/{id}")
    public ResponseEntity<SectionAssignmentResponse> createStudentAndAssignSection(@PathVariable("id") Long sectionId, @RequestBody @Valid StudentCreate studentCreate) {
        return ResponseEntity.ok(sectionAssignmentService.createStudentAndAssignSection(sectionId, studentCreate));
    }

    @GetMapping("/section/{sectionId}")
    public ResponseEntity<List<SectionAssignmentResponse>> findAllBySectionId(@PathVariable Long sectionId) {
        return ResponseEntity.ok(sectionAssignmentService.findAllBySectionId(sectionId));
    }

//    @GetMapping("/student/{studentId}")
//    public ResponseEntity<SectionAssignmentResponse> findByStudentIdAndActiveYear(@PathVariable Long studentId) {
//        return ResponseEntity.ok(sectionAssignmentService.findByStudentIdAndActiveYear(studentId));
//    }

    @GetMapping("/{id}")
    public ResponseEntity<SectionAssignmentResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(sectionAssignmentService.findById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable Long id) {
        sectionAssignmentService.deleteById(id);
        return ResponseEntity.ok("Section Assignment deleted successfully!");
    }

    @DeleteMapping("/section/{id}")
    public ResponseEntity<String> deleteAllBySectionId(@PathVariable Long id) {
        sectionAssignmentService.deleteAllBySectionId(id);
        return ResponseEntity.ok("All section assignments deleted successfully!");
    }
}
