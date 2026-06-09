package com.justdeepfried.GyanJyotiLMS.entities.section.dto;

import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;

public record SectionAssignmentStudentResponse (
        Long sectionAssignmentId,
        Long studentId,
        String studentName
) {
    public static SectionAssignmentStudentResponse from(SectionAssignmentModel sectionAssignmentModel) {
        return new SectionAssignmentStudentResponse(
                sectionAssignmentModel.getSectionAssignmentId(),
                sectionAssignmentModel.getStudent().getStudentId(),
                sectionAssignmentModel.getStudent().getStudentName()
        );
    }
}
