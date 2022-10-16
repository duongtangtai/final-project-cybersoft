package com.example.jiraproject.task.repository;

import com.example.jiraproject.task.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {
    Optional<Task> findByName(String name);

    @Query(value = "select t from Task t left join fetch t.project left join fetch t.reporter where t.id = ?1")
    Optional<Task> findByIdWithInfo(UUID taskId);

    @Query(value = "select t from Task t left join fetch t.project left join fetch t.reporter order by t.createdAt")
    Set<Task> findAllWithInfo();

    @Query(value = "select t from Task t left join fetch t.project left join fetch t.reporter",
    countQuery = "select count(t) from Task t left join t.project left join t.reporter")
    Page<Task> findAllWithInfoWithPaging(Pageable pageable);
}
