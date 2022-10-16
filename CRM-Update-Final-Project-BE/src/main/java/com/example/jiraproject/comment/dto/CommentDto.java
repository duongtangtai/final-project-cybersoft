package com.example.jiraproject.comment.dto;

import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.util.UUID;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CommentDto {

    @UUIDConstraint(groups = {UpdateInfo.class})
    private UUID id;

    @Size(max = 300, message = "{comment.description.size}", groups = {SaveInfo.class, UpdateInfo.class})
    @NotBlank(message = "{comment.description.not-blank}", groups = {SaveInfo.class, UpdateInfo.class})
    private String description;
}
