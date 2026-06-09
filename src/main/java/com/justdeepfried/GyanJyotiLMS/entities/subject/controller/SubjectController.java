package com.justdeepfried.GyanJyotiLMS.entities.subject.controller;

import com.justdeepfried.GyanJyotiLMS.entities.subject.dtos.SubjectCreate;
import com.justdeepfried.GyanJyotiLMS.entities.subject.dtos.SubjectResponse;
import com.justdeepfried.GyanJyotiLMS.entities.subject.dtos.SubjectUpdate;
import com.justdeepfried.GyanJyotiLMS.entities.subject.model.SubjectModel;
import com.justdeepfried.GyanJyotiLMS.entities.subject.service.SubjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subject")
@RequiredArgsConstructor
public class SubjectController {

    private final SubjectService subjectService;

    @GetMapping
    public ResponseEntity<List<SubjectResponse>> findAll() {
        return ResponseEntity.ok(subjectService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SubjectModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(subjectService.findById(id));
    }

    @PostMapping
    public ResponseEntity<String> createSubject(@RequestBody SubjectCreate subjectCreate) {
        subjectService.createSubject(subjectCreate);
        return ResponseEntity.ok("Subject created successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> updateSubject(@PathVariable Long id, @RequestBody SubjectUpdate subjectUpdate) {
        subjectService.updateSubject(id, subjectUpdate);
        return ResponseEntity.ok("Subject updated successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        subjectService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
