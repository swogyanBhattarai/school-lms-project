package com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos;


import java.time.LocalDate;
import java.util.List;

public record MassAttendance (
        List<StudentAttendance> studentAttendances,
        LocalDate attendanceDate
) {
}
