package com.example.riraproject.comment.resource;

import com.example.riraproject.comment.dto.CommentDto;
import com.example.riraproject.comment.dto.CommentWithInfoDto;
import com.example.riraproject.comment.service.CommentService;
import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.MessageUtil;
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

import java.util.List;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CommentRestResourceTest {
    @Mock private CommentService service;
    @Autowired private MessageSource messageSource;
    private CommentRestResource restResource;
    private CommentDto dto;
    private CommentWithInfoDto dtoWithInfo;
    private final String id = UUID.randomUUID().toString();

    @BeforeEach
    void init() {
        restResource = new CommentRestResource(service, messageSource);
        dto = CommentDto.builder()
                .description("this is a dto")
                .build();
        dtoWithInfo = new CommentWithInfoDto();
        dtoWithInfo.setDescription("this is a dtoWithInfo");
    }

    @Test
    void findByIdTest() {
        //SETUP
        Mockito.when(service.findById(CommentDto.class, UUID.fromString(id)))
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
        Mockito.verify(service).findById(CommentDto.class, UUID.fromString(id));
    }
//
    @Test
    void findAllTest() {
        //SETUP
        Mockito.when(service.findAll(CommentDto.class)).thenReturn(List.of(dto));
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
        Mockito.verify(service).findAll(CommentDto.class);
    }

    @Test
    void findAllWithPagingTest() {
        //SETUP
        Mockito.when(service.findAllWithPaging(CommentDto.class, 2 ,3 ))
                .thenReturn(List.of(dto));
        ResponseEntity<ResponseDto> result = restResource.findAllWithPaging(2, 3);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dto), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllWithPaging(CommentDto.class, 2 ,3 );
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
        Mockito.when(service.findAllWithInfoWithPaging(2, 3))
                .thenReturn(List.of(dtoWithInfo));
        ResponseEntity<ResponseDto> result = restResource.findAllWithInfoWithPaging(2, 3);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dtoWithInfo), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllWithInfoWithPaging(2, 3);
    }

    @Test
    void findAllWithInfoByTaskIdTest() {
        //SETUP
        Mockito.when(service.findAllWithInfoByTaskId(UUID.fromString(id)))
                .thenReturn(List.of(dtoWithInfo));
        ResponseEntity<ResponseDto> result = restResource.findAllWithInfoByTaskId(id);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(List.of(dtoWithInfo), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllWithInfoByTaskId(UUID.fromString(id));
    }

    @Test
    void saveTest() {
        //SETUP
        Mockito.when(service.saveComment(dto))
                .thenReturn(dtoWithInfo);
        Assertions.assertDoesNotThrow(() -> restResource.save(dto));
        ResponseEntity<ResponseDto> result = restResource.save(dto);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource,
                "comment.saved"), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.CREATED.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service, Mockito.times(2)).saveComment(dto);
    }

    @Test
    void updateTest() {
        //SETUP
        Mockito.when(service.update(dto.getId(), dto))
                .thenReturn(dto);
        ResponseEntity<ResponseDto> result = restResource.update(dto);
        //TRY
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
        ResponseEntity<ResponseDto> result = restResource.deleteById(id);
        //TRY
        Assertions.assertNotNull(result);
        Assertions.assertNotNull(result.getBody());
        ResponseDto body = result.getBody();
        Assertions.assertEquals(MessageUtil
                .getMessage(messageSource, "comment.deleted"), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).deleteById(UUID.fromString(id));
    }
}
