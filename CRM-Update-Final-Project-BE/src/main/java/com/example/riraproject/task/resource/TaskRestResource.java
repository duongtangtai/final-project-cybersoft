package com.example.riraproject.task.resource;

import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.common.util.ResponseUtil;
import com.example.riraproject.common.validation.annotation.UUIDConstraint;
import com.example.riraproject.common.validation.group.SaveInfo;
import com.example.riraproject.common.validation.group.UpdateInfo;
import com.example.riraproject.role.util.RoleUtil;
import com.example.riraproject.security.aop.Authorized;
import com.example.riraproject.task.dto.TaskDto;
import com.example.riraproject.task.service.TaskService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/tasks")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class TaskRestResource {
    private final TaskService service;
    private final MessageSource messageSource;

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findById(TaskDto.class, UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping
    public ResponseEntity<ResponseDto> findAll() {
        return ResponseUtil.get(service.findAll(TaskDto.class), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/paging")
    public ResponseEntity<ResponseDto> findAllWithPaging(@RequestParam("size") int size,
                                                         @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithPaging(TaskDto.class, size, pageIndex), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/with-info/{id}")
    public ResponseEntity<ResponseDto> findByIdWithInfo(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findByIdWithInfo(UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/with-info")
    public ResponseEntity<ResponseDto> findAllWithInfo(){
        return ResponseUtil.get(service.findAllWithInfo(), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/with-info/paging")
    public ResponseEntity<ResponseDto> findAllWithInfoWithPaging(@RequestParam("size") int size,
                                                                           @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithInfoWithPaging(size, pageIndex), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/by-project/{projectId}")
    public ResponseEntity<ResponseDto> findAllByProject(@PathVariable("projectId") @UUIDConstraint String projectId) {
        return ResponseUtil.get(service.findAllByProject(UUID.fromString(projectId)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/by-project-and-user/{projectId}/{userId}")
    public ResponseEntity<ResponseDto> findAllByProjectAndUser(@PathVariable("projectId") @UUIDConstraint String projectId,
                                                               @PathVariable("userId") @UUIDConstraint String userId) {
        return ResponseUtil.get(service.findAllByProjectAndUser(UUID.fromString(projectId), UUID.fromString(userId)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/by-user/{userId}")
    public ResponseEntity<ResponseDto> findAllByUser(@PathVariable("userId") @UUIDConstraint String userId) {
        return ResponseUtil.get(service.findAllByUser(UUID.fromString(userId)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/status")
    public ResponseEntity<ResponseDto> findAllStatus() {
        return ResponseUtil.get(service.findAllStatus(), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.LEADER})
    @PostMapping
    public ResponseEntity<ResponseDto> save(@RequestBody @Validated(SaveInfo.class) TaskDto taskDto) {
        service.save(taskDto);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "task.saved"), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.EMPLOYEE})
    @PostMapping("work-with-task/{id}")
    public ResponseEntity<ResponseDto> workWithTask(@PathVariable("id") @UUIDConstraint String id) {
        service.workWithTask(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "task.in-progress"), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.EMPLOYEE})
    @PostMapping("/complete-task/{id}")
    public ResponseEntity<ResponseDto> completeTask(@PathVariable("id") @UUIDConstraint String id) {
        service.completeTask(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "task.completed"), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.LEADER})
    @PutMapping
    public ResponseEntity<ResponseDto> update(@RequestBody @Validated(UpdateInfo.class) TaskDto taskDto) {
        service.update(taskDto);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "task.updated"), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.LEADER})
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@PathVariable("id") @UUIDConstraint String id) {
        service.deleteById(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "task.deleted"), HttpStatus.OK);
    }
}
