package com.igoodwill.coursework.elastic.model;

import com.fasterxml.jackson.annotation.JsonAlias;
import lombok.Data;

@Data
public class Attachment {

    @JsonAlias("content_type")
    private String contentType;
}
