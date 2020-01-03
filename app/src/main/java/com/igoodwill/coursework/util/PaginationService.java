package com.igoodwill.coursework.util;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

@Service
public class PaginationService {

    public Pageable getPageable(int page, int size, String sortDirection, String sortField) {
        Sort sort;
        if (sortDirection != null && sortField != null) {
            sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        } else {
            sort = Sort.unsorted();
        }

        return PageRequest.of(page, size, sort);
    }
}
