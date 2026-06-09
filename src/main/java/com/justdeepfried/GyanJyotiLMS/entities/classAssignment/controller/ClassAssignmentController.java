package com.justdeepfried.GyanJyotiLMS.entities.classAssignment.controller;

import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.dtos.*;
import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.model.ClassAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.service.ClassAssignmentService;
import com.justdeepfried.GyanJyotiLMS.security.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/class-assignment")
@RequiredArgsConstructor
public class ClassAssignmentController {

    private final ClassAssignmentService classAssignmentService;

    @GetMapping
    public ResponseEntity<List<ClassAssignmentModel>> findAll() {
        return ResponseEntity.ok(classAssignmentService.findAll());
    }

    @GetMapping("/section/{id}")
    public ResponseEntity<List<ClassAssignmentResponse>> findAllBySectionId(@PathVariable("id") Long sectionId) {
        return ResponseEntity.ok(classAssignmentService.findAllBySectionId(sectionId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ClassAssignmentModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(classAssignmentService.findById(id));
    }

    @PostMapping
    public ResponseEntity<ClassAssignmentResponse> create(@RequestBody ClassAssignmentCreate create) {
        return ResponseEntity.ok(classAssignmentService.createClassAssignment(create));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ClassAssignmentResponse> update(@PathVariable Long id, @RequestBody ClassAssignmentUpdate update) {
        return ResponseEntity.ok(classAssignmentService.updateClassAssignment(id, update));
    }

    @GetMapping("/teacher")
    public ResponseEntity<List<ClassAssignmentAttendanceResponse>> findAllByTeacherId(@AuthenticationPrincipal UserPrincipal userPrincipal) {
        return ResponseEntity.ok(classAssignmentService.findAllByTeacherId(userPrincipal.getTeacherId()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        classAssignmentService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
