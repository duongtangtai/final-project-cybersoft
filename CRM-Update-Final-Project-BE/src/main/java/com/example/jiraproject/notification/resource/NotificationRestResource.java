package com.example.jiraproject.notification.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.notification.dto.NotificationDto;
import com.example.jiraproject.notification.service.NotificationService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/notifications")
@RequiredArgsConstructor
@Validated
public class NotificationRestResource {
    private final NotificationService service;
    private final MessageSource messageSource;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findById(NotificationDto.class, UUID.fromString(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> findAll() {
        return ResponseUtil.get(service.findAll(NotificationDto.class), HttpStatus.OK);
    }

    @GetMapping("/with-paging")
    public ResponseEntity<ResponseDto> findAllWithPaging(@RequestParam("size") int size,
                                                         @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithPaging(NotificationDto.class, size, pageIndex), HttpStatus.OK);
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
    public ResponseEntity<ResponseDto> save(@RequestBody @Validated(SaveInfo.class) NotificationDto dto,
                                            @RequestParam("fromId") @UUIDConstraint String fromId,
                                            @RequestParam("toId") @UUIDConstraint String toId) {
        return ResponseUtil.get(service.saveNotification(dto, UUID.fromString(fromId), UUID.fromString(toId)),
                HttpStatus.CREATED);
    }

    @PutMapping("/{id}") //update from "SENT" status -> "READ" status
    public ResponseEntity<ResponseDto> update(@PathVariable("id") @UUIDConstraint String id){
        return ResponseUtil.get(service.updateNotification(UUID.fromString(id)), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@PathVariable("id") @UUIDConstraint String id) {
        service.deleteById(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "notification.deleted"), HttpStatus.OK);
    }
}
