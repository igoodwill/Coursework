package com.igoodwill.coursework.security.service;

import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

import static com.igoodwill.coursework.security.util.SecurityConstants.DISPLAY_NAME;
import static com.igoodwill.coursework.security.util.SecurityConstants.USER_ID_CLAIM;

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("graphApiRestTemplate")
    private final RestTemplate graphApiRestTemplate;

    public UUID getCurrentUserId() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        UserPrincipal principal = (UserPrincipal) authentication.getPrincipal();

        String userId = (String) principal.getClaim(USER_ID_CLAIM);
        UUID result = UUID.fromString(userId);
        return result;
    }

    public String getDisplayUserName(UUID userId) {
        return Optional
                .ofNullable(userId)
                .map(id -> "/users/" + id)
                .map(url -> graphApiRestTemplate.getForEntity(url, Map.class))
                .map(HttpEntity::getBody)
                .map(body -> (String) body.get(DISPLAY_NAME))
                .orElse(null);
    }
}
