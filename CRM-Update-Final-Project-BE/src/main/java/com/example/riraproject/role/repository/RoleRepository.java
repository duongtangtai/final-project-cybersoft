package com.example.riraproject.role.repository;

import com.example.riraproject.role.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@Repository
public interface RoleRepository extends JpaRepository<Role, UUID> {
    Optional<Role> findByNameOrCode(String name, String code);

    @Query(value = "select r from Role r where r.id = ?1")
    Optional<Role> findByIdWithInfo(UUID id);

    @Query(value = "select r from Role r order by r.createdAt")
    Set<Role> findAllWithInfo();

    @Query(value = "select r from Role r",
    countQuery = "select count(r) from Role r")
    Page<Role> findAllWithInfoWithPaging(Pageable pageable);

    @Query(value = "select r from Role r left join fetch r.users u where u.username = ?1")
    List<Role> findByUsername(String username);
}
