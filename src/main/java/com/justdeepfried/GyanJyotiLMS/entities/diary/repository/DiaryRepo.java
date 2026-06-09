package com.justdeepfried.GyanJyotiLMS.entities.diary.repository;

import com.justdeepfried.GyanJyotiLMS.entities.diary.model.DiaryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface DiaryRepo extends JpaRepository<DiaryModel, Long> {
    List<DiaryModel> findAllBySection_SectionIdAndDiaryDate(Long sectionId, LocalDate diaryDate);

    @Modifying
    @Query("""
    UPDATE DiaryModel dm
    SET dm.createdBy = null
    WHERE dm.createdBy.teacherId = :teacherId
    """)
    void clearTeacherReference(@Param("teacherId") Long teacherId);
}
