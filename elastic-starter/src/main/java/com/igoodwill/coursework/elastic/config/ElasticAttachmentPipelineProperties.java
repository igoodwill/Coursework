package com.igoodwill.coursework.elastic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.igoodwill.coursework.elastic.pipeline.attachment")
@Data
public class ElasticAttachmentPipelineProperties {

    private String id = "attachment";

    private String description = "Extract attachment information";

    private int indexedChars = -1;
}
