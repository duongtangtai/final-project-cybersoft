package com.example.jiraproject.file.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.ApiUtil;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.file.dto.FileInfoDto;
import com.example.jiraproject.file.service.FileService;
import com.example.jiraproject.file.util.FileUtil;
import com.example.jiraproject.operation.model.Operation;
import com.example.jiraproject.security.aop.Authorized;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.MessageSource;
import org.springframework.context.event.EventListener;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

import java.util.List;

@RestController
@RequestMapping("/v1/files")
@RequiredArgsConstructor
public class FileRestResource {
    private final FileService service;
    private final MessageSource messageSource;

    @EventListener(ApplicationReadyEvent.class) //call this method after the application is ready
    public void initService() {
        service.init();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Resource> findFindByUsername(@PathVariable("id") String id) {
        return ResponseEntity.ok()
                .contentType(MediaType.IMAGE_JPEG)
                .body(service.load(id));
    }

    @Authorized(operation = ApiUtil.FILE)
    @GetMapping
    public ResponseEntity<List<FileInfoDto>> getAllFileInfo() {
        List<FileInfoDto> fileList = service.loadAll().map(path -> {
            String pathString = path.getFileName().toString();
           String fileName = pathString.substring(0, pathString.length() - FileUtil.SUFFIX.length());
           String url = MvcUriComponentsBuilder
                   .fromMethodName(FileRestResource.class, "findFindByUsername",
                           fileName)
                   .build().toString();
           return FileInfoDto.builder()
                   .fileName(fileName)
                   .url(url)
                   .build();
        }).toList();
        return ResponseEntity.ok().body(fileList);
    }

    @Authorized(operation = ApiUtil.FILE, type = Operation.Type.SAVE_OR_UPDATE)
    @PostMapping("/{userId}")
    public ResponseEntity<ResponseDto> uploadFile(@RequestParam("file") MultipartFile file,
                                                  @PathVariable("userId") @UUIDConstraint String userId) {
        service.save(userId, file);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "file.uploaded"), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.FILE, type = Operation.Type.REMOVE)
    @DeleteMapping
    public ResponseEntity<ResponseDto> deleteAllFiles() {
        service.deleteAll();
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "file.deleted"), HttpStatus.OK);
    }
}
