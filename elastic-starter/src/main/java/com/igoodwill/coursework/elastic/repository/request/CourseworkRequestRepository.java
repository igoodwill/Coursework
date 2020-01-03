package com.igoodwill.coursework.elastic.repository.request;

import com.igoodwill.coursework.elastic.model.CourseworkRequest;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CourseworkRequestRepository
        extends ElasticsearchRepository<CourseworkRequest, String>, CustomCourseworkRequestRepository {
}
