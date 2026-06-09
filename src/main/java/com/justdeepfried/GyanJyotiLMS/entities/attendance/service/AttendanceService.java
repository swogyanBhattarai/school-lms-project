package com.justdeepfried.GyanJyotiLMS.entities.attendance.service;

import com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.AttendanceResponse;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.MassAttendance;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.StudentAttendance;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.attendanceSummary.PerStudentAttendanceSummary;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.model.AttendanceModel;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.repository.AttendanceRepo;
import com.justdeepfried.GyanJyotiLMS.entities.school.context.SchoolContext;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.school.service.SchoolService;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.service.SectionService;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.repository.StudentRepo;
import com.justdeepfried.GyanJyotiLMS.entities.subject.model.SubjectModel;
import com.justdeepfried.GyanJyotiLMS.entities.subject.service.SubjectService;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.model.TeacherModel;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.service.TeacherService;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceAlreadyExistsException;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceNotFoundException;
import com.justdeepfried.GyanJyotiLMS.exception.ValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AttendanceService {

    private final AttendanceRepo attendanceRepo;
    private final StudentRepo studentRepo;

    private final SchoolService schoolService;
    private final TeacherService teacherService;
    private final SectionService sectionService;
    private final SubjectService subjectService;

    @Transactional(readOnly = true)
    public List<AttendanceModel> findAll() {
        return attendanceRepo.findAll();
    }

    @Transactional(readOnly = true)
    public AttendanceModel findById(Long id) {
        return attendanceRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Attendance record not found with id: " + id));
    }

    public AttendanceModel save(AttendanceModel attendance) {
        attendance.setSchool(schoolService.findById(SchoolContext.get()));
        return attendanceRepo.save(attendance);
    }

    public List<AttendanceResponse> findByTeacherIdSectionIdAndSubjectIdAndAttendanceDate(Long sectionId, Long subjectId, Long teacherId) {
        LocalDate today = LocalDate.now();
        return attendanceRepo.findByTeacherIdSectionIdAndSubjectIdAndAttendanceDate(sectionId, subjectId, today, teacherId).stream()
                .map(AttendanceResponse::from)
                .toList();
    }

    public List<PerStudentAttendanceSummary> findAllFilteredForStudent(Long studentId, Long subjectId, Long sectionId, LocalDate fromDate, LocalDate toDate) {
        if (fromDate != null && toDate != null && fromDate.isAfter(toDate)) throw new ValidationException("From date cannot be after To date!");

        if (sectionId == null || studentId == null) throw new ValidationException("StudentId and SectionId cannot be empty!");

        return attendanceRepo.findAllFilteredForStudent(studentId, subjectId, sectionId, fromDate, toDate);
    }


    @Transactional(readOnly = true)
    public List<AttendanceResponse> findAllByStudentIdAndDate(Long studentId, LocalDate date) {
        return attendanceRepo.findAllByStudentStudentIdAndAttendanceDate(studentId, date).stream()
                .map(AttendanceResponse::from)
                .toList();
    }

    public void massCreateAttendance(Long sectionId, Long subjectId, Long teacherId, MassAttendance massAttendance) {
        LocalDate today = LocalDate.now();

        if (attendanceRepo.existsBySectionIdAndSubjectIdAndAttendanceDate(
                sectionId,
                subjectId,
                today
        )) throw new ResourceAlreadyExistsException("Attendance has already been recorded!");

        Map<Long, StudentModel> studentModelMap = new HashMap<>();
        List<AttendanceModel> newAttendance = new ArrayList<>();

        Set<Long> submittedStudentIds = massAttendance.studentAttendances()
                        .stream().map(StudentAttendance::studentId)
                        .collect(Collectors.toSet());

        studentRepo.findAllStudentsBySectionId(sectionId)
                .forEach(s -> studentModelMap.put(s.getStudentId(), s));

        if (!studentModelMap.keySet().equals(submittedStudentIds))
            throw new ValidationException("Cannot post attendance without all students!");

        SchoolModel school = schoolService.findById(SchoolContext.get());
        TeacherModel performedBy = teacherService.findById(teacherId);
        SectionModel section = sectionService.findModelById(sectionId);
        SubjectModel subject = subjectService.findById(subjectId);


        for (StudentAttendance attendance : massAttendance.studentAttendances()) {
            StudentModel studentModel = studentModelMap.get(attendance.studentId());

            if (studentModel == null) throw new ResourceNotFoundException("Student not found with id: " + attendance.studentId());

            AttendanceModel a = new AttendanceModel();

            a.setStudent(studentModel);
            a.setSection(section);
            a.setSubject(subject);
            a.setSchool(school);
            a.setPerformedBy(performedBy);
            a.setAttendanceDate(today);
            a.setAttendanceStatus(attendance.attendanceStatus());

            newAttendance.add(a);
        }

        attendanceRepo.saveAll(newAttendance);
    }
}
