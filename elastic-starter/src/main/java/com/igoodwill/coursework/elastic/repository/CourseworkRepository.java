package com.igoodwill.coursework.elastic.repository;

import com.igoodwill.coursework.elastic.model.Coursework;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

public interface CourseworkRepository extends ElasticsearchRepository<Coursework, String>, CustomCourseworkRepository {
}
