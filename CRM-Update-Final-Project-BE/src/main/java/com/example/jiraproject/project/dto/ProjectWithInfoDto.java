package com.example.jiraproject.project.dto;

import com.example.jiraproject.user.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
public class ProjectWithInfoDto {
    private UUID id;
    private String name;
    private String description;
    private String symbol;
    private UserDto creator;
    private UserDto leader;
}
