package com.igoodwill.coursework.model;

import com.igoodwill.coursework.elastic.model.Coursework;
import lombok.Data;

@Data
public class CourseworkDto {

    private String id;

    private String title;

    private String filename;

    public static CourseworkDto from(Coursework coursework) {
        CourseworkDto dto = new CourseworkDto();
        dto.setId(coursework.getId());
        dto.setTitle(coursework.getTitle());
        dto.setFilename(coursework.getFilename());
        return dto;
    }

    public Coursework toCoursework() {
        Coursework coursework = new Coursework();
        coursework.setId(getId());
        coursework.setTitle(getTitle());
        return coursework;
    }
}
