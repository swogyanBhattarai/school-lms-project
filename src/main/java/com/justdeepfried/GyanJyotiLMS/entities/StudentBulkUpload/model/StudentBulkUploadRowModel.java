package com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.model;

import com.justdeepfried.GyanJyotiLMS.enums.ROW_STATUS;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Entity
@Setter
@Getter
public class StudentBulkUploadRowModel {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long bulkUploadRowId;

    private int rowNumber;

    private String studentName;
    private LocalDate dateOfBirth;
    private String parentName1;
    private String parentPhoneNumber1;
    private String parentName2;
    private String parentPhoneNumber2;

    @Enumerated(EnumType.STRING)
    private ROW_STATUS rowStatus;

    private String errorMessage;

    @ManyToOne
    @JoinColumn(name = "bulk_upload_result_id")
    private StudentBulkUploadModel bulkUploadModel;
}
