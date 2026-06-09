package com.justdeepfried.GyanJyotiLMS.entities.teacher.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.model.ClassAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.model.TeacherModel;

import java.util.List;

public record TeacherResponse (
        Long teacherId,
        String teacherName,
        String teacherPhoneNumber,
        TeacherAssignmentResponse assignmentResponse
){
    public static TeacherResponse from(TeacherModel teacherModel) {

        List<ClassAssignmentModel> classAssignments = teacherModel.getClassAssignments()
                .stream().filter(ca -> ca.getSection().getSchoolClass().getAcademicYear().isActive())
                .toList();

        return new TeacherResponse(
                teacherModel.getTeacherId(),
                teacherModel.getTeacherName(),
                teacherModel.getTeacherPhoneNumber(),
                TeacherAssignmentResponse.from(classAssignments)
        );
    }
}
