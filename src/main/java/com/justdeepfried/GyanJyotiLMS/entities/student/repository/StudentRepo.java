package com.justdeepfried.GyanJyotiLMS.entities.student.repository;

import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.querydsl.StudentCustomRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepo extends JpaRepository<StudentModel, Long>, JpaSpecificationExecutor<StudentModel>, StudentCustomRepo {

    @Query("""
    SELECT COUNT(s) > 0
    FROM StudentModel s
    JOIN s.parents p
    WHERE LOWER(s.studentName) = LOWER(:studentName)
    AND s.dateOfBirth = :dateOfBirth
    AND p.parentNumber IN :parentNumbers
    """)
    boolean studentAlreadyExists(
           @Param("studentName") String studentName,
           @Param("dateOfBirth") LocalDate dateOfBirth,
           @Param("parentNumbers") List<String> parentNumbers
    );

    @Query("""
    SELECT DISTINCT s
    FROM StudentModel s
    JOIN s.parents p
    WHERE LOWER(s.studentName) = LOWER(:studentName)
    AND s.dateOfBirth = :dateOfBirth
    AND p.parentNumber IN :parentNumbers
    """)
    Optional<StudentModel> findStudentByNameAndDoBAndParentNumber(
            @Param("studentName") String studentName,
            @Param("dateOfBirth") LocalDate dateOfBirth,
            @Param("parentNumbers") List<String> parentNumbers
    );

    @Query("""
    SELECT DISTINCT s
    FROM StudentModel s
    JOIN s.parents p
    WHERE LOWER(s.studentName) = LOWER(:studentName)
    AND s.dateOfBirth = :dateOfBirth
    AND p.parentNumber = :parentNumber
    """)
    Optional<StudentModel> findStudentByNameAndDoBAndParentNumber(
            @Param("studentName") String studentName,
            @Param("dateOfBirth") LocalDate dateOfBirth,
            @Param("parentNumber") String parentNumber
    );

    @Query("""
    SELECT DISTINCT s
    FROM StudentModel s
    JOIN s.parents p
    WHERE LOWER(s.studentName) = LOWER(:studentName)
    AND s.dateOfBirth = :dateOfBirth
    """)
    Optional<StudentModel> findStudentByNameAndDoB(
            @Param("studentName") String studentName,
            @Param("dateOfBirth") LocalDate dateOfBirth
    );

    @Query("SELECT s FROM StudentModel s LEFT JOIN FETCH s.parents WHERE s.school.schoolId = :schoolId ")
    List<StudentModel> findAllStudentsAndParentBySchoolId(@Param("schoolId") Long schoolId);

    @Query("""
    SELECT DISTINCT s
    FROM StudentModel s
    JOIN s.sectionAssignments sa
    WHERE sa.section.sectionId = :sectionId
    """)
    List<StudentModel> findAllStudentsBySectionId(@Param("sectionId") Long sectionId);
}
