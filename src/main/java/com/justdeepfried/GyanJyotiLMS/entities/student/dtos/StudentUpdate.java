package com.justdeepfried.GyanJyotiLMS.entities.student.dtos;

import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record StudentUpdate (
        @NotBlank(message = "Student Name cannot be empty!")
        String studentName,
        @NotNull(message = "Date of Birth cannot be empty!")
        @Past(message = "Date of Birth must be of the past!")
        LocalDate dateOfBirth,

        @NotBlank(message = "Primary parent's name cannot be empty!")
        String parentName1,
        @NotBlank(message = "Primary parent's phone number cannot be empty!")
        @Pattern(regexp = "^[0-9]{10}", message = "Phone number cannot be less than 10 digits!")
        String parentPhoneNumber1,

        String parentName2,
        @Pattern(regexp = "^[0-9]{10}", message = "Secondary parent's phone number cannot be less than 10 digits!")
        String parentPhoneNumber2
){
        @AssertTrue(message = "Secondary parent should have both Name and Phone Number!")
        public boolean isParent2Valid() {
                boolean hasName = parentName2 != null && !parentName2.isBlank();
                boolean hasNumber = parentPhoneNumber2 != null && !parentPhoneNumber2.isBlank();

                return hasName == hasNumber;
        }
}
