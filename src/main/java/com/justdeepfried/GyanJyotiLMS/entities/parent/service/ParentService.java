package com.justdeepfried.GyanJyotiLMS.entities.parent.service;

import com.justdeepfried.GyanJyotiLMS.entities.parent.dtos.ParentCreate;
import com.justdeepfried.GyanJyotiLMS.entities.parent.model.ParentModel;
import com.justdeepfried.GyanJyotiLMS.entities.parent.repository.ParentRepo;
import com.justdeepfried.GyanJyotiLMS.entities.school.context.SchoolContext;
import com.justdeepfried.GyanJyotiLMS.entities.school.model.SchoolModel;
import com.justdeepfried.GyanJyotiLMS.entities.school.service.SchoolService;
import com.justdeepfried.GyanJyotiLMS.entities.user.dto.UserCreateDTO;
import com.justdeepfried.GyanJyotiLMS.entities.user.model.UserModel;
import com.justdeepfried.GyanJyotiLMS.entities.user.repository.UserRepository;
import com.justdeepfried.GyanJyotiLMS.entities.user.service.UserService;
import com.justdeepfried.GyanJyotiLMS.enums.USER_ROLES;
import com.justdeepfried.GyanJyotiLMS.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ParentService {
    private final ParentRepo parentRepo;
    private final SchoolService schoolService;
    private final UserService userService;

    private final BCryptPasswordEncoder encoder;
    private final UserRepository userRepo;

    @Transactional(readOnly = true)
    public ParentModel findById(Long parentId) {
        return parentRepo.findById(parentId).orElseThrow(() -> new ResourceNotFoundException("Parent not found with id: " + parentId));
    }

    @Transactional(readOnly = true)
    public ParentModel findByParentNumber(String phoneNumber) {
        return parentRepo.findByParentNumber(phoneNumber).orElseThrow(() -> new ResourceNotFoundException("Parent not found with parentNumber: " + phoneNumber));
    }

    public ParentModel findOrCreateParent(ParentCreate parentCreate) {
        return parentRepo.findByParentNumber(parentCreate.parentPhoneNumber())
                .orElseGet(() -> createNewParent(parentCreate));
    }

    private ParentModel createNewParent(ParentCreate parentCreate) {
        ParentModel p = new ParentModel();
        p.setParentName(parentCreate.parentName());
        p.setParentNumber(parentCreate.parentPhoneNumber());

        p.setSchool(schoolService.findById(SchoolContext.get()));

        ParentModel savedParent = parentRepo.save(p);

        createParentUserAccount(savedParent);

        return savedParent;
    }

    private void createParentUserAccount(ParentModel parentModel) {
        Optional<UserModel> existing = userRepo.findByUsername(parentModel.getParentNumber());

        if (existing.isPresent()) return;

        UserModel parentUser = new UserModel();

        parentUser.setParent(parentModel);
        parentUser.setRole(USER_ROLES.ADMIN);

        parentUser.setUsername(parentModel.getParentNumber());
        parentUser.setPassword(encoder.encode(parentModel.getParentNumber()));

        parentUser.setSchool(schoolService.findById(SchoolContext.get()));

        userRepo.save(parentUser);
    }

    public ParentModel updateParent(Long parentId, ParentCreate parentCreate) {
        ParentModel parent = findById(parentId);
        parent.setParentName(parentCreate.parentName());
        parent.setParentNumber(parentCreate.parentPhoneNumber());
        return parentRepo.save(parent);
    }


    // THIS IS FOR THE BULK UPLOAD OPTIMIZATION!!!

    public Map<String, ParentModel> loadParentMapBySchool(Long schoolId) {
        return parentRepo.findAllBySchool_SchoolId(schoolId)
                .stream()
                .collect(Collectors.toMap(
                        ParentModel::getParentNumber,
                        p -> p
                ));
    }

    public ParentModel findOrCreateParent(ParentCreate parentCreate, SchoolModel schoolModel) {
        return parentRepo.findByParentNumber(parentCreate.parentPhoneNumber())
                .orElseGet(() -> createNewParent(parentCreate, schoolModel));
    }

    public ParentModel createNewParent(ParentCreate parentCreate, SchoolModel schoolModel) {
        ParentModel p = new ParentModel();
        p.setParentName(parentCreate.parentName());
        p.setParentNumber(parentCreate.parentPhoneNumber());

        p.setSchool(schoolModel);

        ParentModel savedParent = parentRepo.save(p);

        createParentUserAccount(savedParent, schoolModel);

        return savedParent;
    }

    private void createParentUserAccount(ParentModel parentModel, SchoolModel schoolModel) {
        Optional<UserModel> existing = userRepo.findByUsername(parentModel.getParentNumber());

        if (existing.isPresent()) return;

        UserModel parentUser = new UserModel();

        parentUser.setParent(parentModel);
        parentUser.setRole(USER_ROLES.ADMIN);

        parentUser.setUsername(parentModel.getParentNumber());
        parentUser.setPassword(encoder.encode(parentModel.getParentNumber()));

        parentUser.setSchool(schoolModel);

        userRepo.save(parentUser);
    }
}
