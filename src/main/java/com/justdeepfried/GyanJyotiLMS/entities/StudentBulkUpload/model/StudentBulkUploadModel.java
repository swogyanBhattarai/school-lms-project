package com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.model;

import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.superclass.base.BaseEntity;
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
public class StudentBulkUploadModel extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long BulkUploadId;

    private int successCount;
    private int failureCount;

    @OneToMany(mappedBy = "bulkUploadModel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<StudentBulkUploadRowModel> rowResults = new ArrayList<>();

    @OneToOne
    @JoinColumn(name = "section_id")
    private SectionModel section;

    @ManyToOne
    @JoinColumn(name = "school_id")
    private SchoolModel school;
}
