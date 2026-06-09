package com.justdeepfried.GyanJyotiLMS.entities.attendance.querydsl;

import com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.attendanceSummary.PerStudentAttendanceSummary;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.model.AttendanceModel;

import java.time.LocalDate;
import java.util.List;

public interface AttendanceCustomRepo {
    List<PerStudentAttendanceSummary> findAllFilteredForStudent(Long studentId, Long subjectId, Long sectionId, LocalDate fromDate, LocalDate toDate);
    List<AttendanceModel> findAllByStudentIdAndSectionId(Long studentId, Long sectionId);
}
