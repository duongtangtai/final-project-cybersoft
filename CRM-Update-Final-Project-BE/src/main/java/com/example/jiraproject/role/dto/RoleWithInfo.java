package com.example.jiraproject.role.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class RoleWithInfo {
    private UUID id;
    private String name;
    private String code;
    private String description;
}
