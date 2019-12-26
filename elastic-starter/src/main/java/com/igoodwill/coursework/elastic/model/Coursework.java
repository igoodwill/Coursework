package com.igoodwill.coursework.elastic.model;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

@Data
@Document(indexName = "work")
public class Coursework {

    @Id
    private String id;

    private String title;

    @Field(type = FieldType.Text, store = true)
    private String file;
}
