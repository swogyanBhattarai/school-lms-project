package com.justdeepfried.GyanJyotiLMS.entities.student.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.model.SchoolClassModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;

public record StudentSectionAssignmentResponse (
        Long sectionId,
        String schoolClassName,
        String sectionName,
        boolean isActiveAssignment
) {
    public static StudentSectionAssignmentResponse from(SectionAssignmentModel assignmentModel) {

        SectionModel sectionModel = assignmentModel.getSection();
        SchoolClassModel classModel = assignmentModel.getSection().getSchoolClass();

        return new StudentSectionAssignmentResponse(
                sectionModel.getSectionId(),
                classModel.getGrade(),
                sectionModel.getSectionName(),
                classModel.getAcademicYear().isActive()
        );
    }
}
