package com.justdeepfried.GyanJyotiLMS.entities.student.dtos;

import java.time.LocalDate;
import java.util.List;

import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;

public record StudentDetailResponse (
        String studentName,
        LocalDate dateOfBirth,
        Long sectionId,
        String schoolClassName,
        String sectionName,
        List<StudentParentResponse> parents
) {
    public static StudentDetailResponse from(StudentModel student) {
        SectionAssignmentModel assignmentModel = student.getSectionAssignments()
                .stream().filter(s -> s.getAcademicYear().isActive())
                .findFirst()
                .orElse(null);

        Long sectionId = assignmentModel != null ? assignmentModel.getSection().getSectionId()
                : null;

        String schoolClassName = assignmentModel != null ? assignmentModel.getSection().getSchoolClass().getGrade()
                : null;

        String sectionName = assignmentModel != null ? assignmentModel.getSection().getSectionName()
                : null;

        return new StudentDetailResponse(
                student.getStudentName(),
                student.getDateOfBirth(),
                sectionId,
                schoolClassName,
                sectionName,
                student.getParents()
                        .stream().map(StudentParentResponse::from)
                        .toList()
        );
    }
}
