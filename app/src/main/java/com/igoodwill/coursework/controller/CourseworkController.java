package com.igoodwill.coursework.controller;

import com.igoodwill.coursework.elastic.model.Coursework;
import com.igoodwill.coursework.elastic.repository.CourseworkRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("coursework")
@RequiredArgsConstructor
public class CourseworkController {

    private final CourseworkRepository repository;

    @PostMapping
    public void createCoursework(@RequestBody Coursework coursework) {
        repository.save(coursework);
    }

    @GetMapping
    public Page<Coursework> findCourseworkByFileContent(@RequestParam String searchQuery) {
        return repository.findByFileContains(searchQuery, Pageable.unpaged());
    }
}
