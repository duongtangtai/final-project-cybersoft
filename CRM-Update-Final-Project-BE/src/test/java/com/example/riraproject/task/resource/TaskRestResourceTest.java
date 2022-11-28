package com.example.riraproject.task.resource;

import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.task.dto.TaskDto;
import com.example.riraproject.task.dto.TaskWithInfoDto;
import com.example.riraproject.task.model.Task;
import com.example.riraproject.task.service.TaskService;
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

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TaskRestResourceTest {
    private final String id = UUID.randomUUID().toString();
    @Autowired private MessageSource messageSource;
    @Mock private TaskService service;
    private TaskRestResource restResource;
    @Mock private TaskDto dto;
    @Mock private TaskWithInfoDto dtoWithInfo;

    @BeforeEach
    void init() {
        restResource = new TaskRestResource(service, messageSource);
    }

    @Test
    void findByIdTest() {
        //SETUP
        Mockito.when(service.findById(TaskDto.class, UUID.fromString(id)))
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
        Mockito.verify(service).findById(TaskDto.class, UUID.fromString(id));
    }


    @Test
    void findAllTest() {
        //SETUP
        Mockito.when(service.findAll(TaskDto.class))
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
        Mockito.verify(service).findAll(TaskDto.class);
    }

    @Test
    void findAllWithPagingTest() {
        //SETUP
        int size = 2;
        int page = 3;
        Mockito.when(service.findAllWithPaging(TaskDto.class, size, page))
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
        Mockito.verify(service).findAllWithPaging(TaskDto.class, size, page);
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
    void findAllByProjectTest() {
        //SETUP
        List<TaskWithInfoDto> list = new ArrayList<>();
        list.add(new TaskWithInfoDto());
        list.add(new TaskWithInfoDto());
        list.add(new TaskWithInfoDto());
        Mockito.when(service.findAllByProject(UUID.fromString(id)))
                .thenReturn(list);
        //TRY
        ResponseEntity<ResponseDto> response = restResource.findAllByProject(id);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(list, body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
    }

    @Test
    void findAllByProjectAndUserTest() {
        //SETUP
        List<TaskWithInfoDto> list = new ArrayList<>();
        list.add(new TaskWithInfoDto());
        list.add(new TaskWithInfoDto());
        list.add(new TaskWithInfoDto());
        String projectId = UUID.randomUUID().toString();
        String userId = UUID.randomUUID().toString();
        Mockito.when(service.findAllByProjectAndUser(UUID.fromString(projectId), UUID.fromString(userId)))
                .thenReturn(list);
        //TRY
        ResponseEntity<ResponseDto> response = restResource.findAllByProjectAndUser(projectId, userId);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(list, body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
    }

    @Test
    void findAllByUserTest() {
        //SETUP
        List<TaskWithInfoDto> list = new ArrayList<>();
        list.add(new TaskWithInfoDto());
        list.add(new TaskWithInfoDto());
        list.add(new TaskWithInfoDto());
        Mockito.when(service.findAllByUser(UUID.fromString(id)))
                .thenReturn(list);
        //TRY
        ResponseEntity<ResponseDto> response = restResource.findAllByUser(id);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(list, body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
    }

    @Test
    void findAllStatusTest() {
        //SETUP
        List<String> list = new ArrayList<>();
        list.add(Task.Status.TODO.toString());
        list.add(Task.Status.IN_PROGRESS.toString());
        list.add(Task.Status.DONE.toString());
        Mockito.when(service.findAllStatus()).thenReturn(list);
        //TRY
        ResponseEntity<ResponseDto> response = restResource.findAllStatus();
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(list, body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
    }

    @Test
    void saveTest() {
        //SETUP
        TaskDto taskDto = Mockito.mock(TaskDto.class);
        Mockito.when(service.save(taskDto)).thenReturn(taskDto);
        //TRY
        ResponseEntity<ResponseDto> response = restResource.save(taskDto);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource, "task.saved"),
                body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).save(taskDto);
    }

    @Test
    void workWithTaskTest() {
        //SETUP
        Mockito.doNothing().when(service).workWithTask(UUID.fromString(id));
        //TRY
        ResponseEntity<ResponseDto> response = restResource.workWithTask(id);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource, "task.in-progress"),
                body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).workWithTask(UUID.fromString(id));
    }

    @Test
    void completeTaskTest() {
        //SETUP
        Mockito.doNothing().when(service).completeTask(UUID.fromString(id));
        //TRY
        ResponseEntity<ResponseDto> response = restResource.completeTask(id);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource, "task.completed"),
                body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).completeTask(UUID.fromString(id));
    }

    @Test
    void updateTask() {
        //SETUP
        TaskDto taskDto = Mockito.mock(TaskDto.class);
        Mockito.when(service.update(taskDto)).thenReturn(taskDto);
        //TRY
        ResponseEntity<ResponseDto> response = restResource.update(taskDto);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource, "task.updated"),
                body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).update(taskDto);
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
        Assertions.assertEquals(MessageUtil.getMessage(messageSource, "task.deleted"),
                body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).deleteById(UUID.fromString(id));
    }
}
