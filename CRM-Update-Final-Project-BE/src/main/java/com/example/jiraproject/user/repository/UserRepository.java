package com.example.jiraproject.user.repository;

import com.example.jiraproject.user.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {

    @Query(value = "select u from User u left join fetch u.roles where u.username = ?1")
    Optional<User> findByUsername(String username);

    Optional<User> findByUsernameOrEmail(String username, String email);

    @Query(value = "select u from User u left join fetch u.roles where u.id = ?1")
    Optional<User> findByIdWithInfo(UUID userId);

    @Query(value = "select u from User u left join fetch u.roles order by u.createdAt")
    Set<User> findAllWithInfo();

    @Query(value = "select u from User u left join fetch u.roles",
    countQuery = "select count(u) from User u left join u.roles")
    Page<User> findAllWithInfoWithPaging(Pageable pageable);
}
