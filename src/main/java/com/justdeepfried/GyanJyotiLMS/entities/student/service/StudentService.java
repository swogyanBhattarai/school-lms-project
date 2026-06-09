package com.justdeepfried.GyanJyotiLMS.entities.student.service;

import com.justdeepfried.GyanJyotiLMS.entities.PageResponse;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.attendanceSummary.AttendanceSummary;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.repository.AttendanceRepo;
import com.justdeepfried.GyanJyotiLMS.entities.parent.dtos.ParentCreate;
import com.justdeepfried.GyanJyotiLMS.entities.parent.model.ParentModel;
import com.justdeepfried.GyanJyotiLMS.entities.parent.service.ParentService;
import com.justdeepfried.GyanJyotiLMS.entities.school.context.SchoolContext;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.school.service.SchoolService;
import com.justdeepfried.GyanJyotiLMS.entities.student.dtos.StudentCreate;
import com.justdeepfried.GyanJyotiLMS.entities.student.dtos.StudentDetailResponse;
import com.justdeepfried.GyanJyotiLMS.entities.student.dtos.StudentResponse;
import com.justdeepfried.GyanJyotiLMS.entities.student.dtos.StudentUpdate;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.repository.StudentRepo;
import com.justdeepfried.GyanJyotiLMS.entities.student.specification.StudentSpecification;
import com.justdeepfried.GyanJyotiLMS.entities.user.model.UserModel;
import com.justdeepfried.GyanJyotiLMS.enums.USER_ROLES;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepo studentRepo;
    private final AttendanceRepo attendanceRepo;

    private final SchoolService schoolService;
    private final ParentService parentService;

    private final BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(4);

    @Transactional(readOnly = true)
    public PageResponse<StudentResponse> findAll(String studentName, Long sectionId, Long classId, Boolean hasSectionAssignment, PageRequest pageRequest) {
        Specification<StudentModel> specifications = StudentSpecification.getSpecification(studentName, sectionId, classId, hasSectionAssignment);

        Page<StudentModel> all = studentRepo.findAll(specifications, pageRequest);

        List<Long> studentIds = all.getContent()
                .stream().map(StudentModel::getStudentId)
                .toList();

        if (studentIds.isEmpty()) {
            return PageResponse.from(all.map(StudentResponse::from));
        }

        Map<Long, AttendanceSummary> summaryMap = attendanceRepo.getAttendanceSummary(studentIds)
                .stream().collect(Collectors.toMap(AttendanceSummary::forStudentId, a -> a));

        return PageResponse.from(all.map(student -> StudentResponse.from(
                student,
                summaryMap.getOrDefault(student.getStudentId(), AttendanceSummary.empty())
        )));
    }

    @Transactional(readOnly = true)
    public PageResponse<StudentResponse> findAllFiltered(String studentName, Long sectionId, Long classId, Boolean hasSectionAssignment, PageRequest pageRequest) {

        Page<StudentModel> allFiltered = studentRepo.findAllFiltered(studentName, sectionId, classId, hasSectionAssignment, pageRequest);

        if (allFiltered.isEmpty()) {
            return PageResponse.from(allFiltered.map(StudentResponse::from));
        }

        List<Long> studentIds = allFiltered.getContent()
                .stream().map(StudentModel::getStudentId)
                .toList();

        Map<Long, AttendanceSummary> summaryMap = attendanceRepo.getAttendanceSummary(studentIds)
                .stream().collect(Collectors.toMap(AttendanceSummary::forStudentId, a -> a));

        return PageResponse.from(allFiltered.map(student -> StudentResponse.from(
                student,
                summaryMap.getOrDefault(student.getStudentId(), AttendanceSummary.empty())
        )));
    }

    @Transactional(readOnly = true)
    public StudentDetailResponse findById(Long id) {
        return StudentDetailResponse.from(studentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id)));
    }

    @Transactional(readOnly = true)
    public StudentModel findModelById(Long id) {
        return studentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Student not found with id: " + id));
    }

    public StudentModel createStudent(StudentCreate student) {
        StudentModel studentModel;

        ParentModel parent1, parent2;

        parent1 = parentService.findOrCreateParent(new ParentCreate(
                student.parentName1(),
                student.parentPhoneNumber1()
        ));

        if (student.parentName2() != null && student.parentPhoneNumber2() != null) {
            parent2 = parentService.findOrCreateParent(new ParentCreate(
                    student.parentName2(),
                    student.parentPhoneNumber2()
            ));
        } else {
            parent2 = null;
        }

        String studentName = student.studentName();
        LocalDate dateOfBirth = student.dateOfBirth();


        Optional<StudentModel> existing = studentRepo.findStudentByNameAndDoB(studentName, dateOfBirth);

        if (existing.isPresent()) {
            studentModel = existing.get();

            List<ParentModel> parentModels = studentModel.getParents();

            boolean hasParent1 = parentModels.stream()
                    .anyMatch(p -> parent1.getParentNumber().equals(p.getParentNumber()));

            if (!hasParent1) studentModel.getParents().add(parent1);

            if (parent2 != null) {
                boolean hasParent2 = parentModels.stream()
                        .anyMatch(p -> parent2.getParentNumber().equals(p.getParentNumber()));

                if (!hasParent2) studentModel.getParents().add(parent2);
            }

            return studentRepo.save(studentModel);
        }

        studentModel = new StudentModel();

        studentModel.setStudentName(studentName);
        studentModel.setDateOfBirth(dateOfBirth);
        studentModel.setSchool(schoolService.findById(SchoolContext.get()));

        studentModel.getParents().add(parent1);

        if (parent2 != null) studentModel.getParents().add(parent2);

        return studentRepo.save(studentModel);
    }

    public StudentModel updateStudent(Long id, StudentUpdate studentUpdate) {
        StudentModel studentModel = findModelById(id);

        studentModel.setStudentName(studentUpdate.studentName());
        studentModel.setDateOfBirth(studentUpdate.dateOfBirth());

        // resolve new parents
        ParentModel parent1 = parentService.findOrCreateParent(
                new ParentCreate(studentUpdate.parentName1(), studentUpdate.parentPhoneNumber1())
        );

        ParentModel parent2;
        if (studentUpdate.parentName2() != null && studentUpdate.parentPhoneNumber2() != null) {
            parent2 = parentService.findOrCreateParent(
                    new ParentCreate(studentUpdate.parentName2(), studentUpdate.parentPhoneNumber2())
            );
        } else {
            parent2 = null;
        }

        boolean hasParent1 = studentModel.getParents()
                .stream().anyMatch(
                        sp -> sp.getParentNumber().equals(parent1.getParentNumber())
                                && sp.getParentName().equalsIgnoreCase(parent1.getParentName())
                );

        if (!hasParent1) studentModel.getParents().add(parent1);

        if (parent2 != null) {
            boolean hasParent2 = studentModel.getParents()
                    .stream().anyMatch(
                            sp -> sp.getParentNumber().equals(parent2.getParentNumber())
                                    && sp.getParentName().equalsIgnoreCase(parent2.getParentName())
                    );

            if (!hasParent2) studentModel.getParents().add(parent2);
        }

        return studentRepo.save(studentModel);
    }

    public void deleteById(Long id) {
        attendanceRepo.clearStudentReference(id);
        studentRepo.deleteById(id);
    }


    // THIS IS FOR THE BULK UPLOAD OPTIMIZATION!!!

    public StudentModel studentResolver(
            StudentCreate studentCreate,
            SchoolModel schoolModel,
            Map<String, StudentModel> studentModelMap,
            Map<String, ParentModel> parentModelMap,
            List<StudentModel> newStudents,
            List<StudentModel> updatedStudents,
            List<ParentModel> newParents,
            List<UserModel> newParentUsers
    ) {
        ParentModel parent1 = parentModelMap.computeIfAbsent(studentCreate.parentPhoneNumber1(), k -> {
            ParentModel p = new ParentModel();
            p.setParentName(studentCreate.parentName1());
            p.setParentNumber(studentCreate.parentPhoneNumber1());
            p.setSchool(schoolModel);
            newParents.add(p);

            UserModel u = new UserModel();
            u.setParent(p);
            u.setUsername(studentCreate.parentPhoneNumber1());
            u.setSchool(schoolModel);
            u.setRole(USER_ROLES.PARENT);
            u.setPassword(encoder.encode(studentCreate.parentPhoneNumber1()));
            newParentUsers.add(u);

            return p;
        });

        ParentModel parent2;
        if (studentCreate.parentName2() != null && studentCreate.parentPhoneNumber2() != null) {
            parent2 = parentModelMap.computeIfAbsent(studentCreate.parentPhoneNumber2(), k -> {
                ParentModel p = new ParentModel();
                p.setParentName(studentCreate.parentName2());
                p.setParentNumber(studentCreate.parentPhoneNumber2());
                p.setSchool(schoolModel);
                newParents.add(p);

                UserModel u = new UserModel();
                u.setParent(p);
                u.setUsername(studentCreate.parentPhoneNumber2());
                u.setSchool(schoolModel);
                u.setRole(USER_ROLES.PARENT);
                u.setPassword(encoder.encode(studentCreate.parentPhoneNumber2()));
                newParentUsers.add(u);

                return p;
            });
        } else {
            parent2 = null;
        }

        String key = studentCreate.studentName() + "|" + studentCreate.dateOfBirth();
        StudentModel studentModel = studentModelMap.get(key);

        if (studentModel != null) {
            boolean hasParent1 = studentModel.getParents()
                    .stream().anyMatch(p -> p.getParentNumber().equals(parent1.getParentNumber()));

            if (!hasParent1) {
                studentModel.getParents().add(parent1);
                updatedStudents.add(studentModel);
            }

            if (parent2 != null) {
                boolean hasParent2 = studentModel.getParents()
                        .stream().anyMatch(p -> p.getParentNumber().equals(parent2.getParentNumber()));

                if (!hasParent2) {
                    studentModel.getParents().add(parent2);
                    updatedStudents.add(studentModel);
                }
            }
            return studentModel;
        }

        StudentModel newStudent = new StudentModel();
        newStudent.setStudentName(studentCreate.studentName());
        newStudent.setDateOfBirth(studentCreate.dateOfBirth());
        newStudent.setSchool(schoolModel);

        newStudent.getParents().add(parent1);
        if (parent2 != null) newStudent.getParents().add(parent2);

        studentModelMap.put(key, newStudent);
        newStudents.add(newStudent);
        return newStudent;
    }
}
