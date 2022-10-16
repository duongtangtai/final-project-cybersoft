package com.example.jiraproject.project.repository;

import com.example.jiraproject.project.model.Project;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface ProjectRepository extends JpaRepository<Project, UUID> {

    Optional<Project> findByName(String name);

    @Query(value = "select p from Project p left join fetch p.creator left join fetch p.leader where p.id = ?1")
    Optional<Project> findByIdWithInfo(UUID projectId);

    @Query(value = "select p from Project p left join fetch p.creator left join fetch p.leader order by p.createdAt")
    Set<Project> findAllWithCreatorAndLeader();

    @Query(value = "select p from Project p left join fetch p.creator left join fetch p.leader",
    countQuery = "select count(p) from Project p left join p.creator left join p.leader")
    Page<Project> findAllWithUserWithPaging(Pageable pageable);

}
