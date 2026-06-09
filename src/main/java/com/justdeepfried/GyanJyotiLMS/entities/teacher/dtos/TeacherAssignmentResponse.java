package com.justdeepfried.GyanJyotiLMS.entities.teacher.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.model.ClassAssignmentModel;

import java.util.List;

public record TeacherAssignmentResponse (
    int subjectsTaught,
    int classesTaught
) {
    public static TeacherAssignmentResponse from(List<ClassAssignmentModel> classAssignments) {

        if (classAssignments == null || classAssignments.isEmpty()) return empty();

        int subjectsTaught = (int) classAssignments.stream()
                .map(ca -> ca.getSubject().getSubjectId())
                .distinct()
                .count();

        int classesTaught = (int) classAssignments.stream()
                .map(ca -> ca.getSection().getSectionId())
                .distinct()
                .count();

        return new TeacherAssignmentResponse(
                subjectsTaught,
                classesTaught
        );
    }

    public static TeacherAssignmentResponse empty() {
        return new TeacherAssignmentResponse(
                0,
                0
        );
    }
}
