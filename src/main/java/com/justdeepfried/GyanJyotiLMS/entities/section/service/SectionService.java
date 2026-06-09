package com.justdeepfried.GyanJyotiLMS.entities.section.service;

import com.justdeepfried.GyanJyotiLMS.entities.classAssignment.repository.ClassAssignmentRepo;
import com.justdeepfried.GyanJyotiLMS.entities.school.context.SchoolContext;
import com.justdeepfried.GyanJyotiLMS.entities.school.service.SchoolService;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.service.SchoolClassService;
import com.justdeepfried.GyanJyotiLMS.entities.section.dto.SectionResponse;
import com.justdeepfried.GyanJyotiLMS.entities.section.dto.SectionUpdate;
import com.justdeepfried.GyanJyotiLMS.entities.section.model.SectionModel;
import com.justdeepfried.GyanJyotiLMS.entities.section.repository.SectionRepo;
import com.justdeepfried.GyanJyotiLMS.entities.sectionAssignment.repository.SectionAssignmentRepo;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceNotFoundException;
import com.justdeepfried.GyanJyotiLMS.exception.UnableToDeleteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SectionService {

    private final SectionRepo sectionRepo;
    private final SchoolClassService classService;
    private final SchoolService schoolService;

    private final SectionAssignmentRepo sectionAssignmentRepo;
    private final ClassAssignmentRepo classAssignmentRepo;

    @Transactional(readOnly = true)
    public List<SectionResponse> findAll() {
        return sectionRepo.findAll().stream().map(SectionResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<SectionResponse> findAllBySchoolClassId(Long schoolClassId) {
        return sectionRepo.findAllBySchoolClass_SchoolClassId(schoolClassId)
                .stream()
                .map(SectionResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public SectionResponse findById(Long id) {
        SectionModel sectionModel = sectionRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + id));
        return SectionResponse.from(sectionModel);
    }

    @Transactional(readOnly = true)
    public SectionModel findModelById(Long id) {
        return sectionRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + id));
    }

    public void createSection(String sectionName, Long classId) {
        SectionModel sectionModel = new SectionModel();
        sectionModel.setSchoolClass(classService.findModelById(classId));
        sectionModel.setSectionName(sectionName);
        sectionModel.setSchool(schoolService.findById(SchoolContext.get()));
        sectionRepo.save(sectionModel);
    }

    public void updateSection(Long id, SectionUpdate sectionUpdate) {
        SectionModel sectionModel = findModelById(id);
        sectionModel.setSectionName(sectionUpdate.sectionName());
        sectionRepo.save(sectionModel);
    }

    public void deleteById(Long id) {
        SectionModel existing = sectionRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("Section not found with id: " + id));

        if (sectionAssignmentRepo.existsBySection_SectionId(id)) {
            throw new UnableToDeleteException("Cannot delete Section with active students!");
        }

        if (classAssignmentRepo.existsBySection_SectionId(id)) {
            throw new UnableToDeleteException("Cannot delete Section with active class assignments!");
        }

        sectionRepo.delete(existing);
    }
}
