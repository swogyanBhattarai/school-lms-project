package com.justdeepfried.GyanJyotiLMS.entities.teacher.dtos;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record TeacherUpdate(
        @NotBlank(message = "Teacher name cannot be empty!")
        String teacherName,
        @Pattern(regexp = "[0-9]{10}", message = "Teacher phone number must be 10 digit!")
        String teacherPhoneNumber
) {
}