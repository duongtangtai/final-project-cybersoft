package com.example.riraproject.user.dto;

import com.example.riraproject.project.dto.ProjectDto;
import com.example.riraproject.role.dto.RoleDto;
import com.example.riraproject.task.dto.TaskDto;
import com.example.riraproject.user.model.User;
import lombok.Getter;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@Getter
@Setter
public class UserWithProjectInfoDto {
    private UUID id;
    private String username;
    private String password;
    private String firstName;
    private String lastName;
    private User.Gender gender;
    private String avatar;
    private String email;
    private String facebookUrl;
    private String occupation;
    private String department;
    private String hobbies;
    private User.AccountStatus accountStatus;
    private Set<RoleDto> roles;
    private Set<ProjectDto> projects;
}
