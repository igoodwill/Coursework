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
import org.springframework.data.elasticsearch.core.ElasticsearchEntityMapper;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.EntityMapper;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentEntity;
import org.springframework.data.elasticsearch.core.mapping.ElasticsearchPersistentProperty;
import org.springframework.data.elasticsearch.repository.support.MappingElasticsearchEntityInformation;
import org.springframework.data.elasticsearch.repository.support.SimpleElasticsearchRepository;

import java.io.IOException;
import java.util.Optional;
import java.util.UUID;

import static com.igoodwill.coursework.elastic.util.Constants.ATTACHMENT_CONTENT;
import static com.igoodwill.coursework.elastic.util.Constants.TITLE;

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
        this.entityMapper = new ElasticsearchEntityMapper(
                elasticsearchOperations
                        .getElasticsearchConverter()
                        .getMappingContext(),
                null
        );
    }

    @Override
    public <S extends Coursework> S save(@NonNull S entity) {
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

    @Override
    public Page<Coursework> searchByTitleAndAttachmentContent(String searchQuery, Pageable pageable) {
        return search(
                QueryBuilders
                        .queryStringQuery("*" + QueryParser.escape(searchQuery) + "*")
                        .field(TITLE)
                        .field(ATTACHMENT_CONTENT),
                pageable
        );
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

    @Override
    public Coursework getById(String id) {
        return findById(id).orElseThrow(IllegalArgumentException::new);
    }
}
