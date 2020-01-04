package com.igoodwill.coursework.elastic.repository;

import com.igoodwill.coursework.elastic.model.HasCreator;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.Optional;
import java.util.UUID;

public interface EntityWithCreatorRepository<T extends HasCreator> extends CustomRepository<T> {

    default boolean isCreatedBy(String id, UUID userId) {
        if (userId == null) {
            return false;
        }

        return Optional
                .ofNullable(id)
                .map(this::getById)
                .map(HasCreator::getCreatorId)
                .map(userId::equals)
                .orElse(false);
    }

    Page<T> search(String searchQuery, Pageable pageable, UUID userId);
}
