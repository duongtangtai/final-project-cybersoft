package com.example.riraproject.project.resource;

import com.example.riraproject.comment.dto.CommentDto;
import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.project.dto.ProjectDto;
import com.example.riraproject.project.dto.ProjectWithInfoDto;
import com.example.riraproject.project.model.Project;
import com.example.riraproject.project.service.ProjectService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.*;

@ExtendWith(MockitoExtension.class)
class ProjectRestResourceTest {
    @Mock private ProjectService service;
    @Mock private MessageSource messageSource;
    @InjectMocks private ProjectRestResource restResource;
    private final String id = UUID.randomUUID().toString();
    @Mock private Project model;
    @Mock private ProjectDto dto;
    @Mock private ProjectWithInfoDto dtoWithInfo;

    @Test
    void findByIdTest() {
        //SETUP
        Mockito.when(service.findById(ProjectDto.class, UUID.fromString(id)))
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
        Mockito.verify(service).findById(ProjectDto.class, UUID.fromString(id));
    }

    @Test
    void findAllTest() {
        //SETUP
        Mockito.when(service.findAll(ProjectDto.class))
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
        Mockito.verify(service).findAll(ProjectDto.class);
    }

    @Test
    void findAllWithPagingTest() {
        //SETUP
        int size = 2;
        int page = 3;
        Mockito.when(service.findAllWithPaging(ProjectDto.class, size, page))
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
        Mockito.verify(service).findAllWithPaging(ProjectDto.class, size, page);
    }

    @Test
    void findByIdWithInfoTest() {
        //SETUP
        Mockito.when(service.findByIdWithInfo(UUID.fromString(id)))
                .thenReturn(dtoWithInfo);
        ResponseEntity<ResponseDto> result = restResource.findByIdWithInfo(id);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(dtoWithInfo, body.getContent());
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
                .thenReturn(List.of(dtoWithInfo));
        ResponseEntity<ResponseDto> result = restResource.findAllWithInfo();
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dtoWithInfo), body.getContent());
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
                .thenReturn(List.of(dtoWithInfo));
        ResponseEntity<ResponseDto> result = restResource.findAllWithInfoWithPaging(size, page);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dtoWithInfo), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllWithInfoWithPaging(size, page);
    }

    @Test
    void findAllProjectStatusTest() {
        //SETUP
        Mockito.when(service.findAllProjectStatus())
                .thenReturn(List.of("status"));
        ResponseEntity<ResponseDto> result = restResource.findAllProjectStatus();
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of("status"), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllProjectStatus();
    }

    @Test
    void saveTest() {
        //SETUP
        Mockito.when(service.save(dto)).thenReturn(null);
        //TRY
        ResponseEntity<ResponseDto> result = restResource.save(dto);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource,
                "project.saved"), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.CREATED.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).save(dto);
    }

    @Test
    void addUsersTest() {
        //SETUP
        Set<UUID> userIds = new HashSet<>();
        Mockito.when(service.addUsers(UUID.fromString(id), userIds)).thenReturn(dtoWithInfo);
        //TRY
        ResponseEntity<ResponseDto> result = restResource.addUsers(id, userIds);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource,
                "project.add-user.successfully"), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).addUsers(UUID.fromString(id), userIds);
    }

    @Test
    void removeUsersTest() {
        //SETUP
        Set<UUID> userIds = new HashSet<>();
        Mockito.when(service.removeUsers(UUID.fromString(id), userIds)).thenReturn(dtoWithInfo);
        //TRY
        ResponseEntity<ResponseDto> result = restResource.removeUsers(id, userIds);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource,
                "project.remove-user.successfully"), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).removeUsers(UUID.fromString(id), userIds);
    }

    @Test
    void updateTest() {
        //SETUP
        Mockito.when(service.update(dto)).thenReturn(dto);
        //TRY
        ResponseEntity<ResponseDto> result = restResource.update(dto);
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource,
                "project.updated"), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).update(dto);
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
                "project.deleted"), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).deleteById(UUID.fromString(id));
    }
}
