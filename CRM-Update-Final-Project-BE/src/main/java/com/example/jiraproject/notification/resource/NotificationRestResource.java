package com.example.jiraproject.notification.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.ApiUtil;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.notification.dto.NotificationDto;
import com.example.jiraproject.notification.service.NotificationService;
import com.example.jiraproject.operation.model.Operation;
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
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class NotificationRestResource {
    private final NotificationService service;
    private final MessageSource messageSource;

    @Authorized(operation = ApiUtil.NOTIFICATION)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findById(NotificationDto.class, UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.NOTIFICATION)
    @GetMapping
    public ResponseEntity<ResponseDto> findAll() {
        return ResponseUtil.get(service.findAll(NotificationDto.class), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.NOTIFICATION)
    @GetMapping("/with-paging")
    public ResponseEntity<ResponseDto> findAllWithPaging(@RequestParam("size") int size,
                                                         @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithPaging(NotificationDto.class, size, pageIndex), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.NOTIFICATION)
    @GetMapping("/{id}/with-info")
    public ResponseEntity<ResponseDto> findByIdWithInfo(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findByIdWithInfo(UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.NOTIFICATION)
    @GetMapping("/with-info")
    public ResponseEntity<ResponseDto> findAllWithInfo() {
        return ResponseUtil.get(service.findAllWithInfo(), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.NOTIFICATION)
    @GetMapping("/with-info/paging")
    public ResponseEntity<ResponseDto> findAllWithInfoWithPaging(@RequestParam("size") int size,
                                                                 @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithInfoWithPaging(size, pageIndex), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.NOTIFICATION)
    @GetMapping("/with-info/by-receiver/{receiverId}")
    public ResponseEntity<ResponseDto> findAllWithInfoByReceiverId(@PathVariable("receiverId") @UUIDConstraint String receiverId) {
        return ResponseUtil.get(service.findAllWithInfoByReceiverId(UUID.fromString(receiverId)), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.NOTIFICATION, type = Operation.Type.SAVE_OR_UPDATE)
    @PostMapping
    public ResponseEntity<ResponseDto> save(@RequestBody @Validated(SaveInfo.class) NotificationDto dto,
                                            @RequestParam("fromId") @UUIDConstraint String fromId,
                                            @RequestParam("toId") @UUIDConstraint String toId) {
        return ResponseUtil.get(service.saveNotification(dto, UUID.fromString(fromId), UUID.fromString(toId)),
                HttpStatus.CREATED);
    }

    @Authorized(operation = ApiUtil.NOTIFICATION, type = Operation.Type.SAVE_OR_UPDATE)
    @PutMapping("/{id}") //update from "SENT" status -> "READ" status
    public ResponseEntity<ResponseDto> update(@PathVariable("id") @UUIDConstraint String id){
        return ResponseUtil.get(service.updateNotification(UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.NOTIFICATION, type = Operation.Type.REMOVE)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@PathVariable("id") @UUIDConstraint String id) {
        service.deleteById(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "notification.deleted"), HttpStatus.OK);
    }
}
