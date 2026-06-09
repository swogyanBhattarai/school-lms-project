package com.justdeepfried.GyanJyotiLMS.entities.academicYear.dtos;

import java.time.LocalDate;

public record AcademicYearCreate (
        String academicYear,
        LocalDate startDate,
        LocalDate endDate
){
}
