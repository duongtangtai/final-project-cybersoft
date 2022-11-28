package com.example.riraproject.notification.resource;

import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.notification.dto.NotificationWithInfoDto;
import com.example.riraproject.notification.model.Notification;
import com.example.riraproject.notification.service.NotificationService;
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
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class NotificationRestResourceTest {
    @Mock private NotificationService service;
    @Autowired private MessageSource messageSource;
    private NotificationRestResource restResource;
    private final String id = UUID.randomUUID().toString();
    private NotificationWithInfoDto dtoWithInfo;

    @BeforeEach
    void init() {
        restResource = new NotificationRestResource(service, messageSource);
        dtoWithInfo = new NotificationWithInfoDto();
        dtoWithInfo.setDescription("this is a new notification");
    }

    @Test
    void findAllSentByReceiverIdTest() {
        //SETUP
        Mockito.when(service.findAllWithReceiverAndStatus(UUID.fromString(id),
                Notification.Status.SENT))
                .thenReturn(List.of(dtoWithInfo));
        //TRY
        ResponseEntity<ResponseDto> response =  restResource.findAllSentByReceiverId(id);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(List.of(dtoWithInfo), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllWithReceiverAndStatus(UUID.fromString(id),
                Notification.Status.SENT);
    }

    @Test
    void findAllReadByReceiverTest() {
        //SETUP
        Mockito.when(service.findAllWithReceiverAndStatus(UUID.fromString(id),
                Notification.Status.READ))
                .thenReturn(List.of(dtoWithInfo));
        //TRY
        ResponseEntity<ResponseDto> response =  restResource.findAllReadByReceiver(id);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(List.of(dtoWithInfo), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).findAllWithReceiverAndStatus(UUID.fromString(id),
                Notification.Status.READ);
    }

    @Test
    void subscribeTest() {
        //SETUP
        String token = "token";
        SseEmitter connection = Mockito.mock(SseEmitter.class);
        Mockito.when(service.subscribe(token))
                .thenReturn(connection);
        //TRY
        SseEmitter result = restResource.subscribe(token);
        Assertions.assertEquals(connection, result);
        Mockito.verify(service).subscribe(token);
    }

    @Test
    void unsubscribeTest() {
        //SETUP
        String username = "username";
        Mockito.doNothing().when(service).unsubscribe(username);
        //TRY
        ResponseEntity<ResponseDto> response = restResource.unsubscribe(username);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource,
                "notification.unsubscribed"), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).unsubscribe(username);
    }

    @Test
    void readAllByReceiverTest() {
        //SETUP
        Mockito.doNothing().when(service).readAllByReceiver(UUID.fromString(id));
        //TRY
        ResponseEntity<ResponseDto> response = restResource.readAllByReceiver(id);
        Assertions.assertNotNull(response);
        Assertions.assertNotNull(response.getBody());
        ResponseDto body = response.getBody();
        Assertions.assertEquals(MessageUtil.getMessage(messageSource,
                "notification.read"), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).readAllByReceiver(UUID.fromString(id));
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
        Assertions.assertEquals(MessageUtil.getMessage(messageSource,
                "notification.deleted"), body.getContent());
        Assertions.assertFalse(body.isHasErrors());
        Assertions.assertNull(body.getErrors());
        Assertions.assertEquals(HttpStatus.OK.value(), body.getStatusCode());
        Assertions.assertNotNull(body.getTimeStamp());
        Mockito.verify(service).deleteById(UUID.fromString(id));
    }
}
