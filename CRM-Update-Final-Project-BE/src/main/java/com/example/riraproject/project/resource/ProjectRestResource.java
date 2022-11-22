package com.example.riraproject.project.resource;

import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.common.util.ResponseUtil;
import com.example.riraproject.common.validation.annotation.UUIDConstraint;
import com.example.riraproject.common.validation.group.SaveInfo;
import com.example.riraproject.common.validation.group.UpdateInfo;
import com.example.riraproject.project.dto.ProjectDto;
import com.example.riraproject.project.service.ProjectService;
import com.example.riraproject.role.util.RoleUtil;
import com.example.riraproject.security.aop.Authorized;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/projects")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class ProjectRestResource {
    private final ProjectService service;
    private final MessageSource messageSource;

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findById(ProjectDto.class, UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping
    public ResponseEntity<ResponseDto> findAll() {
        return ResponseUtil.get(service.findAll(ProjectDto.class), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/paging")
    public ResponseEntity<ResponseDto> findAllWithPaging(@RequestParam("size") int size,
                                                         @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithPaging(ProjectDto.class, size, pageIndex), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/with-info/{id}")
    public ResponseEntity<ResponseDto> findByIdWithInfo(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findByIdWithInfo(UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/with-info")
    public ResponseEntity<ResponseDto> findAllWithInfo() {
        return ResponseUtil.get(service.findAllWithInfo(), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/with-info/paging")
    public ResponseEntity<ResponseDto> findAllWithInfoWithPaging(@RequestParam("size") int size,
                                                                 @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithInfoWithPaging(size, pageIndex), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER})
    @GetMapping("/status")
    public ResponseEntity<ResponseDto> findAllProjectStatus() {
        return ResponseUtil.get(service.findAllProjectStatus(), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER})
    @PostMapping
    public ResponseEntity<ResponseDto> save(@RequestBody @Validated(SaveInfo.class) ProjectDto dto) {
        service.save(dto);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "project.saved"), HttpStatus.CREATED);
    }

    @Authorized(roles = {RoleUtil.MANAGER})
    @PostMapping("/add-users/{id}")
    public ResponseEntity<ResponseDto> addUsers(@PathVariable("id") @UUIDConstraint String projectId,
                                                @RequestBody Set<UUID> userIds) {
        service.addUsers(UUID.fromString(projectId), userIds);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "project.add-user.successfully"),
                HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER})
    @PostMapping("/remove-users/{id}")
    public ResponseEntity<ResponseDto> removeUsers(@PathVariable("id") @UUIDConstraint String projectId,
                                                @RequestBody Set<UUID> userIds) {
        service.removeUsers(UUID.fromString(projectId), userIds);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "project.remove-user.successfully"),
                HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER})
    @PutMapping
    public ResponseEntity<ResponseDto> update(@RequestBody @Validated(UpdateInfo.class) ProjectDto dto) {
        service.update(dto);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "project.updated"), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER})
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@PathVariable("id") @UUIDConstraint String id) {
        service.deleteById(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "project.deleted"), HttpStatus.OK);
    }
}
