package com.example.jiraproject.profile.dto;

import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ChangePasswordForm {
    @UUIDConstraint
    private UUID userId;

    @Size(min = 5, max = 300, message = "{user.password.size}")
    @NotBlank(message = "{user.password.not-blank}")
    private String oldPassword;

    @Size(min = 5, max = 300, message = "{user.password.size}")
    @NotBlank(message = "{user.password.not-blank}")
    private String newPassword;
}
