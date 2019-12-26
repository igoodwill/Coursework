package com.igoodwill.coursework.elastic.repository;

import com.igoodwill.coursework.elastic.model.Coursework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomCourseworkRepository {

    Page<Coursework> findByFileContains(String searchQuery, Pageable pageable);
}
