package com.igoodwill.coursework.security.service;

import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpSession;

import static com.igoodwill.coursework.security.util.SecurityConstants.CURRENT_USER_PRINCIPAL_GRAPHAPI_TOKEN;

@Service
public class TokenService {

    public String getGraphApiToken() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        HttpSession session = attributes.getRequest().getSession(true);
        String token = (String) session.getAttribute(CURRENT_USER_PRINCIPAL_GRAPHAPI_TOKEN);
        return token;
    }
}
