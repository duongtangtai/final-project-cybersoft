package com.example.jiraproject.operation.dto;

import com.example.jiraproject.common.validation.annotation.FieldNotNull;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.operation.model.Operation;
import com.example.jiraproject.operation.validation.annotation.UniqueOperation;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@UniqueOperation(groups = {SaveInfo.class, UpdateInfo.class})
public class OperationDto {

    @UUIDConstraint(groups = UpdateInfo.class)
    private UUID id;

    @Size(max = 100, message = "{operation.name.size}", groups = {SaveInfo.class, UpdateInfo.class})
    @NotBlank(message = "{operation.name.not-blank}", groups = {SaveInfo.class, UpdateInfo.class})
    private String name;

    @Size(max = 500, message = "{operation.description.size}", groups = {SaveInfo.class, UpdateInfo.class})
    @NotBlank(message = "{operation.description.not-blank}", groups = {SaveInfo.class, UpdateInfo.class})
    private String description;

    @FieldNotNull(target = Operation.Type.class, groups = {SaveInfo.class, UpdateInfo.class})
    private Operation.Type type;
}
