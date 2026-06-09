package com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.dtos;

import com.justdeepfried.GyanJyotiLMS.entities.StudentBulkUpload.model.StudentBulkUploadModel;

import java.util.List;

public record StudentBulkUploadResponse (
        int successCount,
        int failureCount,
        List<StudentBulkUploadRowResponse> rowResults
) {
    public static StudentBulkUploadResponse from(StudentBulkUploadModel uploadModel) {
        return new StudentBulkUploadResponse(
                uploadModel.getSuccessCount(),
                uploadModel.getFailureCount(),
                uploadModel.getRowResults()
                        .stream().map(StudentBulkUploadRowResponse::from)
                        .toList()
        );
    }

    public static StudentBulkUploadResponse empty() {
        return new StudentBulkUploadResponse(
                0,
                0,
                List.of()
        );
    }
}
