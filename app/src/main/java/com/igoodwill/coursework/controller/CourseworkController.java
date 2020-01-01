package com.igoodwill.coursework.controller;

import com.igoodwill.coursework.elastic.model.Attachment;
import com.igoodwill.coursework.elastic.model.Coursework;
import com.igoodwill.coursework.elastic.repository.CourseworkRepository;
import com.igoodwill.coursework.model.CourseworkDto;
import lombok.RequiredArgsConstructor;
import org.apache.commons.codec.binary.Base64;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("coursework")
@RequiredArgsConstructor
public class CourseworkController {

    private final CourseworkRepository repository;

    @PostMapping
    public CourseworkDto create(@RequestBody CourseworkDto coursework) {
        return CourseworkDto.from(repository.create(coursework.toCoursework()));
    }

    @PutMapping
    public void updateTitle(@RequestBody CourseworkDto coursework) {
        repository.update(coursework.toCoursework());
    }

    @PutMapping("{id}/file")
    public void uploadFile(@PathVariable String id, @RequestParam MultipartFile file) throws IOException {
        Coursework coursework = new Coursework();
        coursework.setId(id);
        coursework.setFile(Base64.encodeBase64String(file.getBytes()));

        repository.update(coursework);
    }

    @GetMapping(value = "{id}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) {
        Coursework coursework = repository.getById(id);
        Attachment attachment = coursework.getAttachment();
        return ResponseEntity
                .ok()
                .contentType(MediaType.parseMediaType(attachment.getContentType()))
                .body(new ByteArrayResource(Base64.decodeBase64(coursework.getFile())));
    }

    @GetMapping("search")
    public Page<CourseworkDto> search(
            @RequestParam String searchQuery,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String sortField
    ) {
        Sort sort;
        if (sortDirection != null && sortField != null) {
            sort = Sort.by(Sort.Direction.fromString(sortDirection), sortField);
        } else {
            sort = Sort.unsorted();
        }

        return repository
                .searchByTitleAndAttachmentContent(searchQuery, PageRequest.of(page, size, sort))
                .map(CourseworkDto::from);
    }

    @DeleteMapping
    public void delete(@RequestParam String id) {
        repository.deleteById(id);
    }
}
