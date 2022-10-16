package com.example.jiraproject.notification.dto;

import com.example.jiraproject.common.util.DateTimeUtil;
import com.example.jiraproject.notification.model.Notification;
import com.example.jiraproject.user.dto.UserDto;
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
