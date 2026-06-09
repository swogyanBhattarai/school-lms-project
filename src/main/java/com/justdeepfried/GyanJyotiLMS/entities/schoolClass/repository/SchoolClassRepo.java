package com.justdeepfried.GyanJyotiLMS.entities.schoolClass.repository;

import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.model.SchoolClassModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolClassRepo extends JpaRepository<SchoolClassModel, Long> {
    boolean existsByAcademicYear_AcademicYearId(Long academicYearId);
    List<SchoolClassModel> findByAcademicYear_IsActiveTrue();
}
