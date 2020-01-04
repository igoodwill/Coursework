package com.igoodwill.coursework.model;

import com.igoodwill.coursework.elastic.model.Coursework;
import com.igoodwill.coursework.security.service.UserService;
import lombok.Data;

import java.util.UUID;

@Data
public class CourseworkDto {

    private String id;

    private String title;

    private String filename;

    private UUID creatorId;

    private String creatorName;

    public static CourseworkDto from(Coursework coursework, UserService userService) {
        CourseworkDto dto = new CourseworkDto();
        dto.setId(coursework.getId());
        dto.setTitle(coursework.getTitle());
        dto.setFilename(coursework.getFilename());

        UUID creatorId = coursework.getCreatorId();
        dto.setCreatorId(creatorId);
        dto.setCreatorName(userService.getDisplayUserName(creatorId));
        return dto;
    }

    public Coursework toCoursework() {
        Coursework coursework = new Coursework();
        coursework.setId(getId());
        coursework.setTitle(getTitle());
        return coursework;
    }
}
