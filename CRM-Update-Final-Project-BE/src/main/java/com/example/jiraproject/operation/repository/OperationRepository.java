package com.example.jiraproject.operation.repository;

import com.example.jiraproject.operation.model.Operation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface OperationRepository extends JpaRepository<Operation, UUID> {
    Optional<Operation> findByNameAndType(String name, Operation.Type type);

    @Query(value = "select o from Operation o inner join o.roles r inner join r.users u where o.name=?1 and o.type=?2 and u.username=?3")
    Optional<Operation> findByNameAndTypeAndUsername(String operationName, Operation.Type type, String username);
}
