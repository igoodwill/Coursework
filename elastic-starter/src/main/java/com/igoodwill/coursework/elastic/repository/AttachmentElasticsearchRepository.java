package com.igoodwill.coursework.elastic.repository;

import com.igoodwill.coursework.elastic.config.ElasticAttachmentPipelineProperties;
import com.igoodwill.coursework.elastic.model.HasFile;
import lombok.NonNull;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.Requests;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.elasticsearch.repository.support.MappingElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;

import java.io.IOException;

public abstract class AttachmentElasticsearchRepository<T extends HasFile>
        extends SimpleElasticsearchRepository<T, String>
        implements CustomRepository<T> {

    private final ElasticAttachmentPipelineProperties properties;
    private final RestHighLevelClient client;
    private final EntityMapper entityMapper;

    @SuppressWarnings("unchecked")
    public AttachmentElasticsearchRepository(
            ElasticAttachmentPipelineProperties properties,
            ElasticsearchOperations elasticsearchOperations,
            RestHighLevelClient client,
            Class<T> clazz
    ) {
        super(
                new MappingElasticsearchEntityInformation<>(
                        (ElasticsearchPersistentEntity<T>) elasticsearchOperations
                                .getElasticsearchConverter()
                                .getMappingContext()
                                .getRequiredPersistentEntity(clazz)
                ),
                elasticsearchOperations
        );
        this.properties = properties;
        this.client = client;
        this.entityMapper = new ElasticsearchEntityMapper(
                elasticsearchOperations
                        .getElasticsearchConverter()
                        .getMappingContext(),
                null
        );
    }

    @Override
    public <S extends T> S save(@NonNull S entity) {
        String indexName = entityInformation.getIndexName();
        String documentId;
        try {
            IndexRequest indexRequest = new IndexRequest()
                    .index(indexName)
                    .type(entityInformation.getType())
                    .id(entityInformation.getId(entity))
                    .source(entityMapper.mapToString(entity), Requests.INDEX_CONTENT_TYPE);

            if (entity.getFile() != null) {
                indexRequest.setPipeline(properties.getId());
            }

            documentId = client.index(indexRequest, RequestOptions.DEFAULT).getId();
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

    public T getById(String id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
