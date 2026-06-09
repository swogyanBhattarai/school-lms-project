package com.justdeepfried.GyanJyotiLMS.entities.teacher.service;

import java.util.List;

import com.justdeepfried.GyanJyotiLMS.entities.attendance.repository.AttendanceRepo;
import com.justdeepfried.GyanJyotiLMS.entities.diary.repository.DiaryRepo;
import com.justdeepfried.GyanJyotiLMS.entities.user.model.UserModel;
import com.justdeepfried.GyanJyotiLMS.enums.USER_ROLES;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceAlreadyExistsException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.justdeepfried.GyanJyotiLMS.entities.school.context.SchoolContext;
import com.justdeepfried.GyanJyotiLMS.entities.school.service.SchoolService;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.dtos.TeacherCreate;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.dtos.TeacherResponse;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.dtos.TeacherUpdate;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.model.TeacherModel;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.repository.TeacherRepo;
import com.justdeepfried.GyanJyotiLMS.entities.user.repository.UserRepository;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceNotFoundException;

import lombok.RequiredArgsConstructor;

@Service
@Transactional
@RequiredArgsConstructor
public class TeacherService {

    private final TeacherRepo teacherRepo;
    private final SchoolService schoolService;

    private final UserRepository userRepo;
    private final DiaryRepo diaryRepo;
    private final AttendanceRepo attendanceRepo;

    private final BCryptPasswordEncoder encoder;

    @Transactional(readOnly = true)
    public List<TeacherResponse> findAll() {
        return teacherRepo.findAll().stream()
                .map(TeacherResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TeacherModel findById(Long id) {
        return teacherRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));
    }

    public TeacherModel save(TeacherCreate teacherCreate) {

        if (teacherRepo.existsByTeacherName(teacherCreate.teacherName())) throw new ResourceAlreadyExistsException("Teacher already exists!");

        String username = teacherCreate.teacherName().toLowerCase().replaceAll("\\s", ".");

        if (userRepo.existsByUsername(username)) throw new ResourceAlreadyExistsException("User already exists!");

        TeacherModel teacherModel = new TeacherModel();
        teacherModel.setTeacherName(teacherCreate.teacherName());
        teacherModel.setTeacherPhoneNumber(teacherCreate.teacherPhoneNumber());
        teacherModel.setSchool(schoolService.findById(SchoolContext.get()));


        TeacherModel savedTeacher = teacherRepo.save(teacherModel);

        UserModel userModel = new UserModel();

        userModel.setUsername(username);
        userModel.setPassword(encoder.encode(username));
        userModel.setSchool(schoolService.findById(SchoolContext.get()));
        userModel.setRole(USER_ROLES.TEACHER);
        userModel.setTeacher(savedTeacher);

        UserModel savedUser = userRepo.save(userModel);
        savedTeacher.setUser(savedUser);
        return savedTeacher;
    }

    public TeacherModel update(Long id, TeacherUpdate teacherUpdate) {
        TeacherModel teacherModel = findById(id);

        teacherModel.setTeacherName(teacherUpdate.teacherName());
        teacherModel.setTeacherPhoneNumber(teacherUpdate.teacherPhoneNumber());

        return teacherRepo.save(teacherModel);
    }

    public void deleteById(Long id) {
        TeacherModel teacherModel = teacherRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Teacher not found with id: " + id));

        diaryRepo.clearTeacherReference(id);
        attendanceRepo.clearTeacherReference(id);
        teacherRepo.delete(teacherModel);
    }
}
