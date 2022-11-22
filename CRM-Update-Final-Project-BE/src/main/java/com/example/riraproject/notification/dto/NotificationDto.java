package com.example.riraproject.notification.dto;

import com.example.riraproject.common.validation.annotation.UUIDConstraint;
import com.example.riraproject.common.validation.group.SaveInfo;
import com.example.riraproject.common.validation.group.UpdateInfo;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDto {

    @UUIDConstraint(groups = UpdateInfo.class)
    private UUID id;

    @Size(max = 200, message = "{notification.description.size}", groups = SaveInfo.class)
    @NotBlank(message = "{notification.description.not-blank}", groups = SaveInfo.class)
    private String description;
}
