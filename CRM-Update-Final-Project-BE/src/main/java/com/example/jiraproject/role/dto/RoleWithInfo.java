package com.example.jiraproject.role.dto;

import com.example.jiraproject.operation.dto.OperationDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class RoleWithInfo {
    private UUID id;
    private String name;
    private String code;
    private String description;
    private Set<OperationDto> operations;
}
