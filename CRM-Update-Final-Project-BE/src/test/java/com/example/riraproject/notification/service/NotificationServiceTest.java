package com.example.riraproject.notification.service;

import com.auth0.jwt.exceptions.JWTVerificationException;
import com.example.riraproject.notification.dto.NotificationDto;
import com.example.riraproject.notification.dto.NotificationWithInfoDto;
import com.example.riraproject.notification.model.Notification;
import com.example.riraproject.notification.repository.NotificationRepository;
import com.example.riraproject.security.util.JwtUtil;
import com.example.riraproject.user.dto.UserDto;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock private NotificationRepository repository;
    @Mock private ModelMapper mapper;
    @Mock private UserService userService;
    @Mock private JwtUtil jwtUtil;
    @Mock private MessageSource messageSource;
    @Mock private Notification model;
    @Mock private NotificationDto dto;
    @Mock private NotificationWithInfoDto dtoWithInfo;

    @InjectMocks private NotificationServiceImpl service;


    @Test
    void getRepositoryAndMapperTest() {
        Assertions.assertEquals(repository, service.getRepository());
        Assertions.assertEquals(mapper, service.getMapper());
    }

    @Test
    void findAllWithReceiverAndStatusTest() {
        //SETUP
        UUID userId = UUID.randomUUID();
        User user = Mockito.mock(User.class);
        Notification.Status sentStatus = Notification.Status.SENT;
        Notification.Status readStatus = Notification.Status.READ;
        Notification sentModel = Mockito.mock(Notification.class);
        Notification readModel = Mockito.mock(Notification.class);
        NotificationWithInfoDto sentDto = Mockito.mock(NotificationWithInfoDto.class);
        NotificationWithInfoDto readDto = Mockito.mock(NotificationWithInfoDto.class);

        Mockito.when(userService.findUserById(userId)).thenReturn(user);
        Mockito.when(repository.findAllWithReceiverAndStatus(user.getId(), sentStatus))
                .thenReturn(Set.of(sentModel));
        Mockito.when(repository.findAllWithReceiverAndStatus(user.getId(), readStatus))
                .thenReturn(Set.of(readModel));
        Mockito.when(mapper.map(sentModel, NotificationWithInfoDto.class))
                .thenReturn(sentDto);
        Mockito.when(mapper.map(readModel, NotificationWithInfoDto.class))
                .thenReturn(readDto);
        //TRY
        //CASE 1 : SENT STATUS
        Assertions.assertEquals(List.of(sentDto),
                service.findAllWithReceiverAndStatus(userId, sentStatus));
        Mockito.verify(userService).findUserById(userId);
        Mockito.verify(repository).findAllWithReceiverAndStatus(user.getId(), sentStatus);
        //CASE 2 : READ STATUS
        Assertions.assertEquals(List.of(readDto),
                service.findAllWithReceiverAndStatus(userId, readStatus));
        Mockito.verify(userService, Mockito.times(2)).findUserById(userId);
        Mockito.verify(repository, Mockito.times(1)).findAllWithReceiverAndStatus(user.getId(), sentStatus);
    }

    @Test
    void readAllByReceiverTest() {
        //MOCKING
        UUID userId = UUID.randomUUID();
        User user = new User();
        Mockito.when(userService.findUserById(userId)).thenReturn(user);
        Mockito.doNothing().when(repository).setStatusByReceiver(user.getId(), Notification.Status.READ);
        //TRY
        Assertions.assertDoesNotThrow(() -> service.readAllByReceiver(userId));
        Mockito.verify(userService).findUserById(userId);
        Mockito.verify(repository).setStatusByReceiver(user.getId(), Notification.Status.READ);
    }

    @Test
    void saveNotificationTest() {
        //SETUP
        String content = "description";
        User sender = Mockito.spy(User.builder()
                .username("sender")
                .build());
        User receiver = Mockito.spy(User.builder()
                .username("receiver")
                .build());
        ArgumentCaptor<Notification> captor = ArgumentCaptor.forClass(Notification.class);
        //TRY
        Mockito.when(repository.save(captor.capture())).thenReturn(null);
        Assertions.assertDoesNotThrow(() -> service.saveNotification(content, sender, receiver));
        Notification savedModel = captor.getValue();
        Assertions.assertNotNull(savedModel);
        Assertions.assertEquals(content, savedModel.getDescription());
        Assertions.assertEquals(sender.getUsername(), savedModel.getSender().getUsername());
        Assertions.assertEquals(receiver.getUsername(), savedModel.getReceiver().getUsername());
    }

    @Test
    void subscribeTest() {
        //SETUP
        String username = "username";
        String token = "token";
        UsernamePasswordAuthenticationToken authenticationToken = Mockito.spy(
                new UsernamePasswordAuthenticationToken(username,
                        null, Collections.emptyList()));
        Mockito.when(jwtUtil.verifyToken(token)).thenReturn(authenticationToken);
        //CASE 1: TOKEN IS VALID
        //check connection
        SseEmitter emitter = service.subscribe(token);
        Assertions.assertNotNull(emitter);
        Assertions.assertNotNull(emitter.getTimeout());
        Assertions.assertTrue(service.isSubscriber(username));
        //CASE 2: TOKEN IS INVALID
        Mockito.when(jwtUtil.verifyToken(token)).thenThrow(JWTVerificationException.class);
        Assertions.assertNull(service.subscribe(token));
    }

    @Test
    void sendNotificationToUserTest() throws IOException {
        //SETUP
        String username = "username";
        String content = "content";
        //subscribe first
        String token = "token";
        UsernamePasswordAuthenticationToken authenticationToken = Mockito.spy(
                new UsernamePasswordAuthenticationToken(username,
                        null, Collections.emptyList()));
        Mockito.when(jwtUtil.verifyToken(token)).thenReturn(authenticationToken);
        SseEmitter emitter = service.subscribe(token);
        //check connection
        Assertions.assertNotNull(emitter);
        Assertions.assertNotNull(emitter.getTimeout());
        Assertions.assertTrue(service.isSubscriber(username));
        //TRY
        Assertions.assertDoesNotThrow(() -> service.sendNotificationToUser(username, content));
        //TODO how to check the event?
    }

    @Test
    void sendNotificationToAllTest() {
        //SETUP
        String username = "username";
        String content = "content";
        //subscribe first
        String token = "token";
        UsernamePasswordAuthenticationToken authenticationToken = Mockito.spy(
                new UsernamePasswordAuthenticationToken(username,
                        null, Collections.emptyList()));
        Mockito.when(jwtUtil.verifyToken(token)).thenReturn(authenticationToken);
        SseEmitter emitter = service.subscribe(token);
        //check connection
        Assertions.assertNotNull(emitter);
        Assertions.assertNotNull(emitter.getTimeout());
        Assertions.assertTrue(service.isSubscriber(username));
        //TRY
        Assertions.assertDoesNotThrow(() -> service.sendNotificationToAll(content));
    }

    @Test
    void unsubscribeTest() {
        //SETUP
        String username = "username";
        //subscribe first
        String token = "token";
        UsernamePasswordAuthenticationToken authenticationToken = Mockito.spy(
                new UsernamePasswordAuthenticationToken(username,
                        null, Collections.emptyList()));
        Mockito.when(jwtUtil.verifyToken(token)).thenReturn(authenticationToken);
        SseEmitter emitter = service.subscribe(token);
        //check connection
        Assertions.assertNotNull(emitter);
        Assertions.assertNotNull(emitter.getTimeout());
        Assertions.assertTrue(service.isSubscriber(username));
        //TRY
        Assertions.assertDoesNotThrow(() -> service.unsubscribe(username));
        Assertions.assertFalse(service.isSubscriber(username));
    }

    @Test
    void sendLogoutEventTest() {
        //SETUP
        String username = "username";
        String content = "content";
        //subscribe first
        String token = "token";
        UsernamePasswordAuthenticationToken authenticationToken = Mockito.spy(
                new UsernamePasswordAuthenticationToken(username,
                        null, Collections.emptyList()));
        Mockito.when(jwtUtil.verifyToken(token)).thenReturn(authenticationToken);
        SseEmitter emitter = service.subscribe(token);
        //check connection
        Assertions.assertNotNull(emitter);
        Assertions.assertNotNull(emitter.getTimeout());
        Assertions.assertTrue(service.isSubscriber(username));
        //TRY
        Assertions.assertDoesNotThrow(() -> service.sendLogoutEvent(username, content));
        Assertions.assertFalse(service.isSubscriber(username));
    }

    @Test
    void checkUserStatusTest() {
        //SETUP
        String username = "username";
        String content = "content";
        //subscribe first
        String token = "token";
        UsernamePasswordAuthenticationToken authenticationToken = Mockito.spy(
                new UsernamePasswordAuthenticationToken(username,
                        null, Collections.emptyList()));
        Mockito.when(jwtUtil.verifyToken(token)).thenReturn(authenticationToken);
        SseEmitter emitter = service.subscribe(token);
        //check connection
        Assertions.assertNotNull(emitter);
        Assertions.assertNotNull(emitter.getTimeout());
        Assertions.assertTrue(service.isSubscriber(username));
        //CASE 1: user is not a subscriber
        UserDto user1 = UserDto.builder()
                .username("stranger")
                .accountStatus(User.AccountStatus.ACTIVE)
                .build();
        Assertions.assertDoesNotThrow(() -> service.checkUserStatus(user1));
        Assertions.assertTrue(service.isSubscriber(username));
        //CASE 2: user is a subscriber and status is active
        UserDto user2 = UserDto.builder()
                .username(username)
                .accountStatus(User.AccountStatus.ACTIVE)
                .build();
        Assertions.assertDoesNotThrow(() -> service.checkUserStatus(user2));
        Assertions.assertTrue(service.isSubscriber(username));
        //CASE 3: user is a subscriber and status is blocked
        UserDto user3 = UserDto.builder()
                .username(username)
                .accountStatus(User.AccountStatus.TEMPORARILY_BLOCKED)
                .build();
        Assertions.assertDoesNotThrow(() -> service.checkUserStatus(user3));
        Assertions.assertFalse(service.isSubscriber(username));
    }
}
