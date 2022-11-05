package com.example.jiraproject.role.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.role.dto.RoleDto;
import com.example.jiraproject.role.model.Role;
import com.example.jiraproject.role.service.RoleService;
import com.example.jiraproject.role.util.RoleUtil;
import com.example.jiraproject.security.aop.Authorized;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/roles")
@Validated
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class RoleRestResource {
    private final RoleService service;
    private final MessageSource messageSource;

    @Authorized(roles = {RoleUtil.ADMIN})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findById(RoleDto.class, UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.ADMIN})
    @GetMapping
    public ResponseEntity<ResponseDto> findAll() {
        return ResponseUtil.get(service.findAll(RoleDto.class), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.ADMIN})
    @GetMapping("/paging")
    public ResponseEntity<ResponseDto> findAllWithPaging(@RequestParam("size") int size,
                                                         @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithPaging(RoleDto.class, size, pageIndex), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.ADMIN})
    @GetMapping("/{id}/with-info")
    public ResponseEntity<ResponseDto> findByIdWithInfo(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findByIdWithInfo(UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.ADMIN})
    @GetMapping("/with-info")
    public ResponseEntity<ResponseDto> findAllWithInfo() {
        return ResponseUtil.get(service.findAllWithInfo(), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.ADMIN})
    @GetMapping("/with-info/paging")
    public ResponseEntity<ResponseDto> findAllWithInfoWithPaging(@RequestParam("size") int size,
                                                                 @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithInfoWithPaging(size, pageIndex), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.ADMIN})
    @PostMapping
    public ResponseEntity<ResponseDto> save(@RequestBody @Validated(SaveInfo.class) RoleDto roleDto) {
        return ResponseUtil.get(service.save(Role.class, roleDto), HttpStatus.CREATED);
    }

    @Authorized(roles = {RoleUtil.ADMIN})
    @PutMapping
    public ResponseEntity<ResponseDto> update(@RequestBody @Validated(UpdateInfo.class) RoleDto roleDto) {
        return ResponseUtil.get(service.update(roleDto.getId(), roleDto), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@PathVariable("id") @UUIDConstraint String id) {
        service.deleteById(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "role.deleted"), HttpStatus.OK);
    }
}
