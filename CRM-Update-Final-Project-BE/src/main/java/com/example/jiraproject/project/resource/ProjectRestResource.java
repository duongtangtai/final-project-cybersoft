package com.example.jiraproject.project.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.ApiUtil;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.operation.model.Operation;
import com.example.jiraproject.project.dto.ProjectDto;
import com.example.jiraproject.project.service.ProjectService;
import com.example.jiraproject.security.aop.Authorized;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class ProjectRestResource {
    private final ProjectService service;
    private final MessageSource messageSource;

    @Authorized(operation = ApiUtil.PROJECT)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findById(ProjectDto.class, UUID.fromString(id)), HttpStatus.OK);
    }

//    @Authorized(operation = ApiUtil.PROJECT)
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping
    public ResponseEntity<ResponseDto> findAll() {
        return ResponseUtil.get(service.findAll(ProjectDto.class), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.PROJECT)
    @GetMapping("/paging")
    public ResponseEntity<ResponseDto> findAllWithPaging(@RequestParam("size") int size,
                                                         @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithPaging(ProjectDto.class, size, pageIndex), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.PROJECT)
    @GetMapping("/{id}/with-info")
    public ResponseEntity<ResponseDto> findByIdWithInfo(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findByIdWithInfo(UUID.fromString(id)), HttpStatus.OK);
    }


    @Authorized(operation = ApiUtil.PROJECT)
    @GetMapping("/with-info")
    public ResponseEntity<ResponseDto> findAllWithInfo() {
        return ResponseUtil.get(service.findAllWithInfo(), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.PROJECT)
    @GetMapping("/with-info/paging")
    public ResponseEntity<ResponseDto> findAllWithInfoWithPaging(@RequestParam("size") int size,
                                                                 @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithInfoWithPaging(size, pageIndex), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.PROJECT)
    @GetMapping("/status")
    public ResponseEntity<ResponseDto> findAllProjectStatus() {
        return ResponseUtil.get(service.findAllProjectStatus(), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.PROJECT, type = Operation.Type.SAVE_OR_UPDATE)
    @PostMapping
    public ResponseEntity<ResponseDto> save(@RequestBody @Validated(SaveInfo.class) ProjectDto dto) {
        service.save(dto);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "project.saved"), HttpStatus.CREATED);
    }

    @Authorized(operation = ApiUtil.PROJECT, type = Operation.Type.SAVE_OR_UPDATE)
    @PutMapping
    public ResponseEntity<ResponseDto> update(@RequestBody @Validated(UpdateInfo.class) ProjectDto dto) {
        service.update(dto.getId(), dto);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "project.updated"), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.PROJECT, type = Operation.Type.REMOVE)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@PathVariable("id") @UUIDConstraint String id) {
        service.deleteById(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "project.deleted"), HttpStatus.OK);
    }
}
