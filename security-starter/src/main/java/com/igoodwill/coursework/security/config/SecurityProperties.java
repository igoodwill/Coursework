package com.igoodwill.coursework.security.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties("com.igoodwill.coursework.security")
@Data
public class SecurityProperties {

    private String adminRoleName;
}
