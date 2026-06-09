package com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.repository;

import com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.model.StudentBulkUploadModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StudentBulkUploadRepo extends JpaRepository<StudentBulkUploadModel, Long> {
    Optional<StudentBulkUploadModel> findBySection_SectionId(Long sectionId);
    void deleteAllBySection_SectionId(Long sectionId);
}
