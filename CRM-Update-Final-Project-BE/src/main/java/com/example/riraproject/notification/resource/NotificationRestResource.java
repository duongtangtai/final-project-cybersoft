package com.example.riraproject.notification.resource;

import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.common.util.ResponseUtil;
import com.example.riraproject.common.validation.annotation.UUIDConstraint;
import com.example.riraproject.notification.model.Notification;
import com.example.riraproject.notification.service.NotificationService;
import com.example.riraproject.role.util.RoleUtil;
import com.example.riraproject.security.aop.Authorized;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.UUID;

@RestController
@RequestMapping("/v1/notifications")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class NotificationRestResource {
    private final NotificationService service;
    private final MessageSource messageSource;

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/with-info/sent/by-receiver/{receiverId}")
    public ResponseEntity<ResponseDto> findAllSentByReceiverId(@PathVariable("receiverId") @UUIDConstraint String receiverId) {
        return ResponseUtil.get(service.findAllWithReceiverAndStatus(UUID.fromString(receiverId), Notification.Status.SENT), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/with-info/read/by-receiver/{receiverId}")
    public ResponseEntity<ResponseDto> findAllReadByReceiver(@PathVariable("receiverId") @UUIDConstraint String receiverId) {
        return ResponseUtil.get(service.findAllWithReceiverAndStatus(UUID.fromString(receiverId), Notification.Status.READ), HttpStatus.OK);
    }

    @GetMapping(value = "/subscribe/{token}", consumes = MediaType.ALL_VALUE)
    public SseEmitter subscribe(@PathVariable("token") String token) {
        return service.subscribe(token);
    }

    @PostMapping("/unsubscribe/{username}")
    public ResponseEntity<ResponseDto> unsubscribe(@PathVariable("username") String username) {
        service.unsubscribe(username);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource,
                "notification.unsubscribed"), HttpStatus.OK );
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @PostMapping("/read-all/by-receiver/{receiverId}")
    public ResponseEntity<ResponseDto> readAllByReceiver(@PathVariable("receiverId") @UUIDConstraint String receiverId) {
        service.readAllByReceiver(UUID.fromString(receiverId));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "notification.read"), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@PathVariable("id") @UUIDConstraint String id) {
        service.deleteById(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "notification.deleted"), HttpStatus.OK);
    }
}
