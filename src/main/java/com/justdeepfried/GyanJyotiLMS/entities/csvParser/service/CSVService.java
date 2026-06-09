package com.justdeepfried.GyanJyotiLMS.entities.csvParser.service;

import com.justdeepfried.GyanJyotiLMS.entities.csvParser.dtos.CSVStudentRow;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Service
public class CSVService {
    public static final CSVFormat FORMAT = CSVFormat.DEFAULT
            .builder()
            .setHeader()
            .setSkipHeaderRecord(true)
            .setIgnoreEmptyLines(true)
            .setTrim(true)
            .get();

    public List<CSVStudentRow> parseCSV(MultipartFile file) {
        try (
                Reader reader = new InputStreamReader(
                        file.getInputStream(), StandardCharsets.UTF_8
                );
                CSVParser parser = CSVParser.builder()
                        .setFormat(FORMAT)
                        .setReader(reader)
                        .get();
        ) {

            List<CSVStudentRow> studentRows = new ArrayList<>();

            for (CSVRecord record : parser) {
                studentRows.add(CSVStudentRow.from(record));
            }

            return studentRows;
        } catch (IOException e) {
            throw new RuntimeException("Failed to parse CSV file: " + e.getMessage());
        }
    }
}
