package com.example.riraproject.security.resource;

import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.security.dto.LoginFormDto;
import com.example.riraproject.security.dto.LoginResultDto;
import com.example.riraproject.security.dto.RefreshTokenDto;
import com.example.riraproject.security.service.AuthService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class AuthRestResourceTest {
    @Mock private AuthService service;
    private AuthRestResource restResource;

    @BeforeEach
    void init() {
        restResource = new AuthRestResource(service);
    }

    @Test
    void loginTest() {
        //SETUP
        LoginFormDto dto = Mockito.mock(LoginFormDto.class);
        LoginResultDto loginResult = Mockito.mock(LoginResultDto.class);
        Mockito.when(service.login(dto)).thenReturn(loginResult);
        //TRY
        ResponseEntity<ResponseDto> result = restResource.login(dto);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(loginResult, body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).login(dto);
    }

    @Test
    void refreshTokenTest() {
        //SETUP
        String refreshToken = "refreshToken";
        RefreshTokenDto refreshTokenDto = Mockito.mock(RefreshTokenDto.class);
        Mockito.when(service.refreshToken(refreshToken)).thenReturn(refreshTokenDto);
        //TRY
        ResponseEntity<ResponseDto> result = restResource.refreshToken(refreshToken);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(refreshTokenDto, body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).refreshToken(refreshToken);
    }
}
