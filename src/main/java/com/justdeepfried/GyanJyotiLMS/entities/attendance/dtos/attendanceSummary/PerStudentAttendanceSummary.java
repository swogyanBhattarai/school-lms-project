package com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.attendanceSummary;

public record PerStudentAttendanceSummary (
        Long subjectId,
        String subjectName,
        Long presentCount,
        Long absentCount,
        Long leaveCount,
        Long totalCount
) {
    public static PerStudentAttendanceSummary empty() {
        return new PerStudentAttendanceSummary(
                null,
                null,
                0L,
                0L,
                0L,
                0L
        );
    }
}
