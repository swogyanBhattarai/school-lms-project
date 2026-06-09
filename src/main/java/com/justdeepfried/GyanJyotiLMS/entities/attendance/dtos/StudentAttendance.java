package com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos;

import com.justdeepfried.GyanJyotiLMS.enums.ATTENDANCE_STATUS;

public record StudentAttendance (
        Long studentId,
        ATTENDANCE_STATUS attendanceStatus
) {
}
