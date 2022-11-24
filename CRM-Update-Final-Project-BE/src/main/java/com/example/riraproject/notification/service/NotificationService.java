package com.example.riraproject.notification.service;

import com.example.riraproject.common.service.GenericService;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.notification.dto.NotificationDto;
import com.example.riraproject.notification.dto.NotificationWithInfoDto;
import com.example.riraproject.notification.model.Notification;
import com.example.riraproject.notification.repository.NotificationRepository;
import com.example.riraproject.notification.util.NotificationUtil;
import com.example.riraproject.security.util.JwtUtil;
import com.example.riraproject.user.dto.UserDto;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.CopyOnWriteLinkedHashMap;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;

public interface NotificationService extends GenericService<Notification, NotificationDto, UUID> {
    List<NotificationWithInfoDto> findAllWithReceiverAndStatus(UUID userId, Notification.Status status);
    void saveNotification(String content, User sender, User receiver);
    void readAllByReceiver(UUID userId);
    SseEmitter subscribe(String token);
    void sendNotificationToUser(String receiverUsername, String content);
    void sendNotificationToAll(String content);
    void unsubscribe(String username);
    boolean isSubscriber(String username);
    void sendLogoutEvent(String username, String content);
    void checkUserStatus(UserDto userDto);
}
@Service
@RequiredArgsConstructor
class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final ModelMapper mapper;
    private final UserService userService;
    private final JwtUtil jwtUtil;
    private final MessageSource messageSource;
    private final Map<String, SseEmitter> subscribers = new CopyOnWriteLinkedHashMap<>();

    @Override
    public JpaRepository<Notification, UUID> getRepository() {
        return this.repository;
    }

    @Override
    public ModelMapper getMapper() {
        return this.mapper;
    }


    @Transactional(readOnly = true)
    @Override
    public List<NotificationWithInfoDto> findAllWithReceiverAndStatus(UUID userId, Notification.Status status) {
        User user = userService.findUserById(userId);
        return repository.findAllWithReceiverAndStatus(user.getId(), status)
                .stream()
                .map(model -> mapper.map(model, NotificationWithInfoDto.class))
                .toList();
    }

    @Transactional
    @Override
    public void readAllByReceiver(UUID userId) {
        User user = userService.findUserById(userId);
        repository.setStatusByReceiver(user.getId(), Notification.Status.READ);
    }

    @Transactional
    @Override
    public void saveNotification(String content, User sender, User receiver) {
        Notification notification = Notification.builder()
                .description(content)
                .sender(sender)
                .receiver(receiver)
                .build();
        repository.save(notification);
    }

    @Override
    public SseEmitter subscribe(String token) {
        String username;
        try {
            username = (String) jwtUtil.verifyToken(token).getPrincipal();
        } catch (Exception e) {
            System.out.println("ERROR WHILE SUBSCRIBING: " + e.getMessage());
            return null;
        }
        SseEmitter emitter = new SseEmitter(Long.MAX_VALUE); //default 30 seconds
        try {
            emitter.send(SseEmitter.event().name(NotificationUtil.SUBSCRIPTION_EVENT).data("succeeded"));
        } catch (IOException e) {
            e.printStackTrace();
        }
        emitter.onCompletion(() -> subscribers.remove(username));
        subscribers.put(username, emitter);
        return emitter;
    }

    @Override
    public void sendNotificationToUser(String receiverUsername, String content) {
        for (Map.Entry<String, SseEmitter> subscriber: subscribers.entrySet()) {
            try {
                if (subscriber.getKey().equals(receiverUsername)) {
                    SseEmitter emitter = subscriber.getValue();
                    emitter.send(SseEmitter.event().name(NotificationUtil.NOTIFICATION_EVENT).data(content));
                    break; //finish
                }
            } catch (IOException e) {
                subscribers.remove(subscriber.getKey());
            }
        }
    }

    @Override
    public void sendNotificationToAll(String content) {
        for (Map.Entry<String, SseEmitter> subscriber: subscribers.entrySet()) {
            try {
                SseEmitter emitter = subscriber.getValue();
                emitter.send(SseEmitter.event().name(NotificationUtil.NOTIFICATION_EVENT).data(content));
            } catch (IOException e) {
                subscribers.remove(subscriber.getKey());
            }
        }
    }

    @Override
    public void unsubscribe(String subscriberUsername) {
        for (String username: subscribers.keySet()) {
           if (username.equals(subscriberUsername)) {
               subscribers.remove(username);
           }
        }
    }

    @Override
    public boolean isSubscriber(String username) {
        return subscribers.containsKey(username);
    }

    @Override
    public void sendLogoutEvent(String username, String content) {
        for (Map.Entry<String, SseEmitter> subscriber: subscribers.entrySet()) {
            try {
                if (subscriber.getKey().equals(username)) {
                    SseEmitter emitter = subscriber.getValue();
                    emitter.send(SseEmitter.event().name(NotificationUtil.LOGOUT_EVENT).data(
                            content
                    ));
                    subscribers.remove(subscriber.getKey()); //remove when user log out
                    break; //finish
                }
            } catch (IOException e) {
                subscribers.remove(subscriber.getKey());
            }
        }
    }

    @Override
    public void checkUserStatus(UserDto userDto) {
        if (isSubscriber(userDto.getUsername()) &&
                userDto.getAccountStatus().equals(User.AccountStatus.TEMPORARILY_BLOCKED) || 
                userDto.getAccountStatus().equals(User.AccountStatus.PERMANENTLY_BLOCKED)) {
            sendLogoutEvent(userDto.getUsername(),
                    MessageUtil.getMessage(messageSource,
                            "account.blocked"));
        }
    }
}
