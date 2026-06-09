package com.justdeepfried.GyanJyotiLMS.entities.attendance.querydsl;

import com.justdeepfried.GyanJyotiLMS.entities.academicYear.model.QAcademicYearModel;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.dtos.attendanceSummary.PerStudentAttendanceSummary;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.model.AttendanceModel;
import com.justdeepfried.GyanJyotiLMS.entities.attendance.model.QAttendanceModel;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.model.QSchoolClassModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.QSectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.QStudentModel;
import com.justdeepfried.GyanJyotiLMS.entities.subject.model.QSubjectModel;
import com.justdeepfried.GyanJyotiLMS.enums.ATTENDANCE_STATUS;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Projections;
import com.querydsl.core.types.dsl.CaseBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AttendanceCustomRepoImpl implements AttendanceCustomRepo {

    private final JPAQueryFactory queryFactory;

    @Override
    public List<PerStudentAttendanceSummary> findAllFilteredForStudent(Long studentId, Long subjectId, Long sectionId, LocalDate fromDate, LocalDate toDate) {
        QAttendanceModel attendance = QAttendanceModel.attendanceModel;
        QSubjectModel subject = QSubjectModel.subjectModel;
        QStudentModel student = QStudentModel.studentModel;
        QSectionModel section = QSectionModel.sectionModel;
        QSchoolClassModel schoolClass = QSchoolClassModel.schoolClassModel;
        QAcademicYearModel academicYear = QAcademicYearModel.academicYearModel;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(student.studentId.eq(studentId));
        booleanBuilder.and(section.sectionId.eq(sectionId));

        if (toDate != null && fromDate != null) {
            booleanBuilder.and(attendance.attendanceDate.between(fromDate, toDate));
        }
        if (subjectId != null) {
            booleanBuilder.and(subject.subjectId.eq(subjectId));
        }

        booleanBuilder.and(academicYear.isActive.isTrue());

        return queryFactory
                .select(Projections.constructor(PerStudentAttendanceSummary.class,
                        subject.subjectId,
                        subject.subjectName,
                        new CaseBuilder()
                                .when(attendance.attendanceStatus.eq(ATTENDANCE_STATUS.valueOf("PRESENT"))).then(1L)
                                .otherwise(0L)
                                .sum(),
                        new CaseBuilder()
                                .when(attendance.attendanceStatus.eq(ATTENDANCE_STATUS.valueOf("ABSENT"))).then(1L)
                                .otherwise(0L)
                                .sum(),
                        new CaseBuilder()
                                .when(attendance.attendanceStatus.eq(ATTENDANCE_STATUS.valueOf("LEAVE"))).then(1L)
                                .otherwise(0L)
                                .sum(),
                        attendance.count()
                ))
                .from(attendance)
                .leftJoin(attendance.subject, subject)
                .join(attendance.section, section)
                .join(section.schoolClass, schoolClass)
                .join(schoolClass.academicYear, academicYear)
                .join(attendance.student, student)
                .where(booleanBuilder)
                .groupBy(subject.subjectId, subject.subjectName, student.studentId)
                .fetch();
    }

    @Override
    public List<AttendanceModel> findAllByStudentIdAndSectionId(Long studentId, Long sectionId) {
        return List.of();
    }
}
