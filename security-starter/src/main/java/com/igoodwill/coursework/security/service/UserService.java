package com.igoodwill.coursework.security.service;

import com.igoodwill.coursework.security.config.SecurityProperties;
import com.microsoft.azure.spring.autoconfigure.aad.UserPrincipal;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

import static com.igoodwill.coursework.security.util.SecurityConstants.*;

@Service
@RequiredArgsConstructor
public class UserService {

    @Qualifier("graphApiRestTemplate")
    private final RestTemplate graphApiRestTemplate;

    private final SecurityProperties properties;

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

    public boolean isCurrentUserAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        Set<String> authoritiesSet = AuthorityUtils.authorityListToSet(authorities);
        return authoritiesSet.contains(ROLE_PREFIX + properties.getAdminRoleName());
    }
}
