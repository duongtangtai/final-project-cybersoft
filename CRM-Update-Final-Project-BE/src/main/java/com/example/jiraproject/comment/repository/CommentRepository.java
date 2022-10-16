package com.example.jiraproject.comment.repository;

import com.example.jiraproject.comment.model.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface CommentRepository extends JpaRepository<Comment, UUID> {

    @Query(value = "select c from Comment c left join fetch c.writer left join fetch c.task left join fetch c.cmt where c.id = ?1")
    Optional<Comment> findByIdWithInfo(UUID id);

    @Query(value = "select c from Comment c left join fetch c.writer left join fetch c.task left join fetch c.cmt order by c.createdAt")
    Set<Comment> findAllWithInfo();

    @Query(value = "select c from Comment c left join fetch c.writer left join fetch c.task left join fetch c.cmt",
    countQuery = "select count(c) from Comment c left join c.writer left join c.task left join c.cmt")
    Page<Comment> findAllWithInfoWithPaging(Pageable pageable);
}
