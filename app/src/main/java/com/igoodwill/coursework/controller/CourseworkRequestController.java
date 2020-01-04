package com.igoodwill.coursework.controller;

import com.igoodwill.coursework.elastic.model.CourseworkRequest;
import com.igoodwill.coursework.elastic.repository.request.CourseworkRequestRepository;
import com.igoodwill.coursework.model.CourseworkRequestDto;
import com.igoodwill.coursework.security.service.UserService;
import com.igoodwill.coursework.util.FileService;
import com.igoodwill.coursework.util.PaginationService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.UUID;

@RestController
@RequestMapping("courseworkRequest")
@RequiredArgsConstructor
public class CourseworkRequestController {

    private final CourseworkRequestRepository repository;
    private final FileService fileService;
    private final PaginationService paginationService;
    private final UserService userService;

    @PostMapping
    public CourseworkRequestDto create(@RequestBody CourseworkRequestDto dto) {
        UUID currentUserId = userService.getCurrentUserId();

        CourseworkRequest request = dto.toRequest();
        request.setCreatorId(currentUserId);
        return CourseworkRequestDto.from(repository.create(request), userService);
    }

    @PreAuthorize(
            "@userService.isCurrentUserAdmin()" +
                    "|| @courseworkRequestRepository.isCreatedBy(#dto.getId(), @userService.getCurrentUserId())"
    )
    @PutMapping
    public void updateTitle(@RequestBody CourseworkRequestDto dto) {
        CourseworkRequest request = new CourseworkRequest();
        request.setId(dto.getId());
        request.setTitle(dto.getTitle());
        repository.update(request);
    }

    @PreAuthorize(
            "@userService.isCurrentUserAdmin()" +
                    "|| @courseworkRequestRepository.isCreatedBy(#id, @userService.getCurrentUserId())"
    )
    @PutMapping("{id}/file")
    public void uploadFile(@PathVariable String id, @RequestParam MultipartFile file) throws IOException {
        CourseworkRequest request = new CourseworkRequest();
        request.setId(id);
        request.setFile(fileService.toBase64(file));
        request.setFilename(file.getOriginalFilename());

        repository.update(request);
    }

    @PreAuthorize("@userService.isCurrentUserAdmin()")
    @PutMapping("{id}/approve")
    public CourseworkRequestDto approve(@PathVariable String id) {
        UUID currentUserId = userService.getCurrentUserId();
        return CourseworkRequestDto.from(repository.approve(id, currentUserId), userService);
    }

    @PreAuthorize("@userService.isCurrentUserAdmin()")
    @PutMapping("{id}/reject")
    public void reject(@PathVariable String id, @RequestBody String comment) {
        repository.reject(id, comment);
    }

    @PreAuthorize(
            "@userService.isCurrentUserAdmin()" +
                    "|| @courseworkRequestRepository.isCreatedBy(#id, @userService.getCurrentUserId())"
    )
    @PutMapping("{id}/close")
    public void close(@PathVariable String id, @RequestBody String comment) {
        repository.close(id, comment);
    }

    @PreAuthorize(
            "@userService.isCurrentUserAdmin()" +
                    "|| @courseworkRequestRepository.isCreatedBy(#id, @userService.getCurrentUserId())"
    )
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
        Page<CourseworkRequest> searchResult;
        if (userService.isCurrentUserAdmin()) {
            searchResult = repository.search(
                    searchQuery,
                    paginationService.getPageable(page, size, sortDirection, sortField)
            );
        } else {
            searchResult = repository.search(
                    searchQuery,
                    paginationService.getPageable(page, size, sortDirection, sortField),
                    userService.getCurrentUserId()
            );
        }

        return searchResult.map(request -> CourseworkRequestDto.from(request, userService));
    }
}
