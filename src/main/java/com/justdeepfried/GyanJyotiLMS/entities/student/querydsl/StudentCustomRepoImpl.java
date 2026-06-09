package com.justdeepfried.GyanJyotiLMS.entities.student.querydsl;

import com.justdeepfried.GyanJyotiLMS.entities.academicYear.model.QAcademicYearModel;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.QSectionAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.QStudentModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.jpa.impl.JPAQueryFactory;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class StudentCustomRepoImpl implements StudentCustomRepo {
    private final JPAQueryFactory queryFactory;

    @Override
    public Page<StudentModel> findAllFiltered(String studentName, Long sectionId, Long classId, Boolean hasSectionAssignment, PageRequest pageRequest) {

        QStudentModel studentModel = QStudentModel.studentModel;
        QSectionAssignmentModel sectionAssignmentModel = QSectionAssignmentModel.sectionAssignmentModel;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        if (studentName != null) {
            booleanBuilder.and(studentModel.studentName.likeIgnoreCase("%"+studentName+"%"));
        }

        if (classId != null || sectionId != null) {
            if (classId != null) {
                booleanBuilder.and(sectionAssignmentModel.section.schoolClass.schoolClassId.eq(classId));
            }
            if (sectionId != null) {
                booleanBuilder.and(sectionAssignmentModel.section.sectionId.eq(sectionId));
            }
        }

        if (hasSectionAssignment != null) {
            if (hasSectionAssignment) {
                booleanBuilder.and(sectionAssignmentModel.sectionAssignmentId.isNotNull());
            } else {
                booleanBuilder.and(sectionAssignmentModel.sectionAssignmentId.isNull());
            }
        }

        JPAQuery<StudentModel> baseQuery = queryFactory
                .selectFrom(studentModel)
                .leftJoin(studentModel.sectionAssignments, sectionAssignmentModel)
                .on(sectionAssignmentModel.academicYear.isActive.isTrue())
                .where(booleanBuilder);

        Long count = baseQuery.clone()
                .select(studentModel.countDistinct())
                .fetchOne();

        long totalCount = count != null ? count : 0L;

        List<StudentModel> fetch = baseQuery
                .distinct()
                .offset(pageRequest.getOffset())
                .limit(pageRequest.getPageSize())
                .orderBy(getOrderDetails(pageRequest.getSort(), studentModel))
                .fetch();

        return new PageImpl<>(fetch, pageRequest, totalCount);
    }

    private OrderSpecifier<?> getOrderDetails(Sort sort, QStudentModel studentModel) {
        if (sort.isUnsorted()) {
            return studentModel.studentId.asc();
        }

        Sort.Order order = sort.iterator().next();
        Order direction = order.isAscending() ? Order.ASC : Order.DESC;

        if (order.getProperty().equals("studentName")) {
            return new OrderSpecifier<>(direction, studentModel.studentName);
        } else {
            return new OrderSpecifier<>(direction, studentModel.studentId);
        }
    }
}
