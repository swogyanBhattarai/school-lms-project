package com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.model.StudentBulkUploadRowModel;
import com.justdeepfried.GyanJyotiLMS.enums.ROW_STATUS;
import java.time.LocalDate;

public record StudentBulkUploadRowResponse (
        int rowNumber,
        String studentName,
        LocalDate dateOfBirth,
        String parentName1,
        String parentPhoneNumber1,
        String parentName2,
        String parentPhoneNumber2 ,
        ROW_STATUS rowStatus,
        String errorMessage
) {
    public static StudentBulkUploadRowResponse from(StudentBulkUploadRowModel rowModel) {
        return new StudentBulkUploadRowResponse(
                rowModel.getRowNumber(),
                rowModel.getStudentName(),
                rowModel.getDateOfBirth(),
                rowModel.getParentName1(),
                rowModel.getParentPhoneNumber1(),
                rowModel.getParentName2(),
                rowModel.getParentPhoneNumber2(),
                rowModel.getRowStatus(),
                rowModel.getErrorMessage()
        );
    }
}
