package com.justdeepfried.GyanJyotiLMS.entities.student.model;

import com.justdeepfried.GyanJyotiLMS.entities.attendance.model.AttendanceModel;
import com.justdeepfried.GyanJyotiLMS.entities.studentFee.model.StudentFeeModel;
import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.parent.model.ParentModel;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.superclass.softDelete.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Filter(name = "schoolIdFilter", condition = "school_id = :schoolId")
public class StudentModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long studentId;

    private String studentName;

    private LocalDate dateOfBirth;

    @OneToMany(mappedBy = "student", cascade = CascadeType.REMOVE)
    private List<SectionAssignmentModel> sectionAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<AttendanceModel> studentAttendances = new ArrayList<>();

    @OneToMany(mappedBy = "student")
    private List<StudentFeeModel> studentFees = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "student_parents",
            joinColumns = @JoinColumn(name = "student_id"),
            inverseJoinColumns = @JoinColumn(name = "parent_id"),
            uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "parent_id"})
    )
    private List<ParentModel> parents = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
