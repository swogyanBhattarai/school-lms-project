package com.justdeepfried.GyanJyotiLMS.entities.academicYear.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.academicYear.model.AcademicYearModel;

import java.time.LocalDate;
import java.util.List;

public record AcademicYearResponse (
        Long academicYearId,
        String academicYear,
        LocalDate startDate,
        LocalDate endDate,
        boolean isActive,
        List<YearClassResponse> classes
) {
    public static AcademicYearResponse from(AcademicYearModel yearModel) {
        return new AcademicYearResponse(
                yearModel.getAcademicYearId(),
                yearModel.getAcademicYear(),
                yearModel.getStartDate(),
                yearModel.getEndDate(),
                yearModel.isActive(),
                yearModel.getClasses()
                        .stream()
                        .map(YearClassResponse::from)
                        .toList()
        );
    }

    public static AcademicYearResponse from(AcademicYearModel yearModel, List<YearClassResponse> yearClassResponses) {
        return new AcademicYearResponse(
                yearModel.getAcademicYearId(),
                yearModel.getAcademicYear(),
                yearModel.getStartDate(),
                yearModel.getEndDate(),
                yearModel.isActive(),
                yearClassResponses
        );
    }
}
