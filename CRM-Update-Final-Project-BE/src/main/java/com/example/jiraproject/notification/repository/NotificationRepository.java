package com.example.jiraproject.notification.repository;

import com.example.jiraproject.notification.model.Notification;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface NotificationRepository extends JpaRepository<Notification, UUID> {

    @Query(value = "select n from Notification n left join fetch n.sender left join fetch n.receiver where n.id = ?1")
    Optional<Notification> findByIdWithInfo(UUID id);

    @Query(value = "select n from Notification n left join fetch n.sender left join fetch n.receiver order by n.createdAt")
    Set<Notification> findAllWithInfo();

    @Query(value = "select n from Notification n left join fetch n.sender left join fetch n.receiver",
    countQuery = "select count(n) from Notification n left join n.sender left join n.receiver")
    Page<Notification> findAllWithInfoWithPaging(Pageable pageable);
}
