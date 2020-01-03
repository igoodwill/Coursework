package com.igoodwill.coursework.elastic.repository.request;

import com.igoodwill.coursework.elastic.model.CourseworkRequest;
import com.igoodwill.coursework.elastic.repository.CustomRepository;

public interface CustomCourseworkRequestRepository extends CustomRepository<CourseworkRequest> {

    void approve(String requestId);

    void reject(String requestId, String comment);

    void close(String requestId, String comment);
}
