package com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.attendanceSummary;

public record AttendanceSummary (
        Long forStudentId,
        Long presentCount,
        Long absentCount,
        Long totalCount
) {
    public static AttendanceSummary empty() {
        return new AttendanceSummary(null, 0L, 0L, 0L);
    }
}
