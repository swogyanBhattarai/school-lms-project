package com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.specification;

import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.model.SectionAssignmentModel;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SectionAssignmentSpecification {
    public static Specification<SectionAssignmentModel> getSpecification(String studentName, Long classId, Long sectionId) {
        return new Specification<SectionAssignmentModel>() {
            @Override
            public @Nullable Predicate toPredicate(Root<SectionAssignmentModel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> lists = new ArrayList<>();

                lists.add(criteriaBuilder.conjunction());

                if (studentName != null && !studentName.isBlank()) {
                    lists.add(criteriaBuilder.like(criteriaBuilder.lower(root.get("student").get("studentName")), "%" + studentName.toLowerCase() + "%"));
                }

                if (classId != null) {
                    lists.add(criteriaBuilder.equal(root.get("section").get("schoolClass").get("schoolClassId"), classId));
                }

                if (sectionId != null) {
                    lists.add(criteriaBuilder.equal(root.get("section").get("sectionId"), sectionId));
                }

                return criteriaBuilder.and(lists.toArray(new Predicate[0]));
            }
        };
    }
}
