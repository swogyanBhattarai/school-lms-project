package com.justdeepfried.GyanJyotiLMS.entities.academicYear.model;

import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.model.SchoolClassModel;
import com.justdeepfried.GyanJyotiLMS.superclass.softDelete.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Filter;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Filter(name = "schoolIdFilter", condition = "school_id = :schoolId")
public class AcademicYearModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long academicYearId;

    private String academicYear;

    private LocalDate startDate;

    private LocalDate endDate;

    private boolean isActive = false;

    @OneToMany(mappedBy = "academicYear")
    @BatchSize(size = 30)
    private List<SchoolClassModel> classes = new ArrayList<>();

    @OneToMany(mappedBy = "academicYear")
    @BatchSize(size = 30)
    private List<SectionAssignmentModel> sectionAssignments = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
