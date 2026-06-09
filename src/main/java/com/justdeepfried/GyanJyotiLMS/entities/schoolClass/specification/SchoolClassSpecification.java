package com.justdeepfried.GyanJyotiLMS.entities.schoolClass.specification;

import com.justdeepfried.GyanJyotiLMS.entities.academicYear.model.AcademicYearModel;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.model.SchoolClassModel;
import jakarta.persistence.criteria.*;
import org.jspecify.annotations.Nullable;
import org.springframework.data.jpa.domain.Specification;

import java.util.ArrayList;
import java.util.List;

public class SchoolClassSpecification {
    public static Specification<SchoolClassModel> getSpecification() {
        return new Specification<SchoolClassModel>() {
            @Override
            public @Nullable Predicate toPredicate(Root<SchoolClassModel> root, CriteriaQuery<?> query, CriteriaBuilder criteriaBuilder) {
                List<Predicate> lists = new ArrayList<>();

                Join<SchoolClassModel, AcademicYearModel> academicYearModelJoin = root.join("academicYear", JoinType.LEFT);

                lists.add(criteriaBuilder.isTrue(academicYearModelJoin.get("isActive")));

                return criteriaBuilder.and(lists.toArray(new Predicate[0]));
            }
        };
    }
}
