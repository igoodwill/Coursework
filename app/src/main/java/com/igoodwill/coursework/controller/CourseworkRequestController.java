package com.igoodwill.coursework.controller;

import com.igoodwill.coursework.elastic.model.CourseworkRequest;
import com.igoodwill.coursework.elastic.repository.request.CourseworkRequestRepository;
import com.igoodwill.coursework.model.CourseworkRequestDto;
import com.igoodwill.coursework.util.FileService;
import com.igoodwill.coursework.util.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

@RestController
@RequestMapping("courseworkRequest")
@RequiredArgsConstructor
public class CourseworkRequestController {

    private final CourseworkRequestRepository repository;
    private final FileService fileService;
    private final PaginationService paginationService;

    @PostMapping
    public CourseworkRequestDto create(@RequestBody CourseworkRequestDto request) {
        return CourseworkRequestDto.from(repository.create(request.toRequest()));
    }

    @PutMapping
    public void updateTitle(@RequestBody CourseworkRequestDto dto) {
        CourseworkRequest request = new CourseworkRequest();
        request.setId(dto.getId());
        request.setTitle(dto.getTitle());
        repository.update(request);
    }

    @PutMapping("{id}/file")
    public void uploadFile(@PathVariable String id, @RequestParam MultipartFile file) throws IOException {
        CourseworkRequest request = new CourseworkRequest();
        request.setId(id);
        request.setFile(fileService.toBase64(file));
        request.setFilename(file.getOriginalFilename());

        repository.update(request);
    }

    @PutMapping("{id}/approve")
    public void approve(@PathVariable String id) {
        repository.approve(id);
    }

    @PutMapping("{id}/reject")
    public void reject(@PathVariable String id, @RequestBody String comment) {
        repository.reject(id, comment);
    }

    @PutMapping("{id}/close")
    public void close(@PathVariable String id, @RequestBody String comment) {
        repository.close(id, comment);
    }

    @GetMapping(value = "{id}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) throws UnsupportedEncodingException {
        CourseworkRequest request = repository.getById(id);
        return fileService.downloadFile(request);
    }

    @GetMapping("search")
    public Page<CourseworkRequestDto> search(
            @RequestParam String searchQuery,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String sortField
    ) {
        return repository
                .search(searchQuery, paginationService.getPageable(page, size, sortDirection, sortField))
                .map(CourseworkRequestDto::from);
    }
}
