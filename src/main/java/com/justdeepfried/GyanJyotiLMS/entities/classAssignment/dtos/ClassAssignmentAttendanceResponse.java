package com.justdeepfried.GyanJyotiLMS.entities.classAssignment.dtos;

import com.justdeepfried.GyanJyotiLMS.enums.TEACHER_ROLES;

public record ClassAssignmentAttendanceResponse (
        Long classAssignmentId,
        Long sectionId,
        Long subjectId,
        String teacherName,
        TEACHER_ROLES teacherRole,
        String subjectName,
        String sectionName,
        String grade,
        String academicYear,
        Long studentCount,
        boolean attendanceCompleted
) {
}
