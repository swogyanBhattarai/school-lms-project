package com.justdeepfried.GyanJyotiLMS.entities.user.model;

import com.justdeepfried.GyanJyotiLMS.entities.parent.model.ParentModel;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.model.TeacherModel;
import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.enums.USER_ROLES;
import com.justdeepfried.GyanJyotiLMS.superclass.softDelete.SoftDeleteEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Filter;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Setter
@Getter
@Filter(name = "schoolIdFilter", condition = "school_id = :schoolId")
public class UserModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;

    @Column(nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private USER_ROLES role;

    @OneToOne
    @JoinColumn(name = "parent_id")
    private ParentModel parent;

    @OneToOne
    @JoinColumn(name = "teacher_id")
    private TeacherModel teacher;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
