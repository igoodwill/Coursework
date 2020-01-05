package com.igoodwill.coursework.controller;

import com.igoodwill.coursework.security.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("user")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("isAdmin")
    public boolean isCurrentUserAdmin() {
        return userService.isCurrentUserAdmin();
    }
}
