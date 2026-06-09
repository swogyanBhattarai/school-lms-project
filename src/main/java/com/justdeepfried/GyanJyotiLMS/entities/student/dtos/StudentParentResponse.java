package com.justdeepfried.GyanJyotiLMS.entities.student.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.parent.model.ParentModel;

public record StudentParentResponse (
        String parentName,
        String parentNumber
) {
    public static StudentParentResponse from(ParentModel parent) {
        return new StudentParentResponse(
                parent.getParentName(),
                parent.getParentNumber()
        );
    }
}
