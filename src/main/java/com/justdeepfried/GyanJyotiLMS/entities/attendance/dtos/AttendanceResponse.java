package com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.attendance.model.AttendanceModel;
import com.justdeepfried.GyanJyotiLMS.enums.ATTENDANCE_STATUS;

import java.time.LocalDate;

public record AttendanceResponse (
        Long studentId,
        Long sectionId,
        Long subjectId,
        Long teacherId,
        LocalDate attendanceDate,
        ATTENDANCE_STATUS attendanceStatus
) {
    public static AttendanceResponse from(AttendanceModel attendanceModel) {
        return new AttendanceResponse(
                attendanceModel.getStudent().getStudentId(),
                attendanceModel.getSection().getSectionId(),
                attendanceModel.getSubject().getSubjectId(),
                attendanceModel.getPerformedBy() != null ? attendanceModel.getPerformedBy().getTeacherId() : null,
                attendanceModel.getAttendanceDate(),
                attendanceModel.getAttendanceStatus()
        );
    }
}
