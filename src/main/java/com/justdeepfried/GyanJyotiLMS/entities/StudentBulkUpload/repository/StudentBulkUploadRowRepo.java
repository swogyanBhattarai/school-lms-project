package com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.repository;

import com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.model.StudentBulkUploadRowModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentBulkUploadRowRepo extends JpaRepository<StudentBulkUploadRowModel, Long> {
}
