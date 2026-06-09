package com.justdeepfried.GyanJyotiLMS.entities.student.controller;

import com.justdeepfried.GyanJyotiLMS.entities.PageResponse;
import com.justdeepfried.GyanJyotiLMS.entities.student.dtos.StudentCreate;
import com.justdeepfried.GyanJyotiLMS.entities.student.dtos.StudentDetailResponse;
import com.justdeepfried.GyanJyotiLMS.entities.student.dtos.StudentResponse;
import com.justdeepfried.GyanJyotiLMS.entities.student.dtos.StudentUpdate;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.service.StudentService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/student")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public ResponseEntity<PageResponse<StudentResponse>> findAll(
            @RequestParam(required = false, defaultValue = "studentId") String sortBy,
            @RequestParam(required = false, defaultValue = "ASC") String sortDir,
            @RequestParam(required = false, defaultValue = "20") int pageSize,
            @RequestParam(required = false, defaultValue = "1") int pageNum,
            @RequestParam(required = false) Boolean hasSectionAssignment,
            @RequestParam(required = false) String studentName,
            @RequestParam(required = false) Long sectionId,
            @RequestParam(required = false) Long classId
    ) {

        Sort sort = null;

        if (sortDir.equalsIgnoreCase("ASC")) {
            sort = Sort.by(sortBy).ascending();
        } else {
            sort = Sort.by(sortBy).descending();
        }

        return ResponseEntity.ok(studentService.findAllFiltered(studentName, sectionId, classId, hasSectionAssignment,PageRequest.of(pageNum - 1, pageSize, sort)));
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDetailResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(studentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<StudentModel> create(@RequestBody @Valid StudentCreate student) {
        return ResponseEntity.ok(studentService.createStudent(student));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentModel> update(@PathVariable Long id, @RequestBody @Valid StudentUpdate studentUpdate) {
        return ResponseEntity.ok(studentService.updateStudent(id, studentUpdate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        studentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
