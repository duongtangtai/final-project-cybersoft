package com.example.jiraproject.profile.service;

import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.profile.dto.ChangePasswordForm;
import com.example.jiraproject.role.model.Role;
import com.example.jiraproject.security.dto.LoginResultDto;
import com.example.jiraproject.security.util.JwtUtil;
import com.example.jiraproject.user.dto.UserDto;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;

public interface ProfileService {
    LoginResultDto updateProfile(UserDto dto);
    void changePassword(ChangePasswordForm form);
}
@Service
@Transactional
@RequiredArgsConstructor
class ProfileServiceImpl implements ProfileService {
    private final UserService userService;
    private final ModelMapper mapper;
    private final JwtUtil jwtUtil;
    private final MessageSource messageSource;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public LoginResultDto updateProfile(UserDto dto) { //USER CAN ONLY UPDATE THEIR OWN PROFILE
        String authenticatedUsername = jwtUtil.getAuthenticatedUsername();
        if (!authenticatedUsername.equals(dto.getUsername())) { // if this is not their profile
            throw new ValidationException(MessageUtil.getMessage(messageSource, "user.invalid"));
        }
        User user = userService.findUserById(dto.getId());
        //When user update their profile, they can't change their username & password & avatar
        String username = user.getUsername(); // store username
        String password = user.getPassword();
        mapper.map(dto, user);
        user.setUsername(username); // return back username & password
        user.setPassword(password);
        UserDto userDto = mapper.map(user, UserDto.class);
        return LoginResultDto.builder()
                .userData(userDto)
                .roleCodes(user.getRoles().stream().map(Role::getCode).toList())
                .build();
    }

    @Override
    public void changePassword(ChangePasswordForm form) {
        User user = userService.findUserById(form.getUserId());
        if (passwordEncoder.matches(form.getOldPassword(), user.getPassword())) {
            user.setPassword(passwordEncoder.encode(form.getNewPassword()));
        } else {
            throw new ValidationException(MessageUtil.getMessage(messageSource, "user.password.incorrect"));
        }
    }
}
