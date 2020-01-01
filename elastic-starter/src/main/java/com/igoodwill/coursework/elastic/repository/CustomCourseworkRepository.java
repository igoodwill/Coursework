package com.igoodwill.coursework.elastic.repository;

import com.igoodwill.coursework.elastic.model.Coursework;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomCourseworkRepository {

    Page<Coursework> searchByTitleAndAttachmentContent(String searchQuery, Pageable pageable);

    Coursework create(Coursework coursework);

    void update(Coursework coursework);

    Coursework getById(String id);
}
