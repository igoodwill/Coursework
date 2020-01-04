package com.igoodwill.coursework.elastic.repository.coursework;

import com.igoodwill.coursework.elastic.config.ElasticAttachmentPipelineProperties;
import com.igoodwill.coursework.elastic.model.Coursework;
import com.igoodwill.coursework.elastic.repository.AttachmentElasticsearchRepository;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.BoolQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.Optional;
import java.util.UUID;

import static com.igoodwill.coursework.elastic.util.Constants.*;

public class CourseworkRepositoryImpl
        extends AttachmentElasticsearchRepository<Coursework>
        implements CustomCourseworkRepository {

    public CourseworkRepositoryImpl(
            ElasticAttachmentPipelineProperties properties,
            ElasticsearchOperations elasticsearchOperations,
            RestHighLevelClient client
    ) {
        super(properties, elasticsearchOperations, client, Coursework.class);
    }

    @Override
    public Page<Coursework> search(String searchQuery, Pageable pageable) {
        return search(searchQuery, pageable, null);
    }

    @Override
    public Page<Coursework> search(String searchQuery, Pageable pageable, UUID userId) {
        BoolQueryBuilder query = QueryBuilders
                .boolQuery()
                .must(
                        QueryBuilders
                                .queryStringQuery("*" + QueryParser.escape(searchQuery) + "*")
                                .field(TITLE)
                                .field(FILENAME)
                                .field(ATTACHMENT_CONTENT)
                );

        if (userId != null) {
            query.must(QueryBuilders.matchQuery(CREATOR_ID, userId.toString()));
        }

        return search(query, pageable);
    }

    @Override
    public Coursework create(Coursework coursework) {
        coursework.setId(UUID.randomUUID().toString());
        return save(coursework);
    }

    @Override
    public void update(Coursework coursework) {
        Coursework existingCoursework = getById(coursework.getId());
        Optional
                .of(coursework)
                .map(Coursework::getTitle)
                .ifPresent(existingCoursework::setTitle);

        Optional
                .of(coursework)
                .map(Coursework::getFile)
                .ifPresent(existingCoursework::setFile);

        Optional
                .of(coursework)
                .map(Coursework::getFilename)
                .ifPresent(existingCoursework::setFilename);

        save(existingCoursework);
    }
}
