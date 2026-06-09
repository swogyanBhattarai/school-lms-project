package com.justdeepfried.GyanJyotiLMS.entities.diary.dtos;

import jakarta.validation.constraints.FutureOrPresent;

import java.time.LocalDate;

public record DiaryCreate (
        @FutureOrPresent(message = "Diary entry cannot be of the past!")
        LocalDate diaryDate,
        Long subjectId,
        Long createdById,
        Long sectionId,
        String title,
        String content
) {
}
