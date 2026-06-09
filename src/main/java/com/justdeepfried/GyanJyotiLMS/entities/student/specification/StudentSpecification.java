package com.justdeepfried.GyanJyotiLMS.entities.student.specification;

import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;
import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import jakarta.persistence.criteria.*;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class StudentSpecification {
    public static Specification<StudentModel> getSpecification(String studentName, Long sectionId, Long classId, Boolean hasSectionAssignment) {
        return new Specification<StudentModel>() {
            @Override
            public @Nullable Predicate toPredicate(Root<StudentModel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> lists = new ArrayList<>();
                query.distinct(true);

                if (studentName != null && !studentName.isBlank()) {
                    lists.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("studentName")), "%" + studentName.toLowerCase() + "%"));
                }

                if (classId != null || sectionId != null) {
                    Join<StudentModel, SectionAssignmentModel> assignmentJoin = root.join("sectionAssignments", JoinType.LEFT);

                    Join<SectionAssignmentModel, SectionModel> sectionJoin = assignmentJoin.join("section", JoinType.LEFT);

                    if (sectionId != null) lists.add(criteriaBuilder.equal(sectionJoin.get("sectionId"), sectionId));

                    if (classId != null) lists.add(criteriaBuilder.equal(sectionJoin.get("schoolClass").get("schoolClassId"), classId));
                }

                if (hasSectionAssignment != null) {
                    if (hasSectionAssignment) lists.add(criteriaBuilder.isNotEmpty(root.get("sectionAssignments")));
                    else lists.add(criteriaBuilder.isEmpty(root.get("sectionAssignments")));
                }

                return criteriaBuilder.and(lists.toArray(new Predicate[0]));
            }
        };
    }
}
