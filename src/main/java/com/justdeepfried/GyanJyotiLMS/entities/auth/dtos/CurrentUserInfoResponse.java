package com.justdeepfried.GyanJyotiLMS.entities.auth.dtos;

import com.justdeepfried.GyanJyotiLMS.security.user.UserPrincipal;

public record CurrentUserInfoResponse (
        String username,
        String userRole,
        Long schoolId
) {
    public static CurrentUserInfoResponse from(UserPrincipal userPrincipal) {
        return new CurrentUserInfoResponse(
                userPrincipal.getUsername(),
                userPrincipal.getRole(),
                userPrincipal.getSchoolId()
        );
    }

    public static CurrentUserInfoResponse empty() {
        return new CurrentUserInfoResponse(
                "Empty",
                "Empty",
                0L
        );
    }
}
