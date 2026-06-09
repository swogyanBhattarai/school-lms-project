package com.justdeepfried.GyanJyotiLMS.entities.teacher.model;

import com.justdeepfried.GyanJyotiLMS.entities.attendance.model.AttendanceModel;
import com.justdeepfried.GyanJyotiLMS.entities.diary.model.DiaryModel;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.model.ClassAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.entities.user.model.UserModel;
import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Filter(name = "schoolIdFilter", condition = "school_id = :schoolId")
public class TeacherModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long teacherId;

    private String teacherName;

    private String teacherPhoneNumber;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<ClassAssignmentModel> classAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "performedBy")
    private List<AttendanceModel> attendances = new ArrayList<>();

    @OneToMany(mappedBy = "createdBy")
    private List<DiaryModel> diaryModels = new ArrayList<>();

    @OneToOne(mappedBy = "teacher", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
