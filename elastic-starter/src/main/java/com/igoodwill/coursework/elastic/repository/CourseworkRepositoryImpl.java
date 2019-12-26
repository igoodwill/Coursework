package com.igoodwill.coursework.elastic.repository;

import com.igoodwill.coursework.elastic.config.ElasticAttachmentPipelineProperties;
import com.igoodwill.coursework.elastic.model.Coursework;
import lombok.NonNull;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.DefaultEntityMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.elasticsearch.repository.support.MappingElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;

import java.io.IOException;

public class CourseworkRepositoryImpl
        extends SimpleElasticsearchRepository<Coursework, String>
        implements CustomCourseworkRepository {

    private final ElasticAttachmentPipelineProperties properties;
    private final RestHighLevelClient client;
    private final EntityMapper entityMapper;

    @SuppressWarnings("unchecked")
    public CourseworkRepositoryImpl(
            ElasticAttachmentPipelineProperties properties,
            ElasticsearchOperations elasticsearchOperations,
            RestHighLevelClient client
    ) {
        super(
                new MappingElasticsearchEntityInformation<>(
                        (ElasticsearchPersistentEntity<Coursework>) elasticsearchOperations
                                .getElasticsearchConverter()
                                .getMappingContext()
                                .getRequiredPersistentEntity(Coursework.class)
                ),
                elasticsearchOperations
        );
        this.properties = properties;
        this.client = client;
        this.entityMapper = new DefaultEntityMapper(
                elasticsearchOperations
                        .getElasticsearchConverter()
                        .getMappingContext()
        );
    }


    @Override
    public <S extends Coursework> S save(@NonNull S entity) {
        String indexName = entityInformation.getIndexName();
        String documentId;
        try {
            documentId = client.index(
                    new IndexRequest()
                            .index(indexName)
                            .type(entityInformation.getType())
                            .id(entityInformation.getId(entity))
                            .setPipeline(properties.getId())
                            .source(entityMapper.mapToString(entity), Requests.INDEX_CONTENT_TYPE),
                    RequestOptions.DEFAULT
            ).getId();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        ElasticsearchPersistentEntity<?> persistentEntity = elasticsearchOperations.getPersistentEntityFor(entity.getClass());
        ElasticsearchPersistentProperty idProperty = persistentEntity.getIdProperty();
        if (idProperty != null && idProperty.getType().isAssignableFrom(String.class)) {
            persistentEntity.getPropertyAccessor(entity).setProperty(idProperty, documentId);
        }

        elasticsearchOperations.refresh(indexName);
        return entity;
    }

    @Override
    public Page<Coursework> findByFileContains(String searchQuery, Pageable pageable) {
        return search(
                QueryBuilders.queryStringQuery("*" + QueryParser.escape(searchQuery) + "*"),
                pageable
        );
    }
}
