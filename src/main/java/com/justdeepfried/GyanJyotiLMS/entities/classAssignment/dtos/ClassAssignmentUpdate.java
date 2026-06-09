package com.justdeepfried.GyanJyotiLMS.entities.classAssignment.dtos;

import com.justdeepfried.GyanJyotiLMS.enums.TEACHER_ROLES;

public record ClassAssignmentUpdate(
        Long teacherId,
        TEACHER_ROLES teacherRole,
        Long subjectId,
        Long sectionId
) {
}
