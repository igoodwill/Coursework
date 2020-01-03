package com.igoodwill.coursework.elastic.repository.request;

import com.igoodwill.coursework.elastic.config.ElasticAttachmentPipelineProperties;
import com.igoodwill.coursework.elastic.model.Coursework;
import com.igoodwill.coursework.elastic.model.CourseworkRequest;
import com.igoodwill.coursework.elastic.repository.AttachmentElasticsearchRepository;
import com.igoodwill.coursework.elastic.repository.coursework.CourseworkRepository;
import org.apache.lucene.queryparser.classic.QueryParser;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;

import java.util.Optional;
import java.util.UUID;

import static com.igoodwill.coursework.elastic.util.Constants.*;

public class CourseworkRequestRepositoryImpl
        extends AttachmentElasticsearchRepository<CourseworkRequest>
        implements CustomCourseworkRequestRepository {

    private final CourseworkRepository courseworkRepository;

    public CourseworkRequestRepositoryImpl(
            ElasticAttachmentPipelineProperties properties,
            ElasticsearchOperations elasticsearchOperations,
            RestHighLevelClient client,
            CourseworkRepository courseworkRepository
    ) {
        super(properties, elasticsearchOperations, client, CourseworkRequest.class);
        this.courseworkRepository = courseworkRepository;
    }

    @Override
    public Page<CourseworkRequest> search(String searchQuery, Pageable pageable) {
        return search(
                QueryBuilders
                        .queryStringQuery("*" + QueryParser.escape(searchQuery) + "*")
                        .field(TITLE)
                        .field(FILENAME)
                        .field(ATTACHMENT_CONTENT)
                        .field(STATUS)
                        .field(COMMENT),
                pageable
        );
    }

    @Override
    public CourseworkRequest create(CourseworkRequest request) {
        request.setId(UUID.randomUUID().toString());
        return save(request);
    }

    @Override
    public void update(CourseworkRequest request) {
        CourseworkRequest existingRequest = getById(request.getId());
        existingRequest.assertCanBeChanged();

        Optional
                .of(request)
                .map(CourseworkRequest::getTitle)
                .ifPresent(existingRequest::setTitle);

        Optional
                .of(request)
                .map(CourseworkRequest::getFile)
                .ifPresent(existingRequest::setFile);

        Optional
                .of(request)
                .map(CourseworkRequest::getFilename)
                .ifPresent(existingRequest::setFilename);

        Optional
                .of(request)
                .map(CourseworkRequest::getStatus)
                .ifPresent(existingRequest::setStatus);

        Optional
                .of(request)
                .map(CourseworkRequest::getComment)
                .ifPresent(existingRequest::setComment);

        Optional
                .of(request)
                .map(CourseworkRequest::getCourseworkId)
                .ifPresent(existingRequest::setCourseworkId);

        save(existingRequest);
    }

    @Override
    public void approve(String requestId) {
        CourseworkRequest request = getById(requestId);

        Coursework coursework = new Coursework();
        coursework.setTitle(request.getTitle());
        coursework.setFile(request.getFile());
        coursework.setFilename(request.getFilename());

        coursework = courseworkRepository.create(coursework);

        request.approve(coursework.getId());
        save(request);
    }

    @Override
    public void reject(String requestId, String comment) {
        CourseworkRequest request = getById(requestId);
        request.reject(comment);
        save(request);
    }

    @Override
    public void close(String requestId, String comment) {
        CourseworkRequest request = getById(requestId);
        request.close(comment);
        save(request);
    }
}
