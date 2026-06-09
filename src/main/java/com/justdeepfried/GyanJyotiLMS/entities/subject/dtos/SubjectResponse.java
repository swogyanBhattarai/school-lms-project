package com.justdeepfried.GyanJyotiLMS.entities.subject.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.subject.model.SubjectModel;

public record SubjectResponse (
        Long subjectId,
        String subjectName
){
    public static SubjectResponse from(SubjectModel model) {
        return new SubjectResponse(
                model.getSubjectId(),
                model.getSubjectName()
        );
    }
}
