package com.justdeepfried.GyanJyotiLMS.entities.subject.model;

import com.justdeepfried.GyanJyotiLMS.entities.attendance.model.AttendanceModel;
import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.model.ClassAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.superclass.softDelete.SoftDeleteEntity;
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
public class SubjectModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long subjectId;

    private String subjectName;

    @OneToMany(mappedBy = "subject", cascade = CascadeType.REMOVE)
    private List<ClassAssignmentModel> classAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "subject")
    private List<AttendanceModel> subjectAttendances = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
