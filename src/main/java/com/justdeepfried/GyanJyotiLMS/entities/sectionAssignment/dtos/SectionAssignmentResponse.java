package com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;

public record SectionAssignmentResponse (
        Long sectionAssignmentId,
        Long sectionId,
        String sectionName,
        Long studentId,
        String studentName
) {
    public static SectionAssignmentResponse from(SectionAssignmentModel model) {
        return new SectionAssignmentResponse(
                model.getSectionAssignmentId(),
                model.getSection().getSectionId(),
                model.getSection().getSectionName(),
                model.getStudent().getStudentId(),
                model.getStudent().getStudentName()
        );
    }
}
