package com.igoodwill.coursework.elastic.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CustomRepository<T> {

    Page<T> search(String searchQuery, Pageable pageable);

    T create(T coursework);

    void update(T coursework);

    T getById(String id);
}
