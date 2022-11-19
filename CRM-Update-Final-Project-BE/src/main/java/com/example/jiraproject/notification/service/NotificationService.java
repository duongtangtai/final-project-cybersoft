package com.example.jiraproject.notification.service;

import com.example.jiraproject.common.service.GenericService;
import com.example.jiraproject.notification.dto.NotificationDto;
import com.example.jiraproject.notification.dto.NotificationWithInfoDto;
import com.example.jiraproject.notification.model.Notification;
import com.example.jiraproject.notification.repository.NotificationRepository;
import com.example.jiraproject.security.util.JwtUtil;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.internal.util.CopyOnWriteLinkedHashMap;
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
}
@Service
@RequiredArgsConstructor
class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final ModelMapper mapper;
    private final UserService userService;
    private final JwtUtil jwtUtil;
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
            emitter.send(SseEmitter.event().name("result").data("succeeded"));
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
                    emitter.send(SseEmitter.event().name("newNotification").data(content));
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
                emitter.send(SseEmitter.event().name("newNotification").data(content));
            } catch (IOException e) {
                subscribers.remove(subscriber.getKey());
            }
        }
    }
}
