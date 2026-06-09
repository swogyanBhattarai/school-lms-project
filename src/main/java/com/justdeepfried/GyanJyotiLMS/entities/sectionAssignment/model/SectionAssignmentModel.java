package com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model;

import com.justdeepfried.GyanJyotiLMS.entities.academicYear.model.AcademicYearModel;
import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import com.justdeepfried.GyanJyotiLMS.superclass.softDelete.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

@Entity
@Setter
@Getter
@Filter(name = "schoolIdFilter", condition = "school_id = :schoolId")
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"student_id", "academicYear_id"})
)
public class SectionAssignmentModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionAssignmentId;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private SectionModel section;

    @ManyToOne
    @JoinColumn(name = "student_id")
    private StudentModel student;

    @ManyToOne
    @JoinColumn(name = "academicYear_id")
    private AcademicYearModel academicYear;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}