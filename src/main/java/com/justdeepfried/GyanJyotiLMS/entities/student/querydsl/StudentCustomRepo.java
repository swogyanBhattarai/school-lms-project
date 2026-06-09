package com.justdeepfried.GyanJyotiLMS.entities.student.querydsl;

import com.justdeepfried.GyanJyotiLMS.entities.student.model.StudentModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

public interface StudentCustomRepo {
    Page<StudentModel> findAllFiltered(String studentName, Long sectionId, Long classId, Boolean hasSectionAssignment, PageRequest pageRequest);
}
