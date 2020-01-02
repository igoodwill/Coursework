package com.igoodwill.coursework.elastic.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @Field(type = FieldType.Text, fielddata = true)
    private String title;

    @Field(type = FieldType.Text, store = true)
    private String file;

    @Field(type = FieldType.Text, fielddata = true)
    private String filename;

    private Attachment attachment;

    @JsonIgnore
    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    @JsonGetter
    public Attachment getAttachment() {
        return attachment;
    }
}
