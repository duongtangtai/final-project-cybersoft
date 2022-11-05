package com.example.jiraproject.profile.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.profile.dto.ChangePasswordForm;
import com.example.jiraproject.profile.service.ProfileService;
import com.example.jiraproject.role.util.RoleUtil;
import com.example.jiraproject.security.aop.Authorized;
import com.example.jiraproject.user.dto.UserDto;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/v1/profiles")
@RequiredArgsConstructor
@SecurityRequirement(name = "bearerAuth")
public class ProfileRestResource {
    private final ProfileService service;
    private final MessageSource messageSource;

    @Authorized(roles = {RoleUtil.EMPLOYEE})
    @PutMapping("/update-profile")
    public ResponseEntity<ResponseDto> updateProfile(@RequestBody @Validated(UpdateInfo.class) UserDto dto) {
        return ResponseUtil.get(service.updateProfile(dto),HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.EMPLOYEE})
    @PutMapping("/change-password")
    public ResponseEntity<ResponseDto> changePassword(@RequestBody @Valid ChangePasswordForm form) {
        service.changePassword(form);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "user.password.changed"),HttpStatus.OK);
    }
}
