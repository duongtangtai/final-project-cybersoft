package com.example.jiraproject.notification.repository;

import com.example.jiraproject.notification.dto.NotificationWithInfoDto;
import com.example.jiraproject.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query(value = "select n from Notification n left join fetch  n.sender left join fetch n.receiver " +
            "where n.receiver.id = ?1 and n.status = ?2 " +
            "order by n.createdAt desc ")
    Set<Notification> findAllWithReceiverAndStatus(UUID receiverId, Notification.Status status);

    @Modifying
    @Query(value = "update Notification n set n.status = ?2 where n.receiver.id = ?1 ")
    void setStatusByReceiver(UUID id, Notification.Status status);
}
