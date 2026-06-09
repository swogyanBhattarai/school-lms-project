package com.justdeepfried.GyanJyotiLMS.entities.teacher.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.justdeepfried.GyanJyotiLMS.entities.teacher.dtos.TeacherCreate;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.dtos.TeacherResponse;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.dtos.TeacherUpdate;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.model.TeacherModel;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.service.TeacherService;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/teacher")
@RequiredArgsConstructor
public class TeacherController {

    private final TeacherService teacherService;

    @GetMapping
    public ResponseEntity<List<TeacherResponse>> findAll() {
        return ResponseEntity.ok(teacherService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<TeacherModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(teacherService.findById(id));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody @Valid TeacherCreate teacherCreate) {
        teacherService.save(teacherCreate);
        return ResponseEntity.ok("Teacher created successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody @Valid TeacherUpdate teacherUpdate) {
        teacherService.update(id, teacherUpdate);
        return ResponseEntity.ok("Teacher updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        teacherService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
