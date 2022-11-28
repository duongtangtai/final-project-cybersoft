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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class NotificationServiceTest {
    @Mock private NotificationRepository repository;
    @Autowired private ModelMapper mapper;
    @Mock private UserService userService;
    @Mock private JwtUtil jwtUtil;
    @Autowired private MessageSource messageSource;
    private NotificationServiceImpl service;

    @BeforeEach
    void init() {
        service = new NotificationServiceImpl(repository, mapper, userService, jwtUtil, messageSource);
    }


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
        Notification sentModel = Notification.builder()
                .description("this is a sent notification")
                .build();
        Notification readModel = Notification.builder()
                .description("this is a read notification")
                .build();

        Mockito.when(userService.findUserById(userId)).thenReturn(user);
        Mockito.when(repository.findAllWithReceiverAndStatus(user.getId(), sentStatus))
                .thenReturn(Set.of(sentModel));
        Mockito.when(repository.findAllWithReceiverAndStatus(user.getId(), readStatus))
                .thenReturn(Set.of(readModel));
        //TRY
        //CASE 1 : SENT STATUS
        List<NotificationWithInfoDto> result = service.findAllWithReceiverAndStatus(userId, sentStatus);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(sentModel.getDescription(), result.get(0).getDescription());
        Mockito.verify(userService).findUserById(userId);
        Mockito.verify(repository).findAllWithReceiverAndStatus(user.getId(), sentStatus);
        //CASE 2 : READ STATUS
        List<NotificationWithInfoDto> result2 = service.findAllWithReceiverAndStatus(userId, readStatus);
        Assertions.assertEquals(1, result2.size());
        Assertions.assertEquals(readModel.getDescription(), result2.get(0).getDescription());
        Mockito.verify(userService, Mockito.times(2)).findUserById(userId);
        Mockito.verify(repository).findAllWithReceiverAndStatus(user.getId(), readStatus);
    }

    @Test
    void readAllByReceiverTest() {
        //SETUP
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
