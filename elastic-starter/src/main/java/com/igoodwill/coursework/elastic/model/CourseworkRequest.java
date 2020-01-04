package com.igoodwill.coursework.elastic.model;

import com.fasterxml.jackson.annotation.JsonGetter;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.UUID;

@Data
@Document(indexName = "request")
public class CourseworkRequest implements HasFile {

    @Id
    private String id;

    @Field(type = FieldType.Text, fielddata = true)
    private String title;

    @Field(type = FieldType.Text, store = true)
    private String file;

    @Field(type = FieldType.Text, fielddata = true)
    private String filename;

    private Attachment attachment;

    private UUID creatorId;

    @Field(type = FieldType.Text, fielddata = true)
    private CourseworkRequestStatus status = CourseworkRequestStatus.OPENED;

    @Field(type = FieldType.Text, fielddata = true)
    private String comment;

    private String courseworkId;

    private UUID approverId;

    @JsonIgnore
    public void setAttachment(Attachment attachment) {
        this.attachment = attachment;
    }

    @JsonGetter
    public Attachment getAttachment() {
        return attachment;
    }

    public void approve(String courseworkId, UUID approverId) {
        assertCanBeChanged();

        setStatus(CourseworkRequestStatus.APPROVED);
        setCourseworkId(courseworkId);
        setApproverId(approverId);
    }

    public void reject(String comment) {
        assertCanBeChanged();

        setStatus(CourseworkRequestStatus.REJECTED);
        setComment(comment);
    }

    public void close(String comment) {
        assertCanBeChanged();

        setStatus(CourseworkRequestStatus.CLOSED);
        setComment(comment);
    }

    public void assertCanBeChanged() {
        if (status != CourseworkRequestStatus.OPENED) {
            throw new IllegalStateException(
                    String.format("Request is %s and cannot be changed", status.toString().toLowerCase())
            );
        }
    }
}
