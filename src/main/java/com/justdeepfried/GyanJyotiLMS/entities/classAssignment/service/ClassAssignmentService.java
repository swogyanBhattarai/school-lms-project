package com.justdeepfried.GyanJyotiLMS.entities.classAssignment.service;

import com.justdeepfried.GyanJyotiLMS.entities.attendance.service.AttendanceService;
import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.dtos.ClassAssignmentAttendanceResponse;
import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.dtos.ClassAssignmentCreate;
import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.dtos.ClassAssignmentResponse;
import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.dtos.ClassAssignmentUpdate;
import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.model.ClassAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.repository.ClassAssignmentRepo;
import com.justdeepfried.GyanJyotiLMS.entities.school.context.SchoolContext;
import com.justdeepfried.GyanJyotiLMS.entities.school.service.SchoolService;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.service.SectionService;
import com.justdeepfried.GyanJyotiLMS.entities.subject.model.SubjectModel;
import com.justdeepfried.GyanJyotiLMS.entities.subject.service.SubjectService;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.model.TeacherModel;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.service.TeacherService;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceAlreadyExistsException;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.awt.print.Pageable;
import java.time.LocalDate;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class ClassAssignmentService {

    private final ClassAssignmentRepo classAssignmentRepo;
    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final SectionService sectionService;
    private final SchoolService schoolService;
    private final AttendanceService attendanceService;

    @Transactional(readOnly = true)
    public List<ClassAssignmentModel> findAll() {
        return classAssignmentRepo.findAll();
    }

    @Transactional(readOnly = true)
    public List<ClassAssignmentResponse> findAllBySectionId(Long sectionId) {
        return classAssignmentRepo.findAllBySection_SectionId(sectionId).stream()
                .map(ClassAssignmentResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public ClassAssignmentModel findById(Long id) {
        return classAssignmentRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Class Assignment not found with id: " + id));
    }

    @Transactional(readOnly = true)
    public List<ClassAssignmentAttendanceResponse> findAllByTeacherId(Long teacherId) {
        LocalDate today = LocalDate.now();
        return classAssignmentRepo.findAllByTeacherId(teacherId, today);
    }

    public ClassAssignmentResponse createClassAssignment(ClassAssignmentCreate create) {
        ClassAssignmentModel model = new ClassAssignmentModel();

        TeacherModel existingTeacher = teacherService.findById(create.teacherId());
        SubjectModel existingSubject = subjectService.findById(create.subjectId());
        SectionModel existingSection = sectionService.findModelById(create.sectionId());

        if (classAssignmentRepo.existsBySection_SectionIdAndSubject_SubjectId(create.sectionId(), create.subjectId())) {
            throw new ResourceAlreadyExistsException("Class Assignment already exists!");
        }
        model.setTeacher(existingTeacher);
        model.setTeacherRole(create.teacherRole());

        model.setSubject(existingSubject);

        model.setSection(existingSection);

        model.setSchool(schoolService.findById(SchoolContext.get()));

        return ClassAssignmentResponse.from(classAssignmentRepo.save(model));
    }

    public ClassAssignmentResponse updateClassAssignment(Long id, ClassAssignmentUpdate update) {
        ClassAssignmentModel model = findById(id);

        model.setTeacher(teacherService.findById(update.teacherId()));
        model.setTeacherRole(update.teacherRole());

        model.setSubject(subjectService.findById(update.subjectId()));
        model.setSection(sectionService.findModelById(update.sectionId()));
        return ClassAssignmentResponse.from(classAssignmentRepo.save(model));
    }

    public void deleteById(Long id) {
        classAssignmentRepo.deleteById(id);
    }
}
