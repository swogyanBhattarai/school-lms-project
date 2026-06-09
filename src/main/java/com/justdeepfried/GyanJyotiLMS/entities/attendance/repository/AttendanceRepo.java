package com.justdeepfried.GyanJyotiLMS.entities.attendance.repository;

import com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.attendanceSummary.AttendanceSummary;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.model.AttendanceModel;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.querydsl.AttendanceCustomRepo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface AttendanceRepo extends JpaRepository<AttendanceModel, Long>, AttendanceCustomRepo {

    @Query("""
    SELECT COUNT(a) > 0
    FROM AttendanceModel a
    WHERE a.section.sectionId = :sectionId
    AND a.subject.subjectId = :subjectId
    AND a.attendanceDate = :attendanceDate
    """)
    boolean existsBySectionIdAndSubjectIdAndAttendanceDate(
            @Param("sectionId") Long sectionId,
            @Param("subjectId") Long subjectId,
            @Param("attendanceDate") LocalDate attendanceDate
            );

    @Query("""
    SELECT a FROM AttendanceModel a
    WHERE a.section.sectionId = :sectionId
    AND a.performedBy.teacherId = :teacherId
    AND a.subject.subjectId = :subjectId
    AND a.attendanceDate = :attendanceDate
    """)
    List<AttendanceModel> findByTeacherIdSectionIdAndSubjectIdAndAttendanceDate(
            @Param("sectionId") Long sectionId,
            @Param("subjectId") Long subjectId,
            @Param("attendanceDate") LocalDate attendanceDate,
            @Param("teacherId") Long teacherId
    );

    @Modifying
    @Query("""
    UPDATE AttendanceModel am
    SET am.performedBy = null
    WHERE am.performedBy.teacherId = :teacherId
    """)
    void clearTeacherReference(@Param("teacherId") Long teacherId);

    @Modifying
    @Query("""
    UPDATE AttendanceModel am
    SET am.student = null
    WHERE am.student.studentId = :studentId
    """)
    void clearStudentReference(@Param("studentId") Long studentId);

    List<AttendanceModel> findAllByStudentStudentIdAndAttendanceDate(Long studentId, LocalDate attendanceDate);

    @Query("""
    SELECT NEW com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.attendanceSummary.AttendanceSummary (
        a.student.studentId,
        SUM(CASE WHEN a.attendanceStatus = 'PRESENT' THEN 1 ELSE 0 END),
        SUM(CASE WHEN a.attendanceStatus = 'ABSENT' THEN 1 ELSE 0 END),
        COUNT(a)
        )
    FROM AttendanceModel a
    JOIN a.section se
    JOIN se.schoolClass sc
    JOIN sc.academicYear ay
    WHERE a.student.studentId IN :studentIds
    AND ay.isActive = true
    GROUP BY a.student.studentId
    """)
    List<AttendanceSummary> getAttendanceSummary(@Param("studentIds") List<Long> studentIds);
}
