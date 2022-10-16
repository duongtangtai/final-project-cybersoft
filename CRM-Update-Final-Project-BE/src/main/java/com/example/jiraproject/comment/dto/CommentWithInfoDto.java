package com.example.jiraproject.comment.dto;

import com.example.jiraproject.common.util.DateTimeUtil;
import com.example.jiraproject.task.dto.TaskDto;
import com.example.jiraproject.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
public class CommentWithInfoDto {
    private UUID id;
    private String description;

    @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_TIME_FORMAT)
    private LocalDateTime createdAt;

    @DateTimeFormat(pattern = DateTimeUtil.DATE_TIME_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_TIME_FORMAT)
    private LocalDateTime lastModifiedAt;
    private UserDto writer;
    private TaskDto task;
    private CommentDto responseTo;
}
