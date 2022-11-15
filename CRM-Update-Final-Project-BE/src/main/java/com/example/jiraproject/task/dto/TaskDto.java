package com.example.jiraproject.task.dto;

import com.example.jiraproject.common.util.DateTimeUtil;
import com.example.jiraproject.common.validation.annotation.FieldNotNull;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.task.model.Task;
import com.example.jiraproject.task.validation.annotation.UniqueTask;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.time.LocalDate;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UniqueTask(groups = {SaveInfo.class, UpdateInfo.class})
public class TaskDto {

    @UUIDConstraint(groups = UpdateInfo.class)
    private UUID id;

    @Size(min = 5, max = 50, message = "{task.name.size}", groups = {SaveInfo.class, UpdateInfo.class})
    @NotBlank(message = "{task.name.not-blank}", groups = {SaveInfo.class, UpdateInfo.class})
    private String name;

    @Size(min = 5, max = 300, message = "{task.description.size}", groups = {SaveInfo.class, UpdateInfo.class})
    @NotBlank(message = "{task.description.not-blank}", groups = {SaveInfo.class, UpdateInfo.class})
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

    @FieldNotNull(message = "{task.status.not-null}", groups = {SaveInfo.class, UpdateInfo.class})
    private Task.Status status;

    @Size(min = 5, max = 50, message = "{project.name.size}")
    @NotBlank(message = "{project.name.not-blank}")
    private String projectName;

    @Size(max = 25, message = "{task.reporterUsername.size}", groups = {SaveInfo.class, UpdateInfo.class})
//    @NotBlank(message = "{task.reporterUsername.not-blank}", groups = {SaveInfo.class, UpdateInfo.class})
    private String reporterUsername;
}
