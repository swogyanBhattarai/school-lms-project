package com.justdeepfried.GyanJyotiLMS.entities.classAssignment.repository;

import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.dtos.ClassAssignmentAttendanceResponse;
import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.model.ClassAssignmentModel;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ClassAssignmentRepo extends JpaRepository<ClassAssignmentModel, Long> {

    @Query("SELECT a FROM ClassAssignmentModel a")
    @EntityGraph("classAssignment.teacherSubjectSection")
    List<ClassAssignmentModel> findAllWithEagerLoading();

    @EntityGraph("classAssignment.teacherSubject")
    List<ClassAssignmentModel> findAllBySection_SectionId(Long sectionId);

    boolean existsBySection_SectionIdAndSubject_SubjectId(Long sectionId, Long subjectId);

    boolean existsBySection_SectionId(Long sectionId);

    @Query("""
    SELECT NEW com.justdeepfried.GyanJyotiLMS.entities.classAssignment.dtos.ClassAssignmentAttendanceResponse (
        ca.classAssignmentId,
        se.sectionId,
        sb.subjectId,
        ta.teacherName,
        ca.teacherRole,
        sb.subjectName,
        se.sectionName,
        sc.grade,
        ay.academicYear,
        (SELECT COUNT(sa) FROM SectionAssignmentModel sa WHERE sa.section.sectionId = se.sectionId),
            (SELECT COUNT(am) > 0 FROM AttendanceModel am
                WHERE am.section.sectionId = se.sectionId
                AND am.subject.subjectId = sb.subjectId
                AND am.performedBy.teacherId = ta.teacherId
                AND am.attendanceDate = :attendanceDate
            )
        )
    FROM ClassAssignmentModel ca
    JOIN ca.teacher ta
    JOIN ca.subject sb
    JOIN ca.section se
    JOIN se.schoolClass sc
    JOIN sc.academicYear ay
    WHERE ta.teacherId = :teacherId
    AND ay.isActive = true
    """)
    List<ClassAssignmentAttendanceResponse> findAllByTeacherId(@Param("teacherId") Long teacherId, @Param("attendanceDate") LocalDate attendanceDate);
}
