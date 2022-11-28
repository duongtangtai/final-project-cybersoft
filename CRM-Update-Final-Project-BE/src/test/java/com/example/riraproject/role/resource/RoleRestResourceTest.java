package com.example.riraproject.role.resource;

import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.role.dto.RoleDto;
import com.example.riraproject.role.model.Role;
import com.example.riraproject.role.service.RoleService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RoleRestResourceTest {
    @Mock private RoleService service;
    @Mock private MessageSource messageSource;
    private RoleRestResource restResource;
    private final String id = UUID.randomUUID().toString();
    @Mock private RoleDto dto;

    @BeforeEach
    void init() {
        restResource = new RoleRestResource(service, messageSource);
    }

    @Test
    void findByIdTest() {
        //SETUP
        Mockito.when(service.findById(RoleDto.class, UUID.fromString(id)))
                .thenReturn(dto);
        ResponseEntity<ResponseDto> result = restResource.findById(id);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(dto, body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findById(RoleDto.class, UUID.fromString(id));
    }

    @Test
    void findAllTest() {
        //SETUP
        Mockito.when(service.findAll(RoleDto.class))
                .thenReturn(List.of(dto));
        ResponseEntity<ResponseDto> result = restResource.findAll();
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dto), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAll(RoleDto.class);
    }

    @Test
    void findAllWithPagingTest() {
        //SETUP
        int size = 2;
        int page = 3;
        Mockito.when(service.findAllWithPaging(RoleDto.class, size, page))
                .thenReturn(List.of(dto));
        ResponseEntity<ResponseDto> result = restResource.findAllWithPaging(size, page);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dto), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllWithPaging(RoleDto.class, size, page);
    }

    @Test
    void saveTest() {
        //SETUP
        Mockito.when(service.save(Role.class, dto)).thenReturn(dto);
        //TRY
        ResponseEntity<ResponseDto> result = restResource.save(dto);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(dto, body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.CREATED.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).save(Role.class, dto);
    }

    @Test
    void updateTest() {
        //SETUP
        Mockito.when(service.update(dto.getId(), dto)).thenReturn(dto);
        //TRY
        ResponseEntity<ResponseDto> result = restResource.update(dto);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(dto, body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).update(dto.getId(), dto);
    }

    @Test
    void deleteByIdTest() {
        //SETUP
        Mockito.doNothing().when(service).deleteById(UUID.fromString(id));
        //TRY
        ResponseEntity<ResponseDto> result = restResource.deleteById(id);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource,
                "role.deleted"), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).deleteById(UUID.fromString(id));
    }
}
