package com.justdeepfried.GyanJyotiLMS.entities.diary.service;

import com.justdeepfried.GyanJyotiLMS.entities.diary.dtos.DiaryCreate;
import com.justdeepfried.GyanJyotiLMS.entities.diary.dtos.DiaryUpdate;
import com.justdeepfried.GyanJyotiLMS.entities.diary.model.DiaryModel;
import com.justdeepfried.GyanJyotiLMS.entities.diary.repository.DiaryRepo;
import com.justdeepfried.GyanJyotiLMS.entities.school.context.SchoolContext;
import com.justdeepfried.GyanJyotiLMS.entities.school.service.SchoolService;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.service.SectionService;
import com.justdeepfried.GyanJyotiLMS.entities.subject.model.SubjectModel;
import com.justdeepfried.GyanJyotiLMS.entities.subject.service.SubjectService;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.model.TeacherModel;
import com.justdeepfried.GyanJyotiLMS.entities.teacher.service.TeacherService;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class DiaryService {

    private final DiaryRepo diaryRepo;
    private final TeacherService teacherService;
    private final SubjectService subjectService;
    private final SectionService sectionService;
    private final SchoolService schoolService;

    @Transactional(readOnly = true)
    public List<DiaryModel> findAll() {
        return diaryRepo.findAll();
    }

    public List<DiaryModel> findAllBySectionAndDiaryDate(Long sectionId, LocalDate diaryDate) {
        return diaryRepo.findAllBySection_SectionIdAndDiaryDate(sectionId, diaryDate);
    }

    public void createDiary(DiaryCreate diaryCreate) {
        DiaryModel diaryModel = new DiaryModel();

        diaryModel.setDiaryDate(diaryCreate.diaryDate());

        TeacherModel teacherModel = teacherService.findById(diaryCreate.createdById());
        SectionModel sectionModel = sectionService.findModelById(diaryCreate.sectionId());
        SubjectModel subjectModel = subjectService.findById(diaryCreate.subjectId());

        diaryModel.setCreatedBy(teacherModel);
        diaryModel.setSection(sectionModel);
        diaryModel.setSubject(subjectModel);

        diaryModel.setSchool(schoolService.findById(SchoolContext.get()));

        diaryModel.setTitle(diaryCreate.title());
        diaryModel.setContent(diaryCreate.content());

        diaryRepo.save(diaryModel);
    }

    public void updateDiary(DiaryUpdate diaryUpdate, Long diaryId) {
        DiaryModel existing = diaryRepo.findById(diaryId).orElseThrow(() -> new ResourceNotFoundException("Diary not found with id: " + diaryId));

        existing.setTitle(diaryUpdate.title());
        existing.setContent(diaryUpdate.content());

        diaryRepo.save(existing);
    }

    public void deleteDiary(Long diaryId) {
        DiaryModel existing = diaryRepo.findById(diaryId).orElseThrow(() -> new ResourceNotFoundException("Diary not found with id: " + diaryId));
        diaryRepo.delete(existing);
    }

}
