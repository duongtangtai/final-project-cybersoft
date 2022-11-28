package com.example.riraproject.user.resource;

import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.notification.service.NotificationService;
import com.example.riraproject.user.dto.UserDto;
import com.example.riraproject.user.dto.UserWithProjectInfoDto;
import com.example.riraproject.user.dto.UserWithTaskInfoDto;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserRestResourceTest {
    @Mock private UserService service;
    @Mock private NotificationService notificationService;
    @Autowired private MessageSource messageSource;
    private UserRestResource restResource;
    private final String id = UUID.randomUUID().toString();
    private UserDto dto;
    private UserWithProjectInfoDto dtoWithProject;
    private UserWithTaskInfoDto dtoWithTask;

    @BeforeEach
    void init() {
        restResource = new UserRestResource(service, notificationService, messageSource);
        dto = UserDto.builder()
                .username("dto")
                .build();
        dtoWithProject = new UserWithProjectInfoDto();
        dtoWithProject.setUsername("dtoWithInfo");
        dtoWithTask = new UserWithTaskInfoDto();
        dtoWithTask.setUsername("dtoWithInfo");
    }

    @Test
    void findByIdTest() {
        //SETUP
        Mockito.when(service.findById(UserDto.class, UUID.fromString(id)))
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
        Mockito.verify(service).findById(UserDto.class, UUID.fromString(id));
    }

    @Test
    void findAllTest() {
        //SETUP
        Mockito.when(service.findAll(UserDto.class))
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
        Mockito.verify(service).findAll(UserDto.class);
    }

    @Test
    void findAllWithPagingTest() {
        //SETUP
        int size = 2;
        int page = 3;
        Mockito.when(service.findAllWithPaging(UserDto.class, size, page))
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
        Mockito.verify(service).findAllWithPaging(UserDto.class, size, page);
    }

    @Test
    void findByIdWithInfoTest() {
        //SETUP
        Mockito.when(service.findByIdWithInfo(UUID.fromString(id)))
                .thenReturn(dtoWithProject);
        ResponseEntity<ResponseDto> result = restResource.findByIdWithInfo(id);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(dtoWithProject, body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findByIdWithInfo(UUID.fromString(id));
    }

    @Test
    void findAllWithInfoTest() {
        //SETUP
        Mockito.when(service.findAllWithInfo())
                .thenReturn(List.of(dtoWithProject));
        ResponseEntity<ResponseDto> result = restResource.findAllWithInfo();
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dtoWithProject), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllWithInfo();
    }

    @Test
    void findAllWithInfoWithPagingTest() {
        //SETUP
        int size = 2;
        int page = 3;
        Mockito.when(service.findAllWithInfoWithPaging(size, page))
                .thenReturn(List.of(dtoWithProject));
        ResponseEntity<ResponseDto> result = restResource.findAllWithInfoWithPaging(size, page);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dtoWithProject), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllWithInfoWithPaging(size, page);
    }

    @Test
    void findAllAccountStatusTest() {
        List<String> statusList = new ArrayList<>();
        statusList.add(User.AccountStatus.ACTIVE.toString());
        statusList.add(User.AccountStatus.TEMPORARILY_BLOCKED.toString());
        statusList.add(User.AccountStatus.PERMANENTLY_BLOCKED.toString());
        Mockito.when(service.findAllAccountStatus())
                .thenReturn(statusList);
        ResponseEntity<ResponseDto> result = restResource.findAllAccountStatus();
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(statusList, body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllAccountStatus();
    }

    @Test
    void findAllGenderTest() {
        List<String> genderList = new ArrayList<>();
        genderList.add(User.Gender.MALE.toString());
        genderList.add(User.Gender.FEMALE.toString());
        Mockito.when(service.findAllGenders())
                .thenReturn(genderList);
        ResponseEntity<ResponseDto> result = restResource.findAllGenders();
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(genderList, body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllGenders();
    }

    @Test
    void findAllInsideProjectTest() {
        Mockito.when(service.findAllInsideProject(UUID.fromString(id)))
                .thenReturn(List.of(dtoWithProject));
        ResponseEntity<ResponseDto> result = restResource.findAllInsideProject(id);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dtoWithProject), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllInsideProject(UUID.fromString(id));
    }

    @Test
    void findAllInsideProjectWithTaskTest() {
        Mockito.when(service.findAllInsideProjectWithTask(UUID.fromString(id)))
                .thenReturn(List.of(dtoWithTask));
        ResponseEntity<ResponseDto> result = restResource.findAllInsideProjectWithTask(id);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dtoWithTask), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllInsideProjectWithTask(UUID.fromString(id));
    }

    @Test
    void findAllOutsideProjectTest() {
        Mockito.when(service.findAllOutsideProject(UUID.fromString(id)))
                .thenReturn(List.of(dtoWithProject));
        ResponseEntity<ResponseDto> result = restResource.findAllOutsideProject(id);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dtoWithProject), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllOutsideProject(UUID.fromString(id));
    }

    @Test
    void findAllLeaderRoleTest() {
        Mockito.when(service.findAllLeaderRole())
                .thenReturn(List.of(dto));
        ResponseEntity<ResponseDto> result = restResource.findAllLeaderRole();
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dto), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllLeaderRole();
    }

    @Test
    void saveTest() {
        //SETUP
        Mockito.when(service.save(dto)).thenReturn(dto);
        //TRY
        ResponseEntity<ResponseDto> response = restResource.save(dto);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource, "user.saved"),
                body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).save(dto);
    }

    @Test
    void addRolesTest() {
        //SETUP
        Set<UUID> roleIds = new HashSet<>();
        roleIds.add(UUID.randomUUID());
        roleIds.add(UUID.randomUUID());
        roleIds.add(UUID.randomUUID());
        Mockito.when(service.updateRoles(UUID.fromString(id), roleIds))
                .thenReturn(dtoWithProject);
        //TRY
        ResponseEntity<ResponseDto> response = restResource.addRoles(id, roleIds);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource, "user.role.updated"),
                body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).updateRoles(UUID.fromString(id), roleIds);
    }

    @Test
    void updateTest() {
        //SETUP
        Mockito.when(service.update(dto)).thenReturn(dto);
        Mockito.doNothing().when(notificationService).checkUserStatus(dto);
        //TRY
        ResponseEntity<ResponseDto> response = restResource.update(dto);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource, "user.updated"),
                body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).update(dto);
        Mockito.verify(notificationService).checkUserStatus(dto);
    }

    @Test
    void deleteByIdTest() {
        //SETUP
        Mockito.doNothing().when(service).deleteById(UUID.fromString(id));
        //TRY
        ResponseEntity<ResponseDto> response = restResource.deleteById(id);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource, "user.deleted"),
                body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).deleteById(UUID.fromString(id));
    }
}
