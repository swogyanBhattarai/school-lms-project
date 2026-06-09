package com.justdeepfried.GyanJyotiLMS.entities.schoolClass.model;

import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.academicYear.model.AcademicYearModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.superclass.softDelete.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.BatchSize;
import org.hibernate.annotations.Filter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Setter
@Getter
@Filter(name = "schoolIdFilter", condition = "school_id = :schoolId")
public class SchoolClassModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long schoolClassId;

    private String grade;

    @OneToMany(mappedBy = "schoolClass")
    @BatchSize(size = 30)
    private List<SectionModel> sections = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "academicYear_id")
    private AcademicYearModel academicYear;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
