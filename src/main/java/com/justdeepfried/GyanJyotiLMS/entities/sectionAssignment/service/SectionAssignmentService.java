package com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.service;

import com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.repository.StudentBulkUploadRepo;
import com.justdeepfried.GyanJyotiLMS.entities.parent.model.ParentModel;
import com.justdeepfried.GyanJyotiLMS.entities.school.context.SchoolContext;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.school.service.SchoolService;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.repository.SectionRepo;
import com.justdeepfried.GyanJyotiLMS.entities.section.service.SectionService;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.dtos.SectionAssignmentCreate;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.dtos.SectionAssignmentResponse;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.repository.SectionAssignmentRepo;
import com.justdeepfried.GyanJyotiLMS.entities.student.dtos.StudentCreate;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.repository.StudentRepo;
import com.justdeepfried.GyanJyotiLMS.entities.student.service.StudentService;
import com.justdeepfried.GyanJyotiLMS.entities.user.model.UserModel;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceAlreadyExistsException;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.Set;

@Service
@Transactional
@RequiredArgsConstructor
public class SectionAssignmentService {
    private final SectionAssignmentRepo sectionAssignmentRepo;
    private final SchoolService schoolService;
    private final SectionRepo sectionRepo;
    private final StudentRepo studentRepo;
    private final StudentBulkUploadRepo bulkUploadRepo;

    private final SectionService sectionService;
    private final StudentService studentService;

    public SectionAssignmentModel createSectionAssignment(SectionModel section, StudentModel student) {
        SectionAssignmentModel sectionAssignmentModel = new SectionAssignmentModel();
        sectionAssignmentModel.setSection(section);
        sectionAssignmentModel.setStudent(student);
        sectionAssignmentModel.setSchool(schoolService.findById(SchoolContext.get()));
        sectionAssignmentModel.setAcademicYear(section.getSchoolClass().getAcademicYear());

        return sectionAssignmentRepo.save(sectionAssignmentModel);
    }

    public SectionAssignmentResponse createSectionAssignment(SectionAssignmentCreate create) {
        SectionModel section = sectionRepo.findById(create.sectionId())
                .orElseThrow(() -> new ResourceNotFoundException("Section not found"));
        StudentModel student = studentRepo.findById(create.studentId())
                .orElseThrow(() -> new ResourceNotFoundException("Student not found"));

        return SectionAssignmentResponse.from(createSectionAssignment(section, student));
    }

    public SectionAssignmentResponse createStudentAndAssignSection(Long sectionId, StudentCreate studentCreate) {
        StudentModel studentModel = studentService.createStudent(studentCreate);

        SectionModel sectionModel = sectionService.findModelById(sectionId);

        if (sectionAssignmentRepo.existsByStudent_StudentIdAndAcademicYear_AcademicYearId(studentModel.getStudentId(), sectionModel.getSchoolClass().getAcademicYear().getAcademicYearId())) {
            throw new ResourceAlreadyExistsException("Section assignment already exists!");
        }

        SectionAssignmentModel assignmentModel = new SectionAssignmentModel();

        assignmentModel.setStudent(studentModel);
        assignmentModel.setSection(sectionModel);
        assignmentModel.setSchool(schoolService.findById(SchoolContext.get()));
        assignmentModel.setAcademicYear(sectionModel.getSchoolClass().getAcademicYear());

        return SectionAssignmentResponse.from(sectionAssignmentRepo.save(assignmentModel));
    }

    @Transactional(readOnly = true)
    public List<SectionAssignmentResponse> findAllBySectionId(Long sectionId) {
        return sectionAssignmentRepo.findAllBySection_SectionId(sectionId)
                .stream()
                .map(SectionAssignmentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public SectionAssignmentResponse findById(Long id) {
        return sectionAssignmentRepo.findById(id)
                .map(SectionAssignmentResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Section assignment not found"));
    }

    @Transactional(readOnly = true)
    public SectionAssignmentResponse findByStudentIdAndActiveYear(Long studentId) {
        return sectionAssignmentRepo.findActiveByStudentId(studentId)
                .map(SectionAssignmentResponse::from)
                .orElseThrow(() -> new ResourceNotFoundException("Section assignment not found"));
    }

    public void deleteById(Long id) {
        sectionAssignmentRepo.deleteById(id);
    }

    public void deleteAllBySectionId(Long sectionId) {
        bulkUploadRepo.deleteAllBySection_SectionId(sectionId);
        sectionAssignmentRepo.deleteAllBySection_SectionId(sectionId);
    }



    // THIS IS FOR THE BULK UPLOAD OPTIMIZATION!!!

    public void createStudentAndAssignSection(
            StudentCreate studentCreate,
            SchoolModel schoolModel,
            SectionModel sectionModel,
            Map<String, StudentModel> studentModelMap,
            Map<String, ParentModel> parentModelMap,
            Set<StudentModel> assignedThisBatch,
            List<Long> studentIdsWithSectionAssignments,
            List<SectionAssignmentModel> newAssignments,
            List<StudentModel> newStudents,
            List<StudentModel> updatedStudents,
            List<ParentModel> newParents,
            List<UserModel> newParentUsers
    ) {
        StudentModel studentModel = studentService.studentResolver(studentCreate, schoolModel, studentModelMap, parentModelMap, newStudents, updatedStudents, newParents, newParentUsers);

        boolean sectionAssignmentExists = (studentModel.getStudentId() != null && studentIdsWithSectionAssignments.contains(studentModel.getStudentId()))
                || assignedThisBatch.contains(studentModel);

        if (sectionAssignmentExists) {
            throw new ResourceAlreadyExistsException("Section assignment already exists!");
        }

        SectionAssignmentModel assignmentModel = new SectionAssignmentModel();

        assignmentModel.setStudent(studentModel);
        assignmentModel.setSection(sectionModel);
        assignmentModel.setSchool(schoolModel);
        assignmentModel.setAcademicYear(sectionModel.getSchoolClass().getAcademicYear());

        newAssignments.add(assignmentModel);
        assignedThisBatch.add(studentModel);
    }
}
