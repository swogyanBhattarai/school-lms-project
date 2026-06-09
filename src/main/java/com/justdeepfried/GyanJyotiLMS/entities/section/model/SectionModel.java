package com.justdeepfried.GyanJyotiLMS.entities.section.model;

import com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.model.StudentBulkUploadModel;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.model.AttendanceModel;
import com.justdeepfried.GyanJyotiLMS.entities.diary.model.DiaryModel;
import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.model.ClassAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.model.SchoolClassModel;
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
public class SectionModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long sectionId;

    private String sectionName;

    @OneToMany(mappedBy = "section")
    private List<SectionAssignmentModel> sectionAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "section")
    private List<ClassAssignmentModel> classAssignments = new ArrayList<>();

    @OneToMany(mappedBy = "section")
    private List<AttendanceModel> sectionAttendances = new ArrayList<>();

    @ManyToOne
    @JoinColumn(name = "schoolClass_id")
    private SchoolClassModel schoolClass;

    @OneToMany(mappedBy = "section", cascade = CascadeType.REMOVE)
    private List<DiaryModel> diaryModels;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
