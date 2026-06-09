package com.justdeepfried.GyanJyotiLMS.entities.attendance.model;

import com.justdeepfried.GyanJyotiLMS.entities.teacher.model.TeacherModel;
import com.justdeepfried.GyanJyotiLMS.enums.ATTENDANCE_STATUS;
import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import com.justdeepfried.GyanJyotiLMS.entities.subject.model.SubjectModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Filter(name = "schoolIdFilter", condition = "school_id = :schoolId")
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "subject_id", "section_id", "attendance_date"})
)
public class AttendanceModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long attendanceId;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentModel student;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private SectionModel section;

    private LocalDate attendanceDate;

    @Enumerated(EnumType.STRING)
    private ATTENDANCE_STATUS attendanceStatus;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherModel performedBy;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectModel subject;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
