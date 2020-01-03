package com.igoodwill.coursework.elastic.repository.coursework;

import com.igoodwill.coursework.elastic.model.Coursework;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(exported = false)
public interface CourseworkRepository extends ElasticsearchRepository<Coursework, String>, CustomCourseworkRepository {
}
