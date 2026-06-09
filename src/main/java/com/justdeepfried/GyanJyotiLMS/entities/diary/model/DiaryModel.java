package com.justdeepfried.GyanJyotiLMS.entities.diary.model;

import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.subject.model.SubjectModel;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.model.TeacherModel;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
@Filter(name = "schoolIdFilter", condition = "school_id = :schoolId")
public class DiaryModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long diaryId;

    private LocalDate diaryDate;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectModel subject;

    private String title;

    private String content;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherModel createdBy;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private SectionModel section;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
