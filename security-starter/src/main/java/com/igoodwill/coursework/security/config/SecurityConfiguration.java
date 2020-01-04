package com.igoodwill.coursework.security.config;

import com.igoodwill.coursework.security.service.TokenService;
import com.microsoft.azure.spring.autoconfigure.aad.AADAuthenticationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpRequest;
import org.springframework.http.client.support.HttpRequestWrapper;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

import static com.igoodwill.coursework.security.util.SecurityConstants.API_VERSION_PARAM;
import static com.igoodwill.coursework.security.util.SecurityConstants.DEFAULT_API_VERSION_VALUE;

@Configuration
@RequiredArgsConstructor
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

    private final OAuth2UserService<OidcUserRequest, OidcUser> oidcUserService;
    private final AADAuthenticationFilter aadAuthenticationFilter;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .csrf().disable()
                .addFilterBefore(aadAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .oauth2Login()
                .userInfoEndpoint()
                .oidcUserService(oidcUserService);
    }

    @Bean("graphApiRestTemplate")
    public RestTemplate graphApiRestTemplate(TokenService tokenService) {
        RestTemplate restTemplate = new RestTemplateBuilder()
                .rootUri("https://graph.windows.net/myorganization/")
                .interceptors(
                        (request, body, execution) -> {
                            request.getHeaders().set(HttpHeaders.AUTHORIZATION, tokenService.getGraphApiToken());
                            return execution.execute(request, body);
                        }, (request, body, execution) -> {
                            URI uri = UriComponentsBuilder.fromHttpRequest(request)
                                    .queryParam(API_VERSION_PARAM, DEFAULT_API_VERSION_VALUE)
                                    .build()
                                    .toUri();

                            HttpRequest modifiedRequest = new HttpRequestWrapper(request) {
                                @Override
                                public URI getURI() {
                                    return uri;
                                }
                            };

                            return execution.execute(modifiedRequest, body);
                        }
                )
                .build();

        return restTemplate;
    }
}
