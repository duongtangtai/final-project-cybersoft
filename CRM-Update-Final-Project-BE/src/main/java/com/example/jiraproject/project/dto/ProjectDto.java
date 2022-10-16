package com.example.jiraproject.project.dto;

import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.project.validation.annotation.UniqueProject;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UniqueProject(groups = {SaveInfo.class, UpdateInfo.class})
public class ProjectDto {

    @UUIDConstraint(groups = UpdateInfo.class)
    private UUID id;

    @Size(min = 5, max = 50, message = "{project.name.size}", groups = {SaveInfo.class, UpdateInfo.class})
    @NotBlank(message = "{project.name.not-blank}", groups = {SaveInfo.class, UpdateInfo.class})
    private String name;

    @Size(min = 5, max = 500, message = "{project.description.size}", groups = {SaveInfo.class, UpdateInfo.class})
    @NotBlank(message = "{project.description.not-blank}", groups = {SaveInfo.class, UpdateInfo.class})
    private String description;

    @Size(min = 5, max = 100, message = "{project.symbol.size}", groups = {SaveInfo.class, UpdateInfo.class})
    private String symbol;
}
