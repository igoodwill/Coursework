package com.igoodwill.coursework.elastic.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "com.igoodwill.coursework.elastic")
@Data
public class ElasticProperties {

    private String host;

    private int port;
}
