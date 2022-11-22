package com.example.riraproject.project.dto;

import com.example.riraproject.project.model.Project;
import com.example.riraproject.user.dto.UserDto;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class ProjectWithInfoDto {
    private UUID id;
    private String name;
    private String description;
    private String symbol;
    private Project.Status status;
    private UserDto creator;
    private UserDto leader;
    private Set<UserDto> users;
}
