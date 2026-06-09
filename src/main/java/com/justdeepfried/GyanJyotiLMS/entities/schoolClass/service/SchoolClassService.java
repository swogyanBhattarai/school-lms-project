package com.justdeepfried.GyanJyotiLMS.entities.schoolClass.service;

import com.justdeepfried.GyanJyotiLMS.entities.academicYear.service.AcademicYearService;
import com.justdeepfried.GyanJyotiLMS.entities.school.context.SchoolContext;
import com.justdeepfried.GyanJyotiLMS.entities.school.service.SchoolService;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.dtos.SchoolClassResponse;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.model.SchoolClassModel;
import com.justdeepfried.GyanJyotiLMS.entities.schoolClass.repository.SchoolClassRepo;
import com.justdeepfried.GyanJyotiLMS.entities.section.repository.SectionRepo;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceNotFoundException;
import com.justdeepfried.GyanJyotiLMS.exception.UnableToDeleteException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class SchoolClassService {

    private final SchoolClassRepo schoolClassRepo;
    private final AcademicYearService academicYearService;
    private final SchoolService schoolService;
    private final SectionRepo sectionRepo;

    @Transactional(readOnly = true)
    public List<SchoolClassResponse> findAll() {
        return schoolClassRepo.findAll().stream().map(SchoolClassResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public List<SchoolClassResponse> findAllActive() {
        return schoolClassRepo.findByAcademicYear_IsActiveTrue().stream().map(SchoolClassResponse::from).toList();
    }

    @Transactional(readOnly = true)
    public SchoolClassResponse findById(Long id) {
        return SchoolClassResponse.from(schoolClassRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("School Class not found with id: " + id)));
    }

    public SchoolClassModel findModelById(Long id) {
        return schoolClassRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("School Class not found with id: " + id));
    }

    public void save(String grade, Long academicYearId) {
        SchoolClassModel schoolClass = new SchoolClassModel();
        schoolClass.setGrade(grade);
        schoolClass.setSchool(schoolService.findById(SchoolContext.get()));
        schoolClass.setAcademicYear(academicYearService.findById(academicYearId));
        schoolClassRepo.save(schoolClass);
    }

    public void editSchoolClass(String grade, Long schoolClassId) {
        SchoolClassModel classModel = schoolClassRepo.findById(schoolClassId).orElseThrow(() -> new ResourceNotFoundException("School Class not found with id: " + schoolClassId));
        classModel.setGrade(grade);
        schoolClassRepo.save(classModel);
    }

    public void deleteById(Long id) {
        SchoolClassModel existing = schoolClassRepo.findById(id).orElseThrow(() -> new ResourceNotFoundException("School Class not found with id: " + id));

        if (sectionRepo.existsBySchoolClass_SchoolClassId(id)) {
            throw new UnableToDeleteException("Cannot delete Class with active sections!");
        }

        schoolClassRepo.delete(existing);
    }
}
