package com.justdeepfried.GyanJyotiLMS.entities.student.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.attendanceSummary.AttendanceSummary;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.model.SchoolClassModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;

public record StudentResponse (
    Long studentId,
    String studentName,
    Double averageAttendance,
    StudentClassResponse studentClass
){
    public static StudentResponse from(StudentModel studentModel) {

        SectionAssignmentModel activeAssignment = studentModel.getSectionAssignments()
                .stream().filter(sa ->
                        sa.getSection().getSchoolClass().getAcademicYear().isActive()
                )
                .findFirst()
                .orElse(null);

        if (activeAssignment == null) {
            return new StudentResponse(
                    studentModel.getStudentId(),
                    studentModel.getStudentName(),
                    null,
                    null
            );
        }

        SectionModel section = activeAssignment.getSection();
        SchoolClassModel schoolClass = section.getSchoolClass();

        return new StudentResponse(
                studentModel.getStudentId(),
                studentModel.getStudentName(),
                null,
                StudentClassResponse.from(section, schoolClass)
        );
    }

    public static StudentResponse from(StudentModel studentModel, AttendanceSummary summary) {

        SectionAssignmentModel activeAssignment = studentModel.getSectionAssignments()
                .stream().filter(sa ->
                        sa.getSection().getSchoolClass().getAcademicYear().isActive()
                )
                .findFirst()
                .orElse(null);

        if (activeAssignment == null) {
            return new StudentResponse(
                    studentModel.getStudentId(),
                    studentModel.getStudentName(),
                    null,
                    null
            );
        }

        Double averageAttendance = summary.totalCount() > 0 ? (summary.presentCount() * 100.0) / summary.totalCount() : null;

        SectionModel section = activeAssignment.getSection();
        SchoolClassModel schoolClass = section.getSchoolClass();

        return new StudentResponse(
                studentModel.getStudentId(),
                studentModel.getStudentName(),
                averageAttendance,
                StudentClassResponse.from(section, schoolClass)
        );
    }
}
