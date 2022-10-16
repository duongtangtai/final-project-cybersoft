package com.example.jiraproject.project.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.project.dto.ProjectDto;
import com.example.jiraproject.project.model.Project;
import com.example.jiraproject.project.service.ProjectService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/projects")
@RequiredArgsConstructor
@Validated
public class ProjectRestResource {
    private final ProjectService service;
    private final MessageSource messageSource;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findById(ProjectDto.class, UUID.fromString(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> findAll() {
        return ResponseUtil.get(service.findAll(ProjectDto.class), HttpStatus.OK);
    }

    @GetMapping("/paging")
    public ResponseEntity<ResponseDto> findAllWithPaging(@RequestParam("size") int size,
                                                         @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithPaging(ProjectDto.class, size, pageIndex), HttpStatus.OK);
    }

    @GetMapping("/{id}/with-info")
    public ResponseEntity<ResponseDto> findByIdWithInfo(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findByIdWithInfo(UUID.fromString(id)), HttpStatus.OK);
    }


    @GetMapping("/with-info")
    public ResponseEntity<ResponseDto> findAllWithInfo() {
        return ResponseUtil.get(service.findAllWithInfo(), HttpStatus.OK);
    }

    @GetMapping("/with-info/paging")
    public ResponseEntity<ResponseDto> findAllWithInfoWithPaging(@RequestParam("size") int size,
                                                                 @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithInfoWithPaging(size, pageIndex), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDto> save(@RequestBody @Validated(SaveInfo.class) ProjectDto dto) {
        return ResponseUtil.get(service.save(Project.class, dto), HttpStatus.CREATED);
    }

    @PostMapping("/{id}/add-creator")
    public ResponseEntity<ResponseDto> addCreator(@PathVariable("id") @UUIDConstraint String projectId,
                                                  @RequestParam("userId") @UUIDConstraint String userId) {
        return ResponseUtil.get(service.addCreator(UUID.fromString(projectId), UUID.fromString(userId)), HttpStatus.OK);
    }

    @PostMapping("/{id}/remove-creator")
    public ResponseEntity<ResponseDto> removeCreator(@PathVariable("id") @UUIDConstraint String projectId) {
        return ResponseUtil.get(service.removeCreator(UUID.fromString(projectId)), HttpStatus.OK);
    }

    @PostMapping("/{id}/add-leader")
    public ResponseEntity<ResponseDto> addLeader(@PathVariable("id") @UUIDConstraint String projectId,
                                                  @RequestParam("userId") @UUIDConstraint String userId) {
        return ResponseUtil.get(service.addLeader(UUID.fromString(projectId), UUID.fromString(userId)), HttpStatus.OK);
    }

    @PostMapping("/{id}/remove-leader")
    public ResponseEntity<ResponseDto> removeLeader(@PathVariable("id") @UUIDConstraint String projectId) {
        return ResponseUtil.get(service.removeLeader(UUID.fromString(projectId)), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ResponseDto> update(@RequestBody @Validated(UpdateInfo.class) ProjectDto dto) {
        return ResponseUtil.get(service.update(dto.getId(), dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@PathVariable("id") @UUIDConstraint String id) {
        service.deleteById(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "project.deleted"), HttpStatus.OK);
    }
}
