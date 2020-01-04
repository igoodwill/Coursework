package com.igoodwill.coursework.elastic.repository.request;

import com.igoodwill.coursework.elastic.model.CourseworkRequest;
import com.igoodwill.coursework.elastic.repository.EntityWithCreatorRepository;

import java.util.UUID;

public interface CustomCourseworkRequestRepository extends EntityWithCreatorRepository<CourseworkRequest> {

    CourseworkRequest approve(String requestId, UUID approverId);

    void reject(String requestId, String comment);

    void close(String requestId, String comment);
}
