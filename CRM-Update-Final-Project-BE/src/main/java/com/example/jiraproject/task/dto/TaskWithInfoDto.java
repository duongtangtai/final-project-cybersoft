package com.example.jiraproject.task.dto;

import com.example.jiraproject.common.util.DateTimeUtil;
import com.example.jiraproject.project.dto.ProjectDto;
import com.example.jiraproject.task.model.Task;
import com.example.jiraproject.user.dto.UserDto;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
public class TaskWithInfoDto {

    private UUID id;
    private String name;
    private String description;

    @DateTimeFormat(pattern = DateTimeUtil.DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_FORMAT)
    private LocalDate startDateExpected;

    @DateTimeFormat(pattern = DateTimeUtil.DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_FORMAT)
    private LocalDate endDateExpected;

    @DateTimeFormat(pattern = DateTimeUtil.DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_FORMAT)
    private LocalDate startDateInFact;

    @DateTimeFormat(pattern = DateTimeUtil.DATE_FORMAT)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtil.DATE_FORMAT)
    private LocalDate endDateInFact;
    private Task.Status status;
    private ProjectDto project;
    private UserDto reporter;
}
