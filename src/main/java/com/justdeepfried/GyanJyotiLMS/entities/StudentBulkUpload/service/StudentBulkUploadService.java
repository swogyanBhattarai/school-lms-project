package com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.service;

import com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.dtos.StudentBulkUploadResponse;
import com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.model.StudentBulkUploadModel;
import com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.model.StudentBulkUploadRowModel;
import com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.repository.StudentBulkUploadRepo;
import com.justdeepfried.GyanJyotiLMS.entities.csvParser.dtos.CSVStudentRow;
import com.justdeepfried.GyanJyotiLMS.entities.csvParser.service.CSVService;
import com.justdeepfried.GyanJyotiLMS.entities.parent.model.ParentModel;
import com.justdeepfried.GyanJyotiLMS.entities.parent.repository.ParentRepo;
import com.justdeepfried.GyanJyotiLMS.entities.school.context.SchoolContext;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.school.service.SchoolService;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.service.SectionService;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.repository.SectionAssignmentRepo;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.service.SectionAssignmentService;
import com.justdeepfried.GyanJyotiLMS.entities.student.dtos.StudentCreate;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.repository.StudentRepo;
import com.justdeepfried.GyanJyotiLMS.entities.user.model.UserModel;
import com.justdeepfried.GyanJyotiLMS.entities.user.repository.UserRepository;
import com.justdeepfried.GyanJyotiLMS.enums.ROW_STATUS;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentBulkUploadService {

    private final SectionAssignmentService sectionAssignmentService;
    private final CSVService csvService;

    private final SectionService sectionService;
    private final SchoolService schoolService;

    private final StudentBulkUploadRepo bulkUploadRepo;
    private final StudentRepo studentRepo;
    private final ParentRepo parentRepo;
    private final SectionAssignmentRepo sectionAssignmentRepo;
    private final UserRepository userRepo;

    private final Validator validator;

    public void bulkCreateAndAssignStudents(Long sectionId, MultipartFile file) {

        if (file.getOriginalFilename() != null && !file.getOriginalFilename().isBlank()) {
            if (!file.getOriginalFilename().endsWith(".csv")) {
                throw new RuntimeException("Cannot upload file that is not a CSV!");
            }
        }

        List<CSVStudentRow> rows = csvService.parseCSV(file);

        StudentBulkUploadModel result = new StudentBulkUploadModel();

        SectionModel section = sectionService.findModelById(sectionId);
        SchoolModel school = schoolService.findById(SchoolContext.get());

        result.setSection(section);
        result.setSchool(school);

        Map<String, StudentModel> studentModelMap = new HashMap<>();
        Map<String, ParentModel> parentModelMap = new HashMap<>();

        studentRepo.findAllStudentsAndParentBySchoolId(school.getSchoolId())
                .forEach(s -> studentModelMap.put(s.getStudentName() + "|" + s.getDateOfBirth(), s));

        parentRepo.findAllBySchool_SchoolId(school.getSchoolId())
                .forEach(p -> parentModelMap.put(p.getParentNumber(), p));

        List<StudentModel> newStudents = new ArrayList<>();
        List<StudentModel> updatedStudents = new ArrayList<>();
        List<ParentModel> newParents = new ArrayList<>();
        List<UserModel> newParentUsers = new ArrayList<>();
        List<SectionAssignmentModel> newAssignments = new ArrayList<>();

        Set<StudentModel> assignedThisBatch = Collections.newSetFromMap(new IdentityHashMap<>());
        List<Long> studentIdsWithSectionAssignments = sectionAssignmentRepo.findStudentIdsByAcademicYearId(section.getSchoolClass().getAcademicYear().getAcademicYearId());

        int failureCount = 0;
        int successCount = 0;

        for (CSVStudentRow row : rows) {
            StudentBulkUploadRowModel rowResult = new StudentBulkUploadRowModel();

            rowResult.setRowNumber(row.rowNumber());
            rowResult.setStudentName(row.studentName());
            rowResult.setDateOfBirth(row.dateOfBirth());
            rowResult.setParentName1(row.parentName1());
            rowResult.setParentPhoneNumber1(row.parentPhoneNumber1());
            rowResult.setParentName2(row.parentName2());
            rowResult.setParentPhoneNumber2(row.parentPhoneNumber2());
            rowResult.setBulkUploadModel(result);

            StudentCreate studentCreate = StudentCreate.from(row);

            Set<ConstraintViolation<StudentCreate>> violations = validator.validate(studentCreate);

            if (!violations.isEmpty()) {
                String errors = violations.stream()
                        .map(ConstraintViolation::getMessage)
                        .collect(Collectors.joining(", "));

                rowResult.setRowStatus(ROW_STATUS.ERROR);
                rowResult.setErrorMessage(errors);

                failureCount++;
                result.getRowResults().add(rowResult);
                continue;
            }

            try {
                sectionAssignmentService.createStudentAndAssignSection(
                        studentCreate,
                        school,
                        section,
                        studentModelMap,
                        parentModelMap,
                        assignedThisBatch,
                        studentIdsWithSectionAssignments,
                        newAssignments,
                        newStudents,
                        updatedStudents,
                        newParents,
                        newParentUsers
                );

                rowResult.setRowStatus(ROW_STATUS.SUCCESS);
                successCount++;

            } catch (Exception e) {

                rowResult.setRowStatus(ROW_STATUS.ERROR);
                rowResult.setErrorMessage(e.getMessage());
                failureCount++;

            }

            result.getRowResults().add(rowResult);
        }
        result.setFailureCount(failureCount);
        result.setSuccessCount(successCount);

        bulkUploadRepo.findBySection_SectionId(sectionId)
                .ifPresent(bulkUploadRepo::delete);

        parentRepo.saveAll(newParents);
        userRepo.saveAll(newParentUsers);
        studentRepo.saveAll(newStudents);
        studentRepo.saveAll(updatedStudents);
        sectionAssignmentRepo.saveAll(newAssignments);

        bulkUploadRepo.save(result);
    }

    public StudentBulkUploadResponse getBulkUploadResultBySectionId(Long sectionId) {
        return bulkUploadRepo.findBySection_SectionId(sectionId)
                .map(StudentBulkUploadResponse::from)
                .orElseGet(StudentBulkUploadResponse::empty);
    }

}
