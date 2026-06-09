package com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.repository;

import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;
import lombok.extern.java.Log;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SectionAssignmentRepo extends JpaRepository<SectionAssignmentModel, Long>, JpaSpecificationExecutor<SectionAssignmentModel> {
    List<SectionAssignmentModel> findAllBySection_SectionId(Long sectionId);

    boolean existsBySection_SectionIdAndStudent_StudentId(Long sectionId, Long studentId);

    boolean existsBySection_SectionId(Long sectionId);

    @Query("""
    SELECT sa FROM SectionAssignmentModel sa
    JOIN FETCH sa.section s
    JOIN FETCH s.schoolClass sc
    JOIN FETCH sc.academicYear ay
    WHERE sa.student.studentId = :studentId
    AND ay.isActive = true
    """)
    Optional<SectionAssignmentModel> findActiveByStudentId(@Param("studentId") Long studentId);

    boolean existsByStudent_StudentIdAndAcademicYear_AcademicYearId(Long studentId, Long academicYearId);

    @Query("""
    SELECT sa.student.studentId FROM SectionAssignmentModel sa
    WHERE sa.academicYear.academicYearId = :academicYearId
    """)
    List<Long> findStudentIdsByAcademicYearId(@Param("academicYearId") Long academicYearId);

    void deleteAllBySection_SectionId(Long sectionId);
}
