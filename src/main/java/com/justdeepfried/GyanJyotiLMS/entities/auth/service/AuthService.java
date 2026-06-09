package com.justdeepfried.GyanJyotiLMS.entities.auth.service;

import com.justdeepfried.GyanJyotiLMS.entities.auth.dtos.CurrentUserInfoResponse;
import com.justdeepfried.GyanJyotiLMS.entities.user.dto.UserLoginDTO;
import com.justdeepfried.GyanJyotiLMS.security.jwt.JwtService;
import com.justdeepfried.GyanJyotiLMS.security.user.CustomUserDetailsService;
import com.justdeepfried.GyanJyotiLMS.security.user.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class AuthService {

    @Value("${app.accessKeyExpiry}")
    private Long accessKeyExpiry;

    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public ResponseEntity<String> login(UserLoginDTO userLogin) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(userLogin.username(), userLogin.password()));

        UserDetails userDetails = (UserDetails) authenticate.getPrincipal();

        String token = jwtService.createToken(userDetails, accessKeyExpiry);

        ResponseCookie cookie = ResponseCookie.from("accessKey", token)
                .httpOnly(true)
                .secure(false)
                .sameSite("Lax")
                .path("/")
                .maxAge(Duration.ofMillis(accessKeyExpiry))
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Logged in successfully!");
    }

    public CurrentUserInfoResponse me() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof UserPrincipal userPrincipal) {
                String displayName = getString(userPrincipal);

                return new CurrentUserInfoResponse(
                        displayName,
                        userPrincipal.getRole(),
                        userPrincipal.getSchoolId()
                );
            }
        }

        return CurrentUserInfoResponse.empty();
    }

    private static String getString(UserPrincipal userPrincipal) {
        String displayName = userPrincipal.getUsername();

        try {
            if ("TEACHER".equals(userPrincipal.getRole())) {
                displayName = userPrincipal.getTeacherName();
            } else if ("PARENT".equals(userPrincipal.getRole())) {
                displayName = userPrincipal.getParentName();
            }
        } catch (Exception e) {
            // Fallback to username if name retrieval fails
        }
        return displayName;
    }
}
