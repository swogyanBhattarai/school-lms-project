package com.justdeepfried.GyanJyotiLMS.entities.student.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.csvParser.dtos.CSVStudentRow;
import jakarta.validation.constraints.*;

import java.time.LocalDate;

public record StudentCreate (
        @NotBlank(message = "Student Name cannot be empty!")
        String studentName,
        @NotNull(message = "Date of Birth cannot be empty!")
        @Past(message = "Date of Birth must be of the past!")
        LocalDate dateOfBirth,

        @NotBlank(message = "Primary parent's name cannot be empty!")
        String parentName1,
        @NotBlank(message = "Primary parent's phone number cannot be empty!")
        @Pattern(regexp = "^[0-9]{10}", message = "Primary parent's phone number cannot be less than 10 digits!")
        String parentPhoneNumber1,

        String parentName2,
        @Pattern(regexp = "^[0-9]{10}", message = "Secondary parent's phone number cannot be less than 10 digits!")
        String parentPhoneNumber2
){
        @AssertTrue(message = "Secondary parent should have both name and phone number!")
        public boolean isParent2Valid() {
                boolean hasName = parentName2 != null && !parentName2.isBlank();
                boolean hasNumber = parentPhoneNumber2 != null && !parentPhoneNumber2.isBlank();

                return hasName == hasNumber;
        }

        public static StudentCreate from(CSVStudentRow studentRow) {
                return new StudentCreate(
                        studentRow.studentName(),
                        studentRow.dateOfBirth(),
                        studentRow.parentName1(),
                        studentRow.parentPhoneNumber1(),
                        studentRow.parentName2(),
                        studentRow.parentPhoneNumber2()
                );
        }
}
