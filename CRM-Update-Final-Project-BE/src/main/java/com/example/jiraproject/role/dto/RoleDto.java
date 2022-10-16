package com.example.jiraproject.role.dto;

import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.role.validation.annotation.UniqueRole;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UniqueRole(groups = {SaveInfo.class, UpdateInfo.class})
public class RoleDto implements Comparable<RoleDto> {

    @UUIDConstraint(groups = UpdateInfo.class)
    private UUID id;

    @Size(min = 2, max = 50, message = "{role.name.size}", groups = {SaveInfo.class, UpdateInfo.class})
    @NotBlank(message = "{role.name.not-blank}", groups = {SaveInfo.class, UpdateInfo.class})
    private String name;

    @Size(min = 2, max = 50, message = "{role.code.size}", groups = {SaveInfo.class, UpdateInfo.class})
    @NotBlank(message = "{role.code.not-blank}", groups = {SaveInfo.class, UpdateInfo.class})
    private String code;

    @Size(min = 2, max = 500, message = "{role.description.size}", groups = {SaveInfo.class, UpdateInfo.class})
    @NotBlank(message = "{role.description.not-blank}", groups = {SaveInfo.class, UpdateInfo.class})
    private String description;

    @Override
    public int compareTo(RoleDto o) {
        return 0;
    }
}
