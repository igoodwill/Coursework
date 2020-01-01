package com.igoodwill.coursework.elastic.config;

import lombok.RequiredArgsConstructor;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.apache.http.HttpHost;
import org.elasticsearch.action.ingest.PutPipelineRequest;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestClient;
import org.elasticsearch.client.RestClientBuilder;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.bytes.BytesArray;
import org.elasticsearch.common.xcontent.XContentType;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.ElasticsearchRestTemplate;
import org.springframework.data.elasticsearch.repository.config.EnableElasticsearchRepositories;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import static com.igoodwill.coursework.elastic.util.Constants.*;

@Configuration
@EnableElasticsearchRepositories(basePackages = "com.igoodwill.coursework.elastic.repository")
@EnableConfigurationProperties({ElasticProperties.class, ElasticAttachmentPipelineProperties.class})
@RequiredArgsConstructor
public class ElasticConfiguration {

    private final ElasticProperties properties;
    private final ElasticAttachmentPipelineProperties attachmentPipelineProperties;

    @Bean
    public RestHighLevelClient client() throws IOException {
        RestClientBuilder builder = RestClient.builder(
                new HttpHost(properties.getHost(), properties.getPort(), "http")
        );
        RestHighLevelClient client = new RestHighLevelClient(builder);
        setupAttachmentPipeline(client);
        return client;
    }

    @Bean("elasticsearchTemplate")
    public ElasticsearchOperations elasticsearchOperations(RestHighLevelClient client) {
        ElasticsearchRestTemplate result = new ElasticsearchRestTemplate(client);
        return result;
    }

    private void setupAttachmentPipeline(RestHighLevelClient client) throws IOException {
        JSONArray properties = new JSONArray();
        properties.add(CONTENT);
        properties.add(CONTENT_TYPE);

        JSONObject attachment = new JSONObject();
        attachment.put(FIELD, FILE);
        attachment.put(INDEXED_CHARS, attachmentPipelineProperties.getIndexedChars());
        attachment.put(PROPERTIES, properties);

        JSONObject processor = new JSONObject();
        processor.put(ATTACHMENT, attachment);

        JSONArray processors = new JSONArray();
        processors.add(processor);

        JSONObject source = new JSONObject();
        source.put(DESCRIPTION, attachmentPipelineProperties.getDescription());
        source.put(PROCESSORS, processors);

        client
                .ingest()
                .putPipeline(
                        new PutPipelineRequest(
                                attachmentPipelineProperties.getId(),
                                new BytesArray(source.toString().getBytes(StandardCharsets.UTF_8)),
                                XContentType.JSON
                        ),
                        RequestOptions.DEFAULT
                );
    }
}
