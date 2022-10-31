package com.example.jiraproject.profile.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.ApiUtil;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.operation.model.Operation;
import com.example.jiraproject.profile.service.ProfileService;
import com.example.jiraproject.security.aop.Authorized;
import com.example.jiraproject.user.dto.UserDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/v1/profiles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProfileRestResource {
    private final ProfileService service;
    private final MessageSource messageSource;

    @Authorized(operation = ApiUtil.PROFILE, type = Operation.Type.SAVE_OR_UPDATE)
    @PutMapping
    public ResponseEntity<ResponseDto> updateProfile(@RequestBody @Validated(UpdateInfo.class) UserDto dto) {
        return ResponseUtil.get(service.updateProfile(dto),HttpStatus.OK);
    }
}
