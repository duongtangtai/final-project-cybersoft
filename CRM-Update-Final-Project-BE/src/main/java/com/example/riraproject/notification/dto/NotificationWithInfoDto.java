package com.example.riraproject.notification.dto;

import com.example.riraproject.common.util.DateTimeUtil;
import com.example.riraproject.notification.model.Notification;
import com.example.riraproject.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class NotificationWithInfoDto {
    private UUID id;
    private UserDto sender;
    private UserDto receiver;
    private String description;
    private Notification.Status status;
    @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_TIME_FORMAT)
    private LocalDateTime createdAt;
}
