package com.example.riraproject.comment.dto;

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
public class CommentDto {

    @UUIDConstraint(groups = {UpdateInfo.class})
    private UUID id;

    @Size(max = 300, message = "{comment.description.size}", groups = {SaveInfo.class, UpdateInfo.class})
    @NotBlank(message = "{comment.description.not-blank}", groups = {SaveInfo.class, UpdateInfo.class})
    private String description;

    @UUIDConstraint(groups = {SaveInfo.class, UpdateInfo.class})
    private UUID writerId;

    @UUIDConstraint(groups = {SaveInfo.class, UpdateInfo.class})
    private UUID taskId;

    private UUID responseToId; //responseToId can be null
}
