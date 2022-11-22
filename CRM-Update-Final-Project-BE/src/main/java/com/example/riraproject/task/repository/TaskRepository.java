package com.example.riraproject.task.repository;

import com.example.riraproject.task.model.Task;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface TaskRepository extends JpaRepository<Task, UUID> {

    Optional<Task> findByNameAndProjectName(String name, String projectName);

    @Query(value = "select t from Task t left join fetch t.project left join fetch t.reporter where t.id = ?1")
    Optional<Task> findByIdWithInfo(UUID taskId);

    @Query(value = "select t from Task t left join fetch t.project left join fetch t.reporter order by t.createdAt")
    Set<Task> findAllWithInfo();

    @Query(value = "select t from Task t left join fetch t.project left join fetch t.reporter",
    countQuery = "select count(t) from Task t left join t.project left join t.reporter")
    Page<Task> findAllWithInfoWithPaging(Pageable pageable);

    @Query(value = "select t from Task t left join fetch t.project p left join fetch t.reporter " +
            "where p.id = ?1")
    Set<Task> findAllByProject(UUID projectId);

    @Query(value = "select t from Task t left join fetch t.project p left join fetch t.reporter r " +
            "where p.id = ?1 and r.id = ?2")
    Set<Task> findAllByProjectAndUser(UUID projectId, UUID userId);

    @Query(value = "select t from Task t left join fetch t.project left join fetch t.reporter r " +
            "where r.id = ?1")
    Set<Task> findAllByUser(UUID userId);
}
