package com.justdeepfried.GyanJyotiLMS.entities.classAssignment.model;

import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.subject.model.SubjectModel;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.model.TeacherModel;
import com.justdeepfried.GyanJyotiLMS.enums.TEACHER_ROLES;
import com.justdeepfried.GyanJyotiLMS.superclass.softDelete.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;

@Entity
@Setter
@Getter
@NamedEntityGraphs(
        {
                @NamedEntityGraph(
                        name = "classAssignment.teacherSubject",
                        attributeNodes = {
                                @NamedAttributeNode(value = "teacher"),
                                @NamedAttributeNode(value = "subject")
                        }
                        ),
                @NamedEntityGraph(
                        name = "classAssignment.teacherSubjectSection",
                        attributeNodes = {
                                @NamedAttributeNode(value = "teacher"),
                                @NamedAttributeNode(value = "subject"),
                                @NamedAttributeNode(value = "section")
                        }
                )
        }
)
@Table(
        uniqueConstraints = @UniqueConstraint(columnNames = {"section_id", "subject_id"})
)
@Filter(name = "schoolIdFilter", condition = "school_id = :schoolId")
public class ClassAssignmentModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long classAssignmentId;

    @ManyToOne
    @JoinColumn(name = "teacher_id")
    private TeacherModel teacher;

    @Enumerated(EnumType.STRING)
    private TEACHER_ROLES teacherRole;

    @ManyToOne
    @JoinColumn(name = "subject_id")
    private SubjectModel subject;

    @ManyToOne
    @JoinColumn(name = "section_id")
    private SectionModel section;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
