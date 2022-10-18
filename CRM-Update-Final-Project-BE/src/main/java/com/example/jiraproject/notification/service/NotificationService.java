package com.example.jiraproject.notification.service;

import com.example.jiraproject.common.service.GenericService;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.notification.dto.NotificationDto;
import com.example.jiraproject.notification.dto.NotificationWithInfoDto;
import com.example.jiraproject.notification.model.Notification;
import com.example.jiraproject.notification.repository.NotificationRepository;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.UUID;

public interface NotificationService extends GenericService<Notification, NotificationDto, UUID> {
    NotificationWithInfoDto findByIdWithInfo(UUID id);
    List<NotificationWithInfoDto> findAllWithInfo();
    List<NotificationWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex);
    NotificationWithInfoDto saveNotification(NotificationDto dto, UUID fromId, UUID toId);
    NotificationWithInfoDto updateNotification(UUID id);
}
@Service
@Transactional
@RequiredArgsConstructor
class NotificationServiceImpl implements NotificationService {
    private final NotificationRepository repository;
    private final ModelMapper mapper;
    private final UserService userService;
    private final MessageSource messageSource;
    private static final String UUID_NOT_FOUND = "notification.id.not-found";

    @Override
    public JpaRepository<Notification, UUID> getRepository() {
        return this.repository;
    }

    @Override
    public ModelMapper getMapper() {
        return this.mapper;
    }

    @Override
    public NotificationWithInfoDto findByIdWithInfo(UUID id) {
        Notification notification = repository.findByIdWithInfo(id).orElseThrow(() ->
                new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        return mapper.map(notification, NotificationWithInfoDto.class);
    }

    @Override
    public List<NotificationWithInfoDto> findAllWithInfo() {
        return repository.findAllWithInfo().stream()
                .map(model -> mapper.map(model, NotificationWithInfoDto.class))
                .toList();
    }

    @Override
    public List<NotificationWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex) {
        return repository.findAllWithInfoWithPaging(PageRequest.of(pageIndex, size, Sort.by("createdAt")))
                .stream()
                .map(model -> mapper.map(model, NotificationWithInfoDto.class))
                .toList();
    }

    @Override
    public NotificationWithInfoDto saveNotification(NotificationDto dto, UUID fromId, UUID toId) {
        Notification notification = mapper.map(dto, Notification.class);
        User sender = userService.findUserById(fromId);
        User receiver = userService.findUserById(toId);
        notification.setSender(sender);
        notification.setReceiver(receiver);
        repository.save(notification);
        return mapper.map(notification, NotificationWithInfoDto.class);
    }

    @Override
    public NotificationWithInfoDto updateNotification(UUID id) {
        Notification notification = repository.findById(id)
                .orElseThrow(() -> new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        notification.setStatus(Notification.Status.READ); //UPDATE STATUS FROM SENT TO READ
        return mapper.map(notification, NotificationWithInfoDto.class);
    }
}
