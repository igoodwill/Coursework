package com.igoodwill.coursework.controller;

import com.igoodwill.coursework.elastic.model.Coursework;
import com.igoodwill.coursework.elastic.repository.coursework.CourseworkRepository;
import com.igoodwill.coursework.model.CourseworkDto;
import com.igoodwill.coursework.security.service.UserService;
import com.igoodwill.coursework.util.FileService;
import com.igoodwill.coursework.util.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController
@RequestMapping("coursework")
@RequiredArgsConstructor
public class CourseworkController {

    private final CourseworkRepository repository;
    private final FileService fileService;
    private final PaginationService paginationService;
    private final UserService userService;

    @PostMapping
    public CourseworkDto create(@RequestBody CourseworkDto dto, HttpServletRequest request) {
        UUID oid = userService.getCurrentUserId();

        Coursework coursework = dto.toCoursework();
        coursework.setCreatorId(oid);
        return CourseworkDto.from(repository.create(coursework), userService);
    }

    @PutMapping
    public void updateTitle(@RequestBody CourseworkDto dto) {
        Coursework coursework = new Coursework();
        coursework.setId(dto.getId());
        coursework.setTitle(dto.getTitle());
        repository.update(coursework);
    }

    @PutMapping("{id}/file")
    public void uploadFile(@PathVariable String id, @RequestParam MultipartFile file) throws IOException {
        Coursework coursework = new Coursework();
        coursework.setId(id);
        coursework.setFile(fileService.toBase64(file));
        coursework.setFilename(file.getOriginalFilename());

        repository.update(coursework);
    }

    @GetMapping(value = "{id}/file")
    public ResponseEntity<Resource> downloadFile(@PathVariable String id) throws UnsupportedEncodingException {
        Coursework coursework = repository.getById(id);
        return fileService.downloadFile(coursework);
    }

    @GetMapping("search")
    public Page<CourseworkDto> search(
            @RequestParam String searchQuery,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String sortDirection,
            @RequestParam(required = false) String sortField
    ) {
        return repository
                .search(searchQuery, paginationService.getPageable(page, size, sortDirection, sortField))
                .map(coursework -> CourseworkDto.from(coursework, userService));
    }

    @GetMapping("{id}")
    public CourseworkDto get(@PathVariable String id) {
        return CourseworkDto.from(repository.getById(id), userService);
    }

    @DeleteMapping
    public void delete(@RequestParam String id) {
        repository.deleteById(id);
    }
}
