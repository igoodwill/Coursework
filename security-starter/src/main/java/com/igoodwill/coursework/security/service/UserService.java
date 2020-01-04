package com.igoodwill.coursework.security.service;

import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService {

    private static final String USER_ID_CLAIM = "oid";

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String oid = (String) principal.getClaim(USER_ID_CLAIM);
        UUID result = UUID.fromString(oid);
        return result;
    }

    public String getFullUserName(UUID id) {
        return "Full name";
    }
}
