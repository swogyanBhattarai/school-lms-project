package com.justdeepfried.GyanJyotiLMS.entities.schoolClass.controller;

import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.dtos.SchoolClassCreate;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.dtos.SchoolClassResponse;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.dtos.SchoolClassUpdate;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.model.SchoolClassModel;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.service.SchoolClassService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/school-class")
@RequiredArgsConstructor
public class SchoolClassController {

    private final SchoolClassService schoolClassService;

    @GetMapping
    public ResponseEntity<List<SchoolClassResponse>> findAll() {
        return ResponseEntity.ok(schoolClassService.findAll());
    }

    @GetMapping("/get-all-active")
    public ResponseEntity<List<SchoolClassResponse>> findAllActive() {
        return ResponseEntity.ok(schoolClassService.findAllActive());
    }

    @GetMapping("/{id}")
    public ResponseEntity<SchoolClassResponse> findById(@PathVariable Long id) {
        return ResponseEntity.ok(schoolClassService.findById(id));
    }

    @PostMapping
    public ResponseEntity<String> create(@RequestBody SchoolClassCreate creation) {
        schoolClassService.save(creation.grade(), creation.academicYearId());
        return ResponseEntity.ok("Class created successfully!");
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> update(@PathVariable Long id, @RequestBody SchoolClassUpdate update) {
        schoolClassService.editSchoolClass(update.grade(), id);
        return ResponseEntity.ok("Class edited successfully!");
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        schoolClassService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
