package com.justdeepfried.GyanJyotiLMS.entities.student.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.model.SchoolClassModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;

public record StudentClassResponse (
        String grade,
        String sectionName
) {
    public static StudentClassResponse from(SectionModel sectionModel, SchoolClassModel schoolClassModel) {
        return new StudentClassResponse(
                schoolClassModel.getGrade(),
                sectionModel.getSectionName()
        );
    }
}
