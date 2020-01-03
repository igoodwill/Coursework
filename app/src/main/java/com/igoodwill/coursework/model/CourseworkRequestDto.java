package com.igoodwill.coursework.model;

import com.igoodwill.coursework.elastic.model.CourseworkRequest;
import com.igoodwill.coursework.elastic.model.CourseworkRequestStatus;
import lombok.Data;

@Data
public class CourseworkRequestDto {

    private String id;

    private String title;

    private String filename;

    private CourseworkRequestStatus status;

    private String comment;

    private String courseworkId;

    public static CourseworkRequestDto from(CourseworkRequest request) {
        CourseworkRequestDto dto = new CourseworkRequestDto();
        dto.setId(request.getId());
        dto.setTitle(request.getTitle());
        dto.setFilename(request.getFilename());
        dto.setStatus(request.getStatus());
        dto.setComment(request.getComment());
        dto.setCourseworkId(request.getCourseworkId());
        return dto;
    }

    public CourseworkRequest toRequest() {
        CourseworkRequest request = new CourseworkRequest();
        request.setId(getId());
        request.setTitle(getTitle());
        return request;
    }
}
