package com.justdeepfried.GyanJyotiLMS.entities.csvParser.dtos;

import org.apache.commons.csv.CSVRecord;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

public record CSVStudentRow(
        int rowNumber,
        String studentName,
        LocalDate dateOfBirth,
        String parentName1,
        String parentPhoneNumber1,
        String parentName2,
        String parentPhoneNumber2
) {
    public static CSVStudentRow from(CSVRecord record) {

        int rowNumber = (int) record.getRecordNumber() + 1;

        String studentName = record.get("student_name");
        LocalDate dateOfBirth = parseDate(record.get("date_of_birth"));

        String parentName1 = record.get("primary_parent_name");
        String parentPhoneNumber1 = record.get("primary_parent_phone");

        String parentName2 = getOptional(record, "secondary_parent_name");
        String parentPhoneNumber2 = getOptional(record, "secondary_parent_phone");

        return new CSVStudentRow(
                rowNumber,
                studentName,
                dateOfBirth,
                parentName1,
                parentPhoneNumber1,
                parentName2,
                parentPhoneNumber2
        );
    }

    private static final DateTimeFormatter DATE_FORMAT =
            DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private static LocalDate parseDate(String rawDate) {
        if (rawDate == null || rawDate.isBlank()) return null;
        try {
            return LocalDate.parse(rawDate.trim(), DATE_FORMAT);
        } catch (Exception e) {
            return null;
        }
    }

    private static String getOptional(CSVRecord record, String columnName) {
        if (!record.isMapped(columnName)) return null;
        String value = record.get(columnName);
        return value.isBlank() ? null : value;
    }
}
