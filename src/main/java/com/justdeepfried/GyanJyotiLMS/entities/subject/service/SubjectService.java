package com.justdeepfried.GyanJyotiLMS.entities.subject.service;

import com.justdeepfried.GyanJyotiLMS.entities.school.context.SchoolContext;
import com.justdeepfried.GyanJyotiLMS.entities.school.service.SchoolService;
import com.justdeepfried.GyanJyotiLMS.entities.subject.dtos.SubjectCreate;
import com.justdeepfried.GyanJyotiLMS.entities.subject.dtos.SubjectResponse;
import com.justdeepfried.GyanJyotiLMS.entities.subject.dtos.SubjectUpdate;
import com.justdeepfried.GyanJyotiLMS.entities.subject.model.SubjectModel;
import com.justdeepfried.GyanJyotiLMS.entities.subject.repository.SubjectRepo;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SubjectService {

    private final SubjectRepo subjectRepo;
    private final SchoolService schoolService;

    @Transactional(readOnly = true)
    public List<SubjectResponse> findAll() {
        return subjectRepo.findAll().stream()
                .map(SubjectResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public SubjectModel findById(Long id) {
        return subjectRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + id));
    }

    public void createSubject(SubjectCreate subjectCreate) {
        SubjectModel subjectModel = new SubjectModel();
        subjectModel.setSubjectName(subjectCreate.subjectName());
        subjectModel.setSchool(schoolService.findById(SchoolContext.get()));
        subjectRepo.save(subjectModel);
    }

    public void updateSubject(Long subjectId, SubjectUpdate subjectUpdate) {
        SubjectModel subjectModel = subjectRepo.findById(subjectId).orElseThrow(() -> new ResourceNotFoundException("Subject not found with id: " + subjectId));
        subjectModel.setSubjectName(subjectUpdate.subjectName());
        subjectRepo.save(subjectModel);
    }

    public void deleteById(Long id) {
        subjectRepo.deleteById(id);
    }
}
