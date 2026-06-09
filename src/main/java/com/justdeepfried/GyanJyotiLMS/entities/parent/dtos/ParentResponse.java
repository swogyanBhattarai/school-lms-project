package com.justdeepfried.GyanJyotiLMS.entities.parent.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.parent.model.ParentModel;

public record ParentResponse (
        Long parentId,
        String parentName,
        String parentPhoneNumber
){
    public static ParentResponse from(ParentModel parentModel) {
        return new ParentResponse(
                parentModel.getParentId(),
                parentModel.getParentName(),
                parentModel.getParentNumber()
        );
    }
}
