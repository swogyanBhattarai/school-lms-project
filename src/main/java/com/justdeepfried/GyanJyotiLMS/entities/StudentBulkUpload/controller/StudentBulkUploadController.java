package com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.dtos.StudentBulkUploadResponse;
import com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.service.StudentBulkUploadService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/student-bulk-upload")
@RequiredArgsConstructor
public class StudentBulkUploadController {

    private final StudentBulkUploadService bulkUploadService;

    @PostMapping(value = "/section/{id}", consumes = "multipart/form-data")
    public ResponseEntity<String> bulkCreateAndAssignStudents(@PathVariable("id") Long sectionId, @RequestParam("file") MultipartFile file) {
        bulkUploadService.bulkCreateAndAssignStudents(sectionId, file);
        return ResponseEntity.ok("Bulk added students successfully!");
    }

    @GetMapping("/section/{id}")
    public ResponseEntity<StudentBulkUploadResponse> getBulkUploadBySectionId(@PathVariable("id") Long sectionId) {
        return ResponseEntity.ok(bulkUploadService.getBulkUploadResultBySectionId(sectionId));
    }
}
