package com.example.jiraproject.file.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.file.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/v1/files")
@RequiredArgsConstructor
public class FileRestResource {
    private final FileService service;

    @GetMapping("/{id}")
    public ResponseEntity<Resource> findFindById(@PathVariable("id") String fileId) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(service.load(fileId));
    }

    @PostMapping("/{userId}")
    public ResponseEntity<ResponseDto> uploadFile(@PathVariable("userId") @UUIDConstraint String userId,
                                                  @RequestParam("file") MultipartFile file) {
        service.save(userId, file);
        return ResponseUtil.get("Upload file successfully!", HttpStatus.OK);
    }
}
