package com.justdeepfried.GyanJyotiLMS.entities.parent.model;

import com.justdeepfried.GyanJyotiLMS.entities.user.model.UserModel;
import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
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
public class ParentModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long parentId;

    private String parentName;

    private String parentNumber;

    @ManyToMany(mappedBy = "parents")
    private List<StudentModel> children = new ArrayList<>();

    @OneToOne(mappedBy = "parent")
    private UserModel user;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
