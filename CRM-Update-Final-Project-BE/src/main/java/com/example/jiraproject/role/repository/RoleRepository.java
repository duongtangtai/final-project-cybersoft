package com.example.jiraproject.role.repository;

import com.example.jiraproject.role.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByNameOrCode(String name, String code);

    @Query(value = "select r from Role r left join fetch r.operations where r.id = ?1")
    Optional<Role> findByIdWithInfo(UUID id);

    @Query(value = "select r from Role r left join fetch r.operations order by r.createdAt")
    Set<Role> findAllWithInfo();

    @Query(value = "select r from Role r left join fetch r.operations",
    countQuery = "select count(r) from Role r left join r.operations")
    Page<Role> findAllWithInfoWithPaging(Pageable pageable);
}
