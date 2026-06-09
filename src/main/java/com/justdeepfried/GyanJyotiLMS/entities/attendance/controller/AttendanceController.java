package com.justdeepfried.GyanJyotiLMS.entities.attendance.controller;

import java.time.LocalDate;
import java.util.List;

import com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.attendanceSummary.PerStudentAttendanceSummary;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.AttendanceResponse;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.MassAttendance;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.model.AttendanceModel;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.service.AttendanceService;
import com.justdeepfried.GyanJyotiLMS.security.user.UserPrincipal;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/attendance")
@RequiredArgsConstructor
public class AttendanceController {

    private final AttendanceService attendanceService;

    @GetMapping
    public ResponseEntity<List<AttendanceModel>> findAll() {
        return ResponseEntity.ok(attendanceService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<AttendanceModel> findById(@PathVariable Long id) {
        return ResponseEntity.ok(attendanceService.findById(id));
    }

    @PostMapping
    public ResponseEntity<AttendanceModel> create(@RequestBody AttendanceModel attendance) {
        return ResponseEntity.ok(attendanceService.save(attendance));
    }

    @PostMapping("/section/{sectionId}/subject/{subjectId}")
    public ResponseEntity<String> massCreate(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long sectionId,
            @PathVariable Long subjectId,
            @RequestBody MassAttendance massAttendance
            ) {
        attendanceService.massCreateAttendance(
                sectionId,
                subjectId,
                userPrincipal.getTeacherId(),
                massAttendance
        );
        return ResponseEntity.ok("Attendance created successfully!");
    }

    @GetMapping("/section/{sectionId}/subject/{subjectId}")
    public ResponseEntity<List<AttendanceResponse>> findAttendance(
            @AuthenticationPrincipal UserPrincipal userPrincipal,
            @PathVariable Long sectionId,
            @PathVariable Long subjectId
            ) {
        return ResponseEntity.ok(attendanceService.findByTeacherIdSectionIdAndSubjectIdAndAttendanceDate(sectionId, subjectId, userPrincipal.getTeacherId()));
    }

    @GetMapping("/student-summary")
    public ResponseEntity<List<PerStudentAttendanceSummary>> getStudentAttendanceSummary(
            @RequestParam Long studentId,
            @RequestParam Long sectionId,
            @RequestParam(required = false) Long subjectId,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate fromDate,
            @RequestParam(required = false)
            @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
            LocalDate toDate
    ) {

        List<PerStudentAttendanceSummary> response =
                attendanceService.findAllFilteredForStudent(
                        studentId,
                        subjectId,
                        sectionId,
                        fromDate,
                        toDate
                );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/student-daily")
    public ResponseEntity<List<AttendanceResponse>> getStudentDailyAttendance(
            @RequestParam Long studentId,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date
    ) {
        if (date == null) date = LocalDate.now();
        return ResponseEntity.ok(attendanceService.findAllByStudentIdAndDate(studentId, date));
    }
}
