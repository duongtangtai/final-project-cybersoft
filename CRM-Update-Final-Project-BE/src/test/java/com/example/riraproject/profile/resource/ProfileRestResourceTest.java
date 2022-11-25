package com.example.riraproject.profile.resource;

import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.profile.dto.ChangePasswordForm;
import com.example.riraproject.profile.service.ProfileService;
import com.example.riraproject.security.dto.LoginResultDto;
import com.example.riraproject.user.dto.UserDto;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@ExtendWith(MockitoExtension.class)
public class ProfileRestResourceTest {
    @Mock private ProfileService service;
    @Mock private MessageSource messageSource;

    @InjectMocks private ProfileRestResource restResource;

    @Test
    void updateProfileTest() {
        //SETUP
        UserDto dto = Mockito.mock(UserDto.class);
        LoginResultDto loginResultDto = Mockito.mock(LoginResultDto.class);
        Mockito.when(service.updateProfile(dto)).thenReturn(loginResultDto);
        //TRY
        ResponseEntity<ResponseDto> result = restResource.updateProfile(dto);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto response = result.getBody();
        Assertions.assertEquals(loginResultDto, response.getContent());
        Assertions.assertFalse(response.isHasErrors());
        Assertions.assertNull(response.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assertions.assertNotNull(response.getTimeStamp());
        Mockito.verify(service).updateProfile(dto);
    }

    @Test
    void changePasswordTest() {
        //SETUP
        ChangePasswordForm form = Mockito.mock(ChangePasswordForm.class);
        Mockito.doNothing().when(service).changePassword(form);
        //TRY
        ResponseEntity<ResponseDto> result = restResource.changePassword(form);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto response = result.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource,
                "user.password.changed"), response.getContent());
        Assertions.assertFalse(response.isHasErrors());
        Assertions.assertNull(response.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), response.getStatusCode());
        Assertions.assertNotNull(response.getTimeStamp());
        Mockito.verify(service).changePassword(form);
    }
}
