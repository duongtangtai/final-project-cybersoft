package com.example.jiraproject.operation.service;

import com.example.jiraproject.common.service.GenericService;
import com.example.jiraproject.operation.dto.OperationDto;
import com.example.jiraproject.operation.model.Operation;
import com.example.jiraproject.operation.repository.OperationRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface OperationService extends GenericService<Operation, OperationDto, UUID> {
    List<Operation> findAllByIds(Set<UUID> ids);
}
@Service
@Transactional
@RequiredArgsConstructor
class OperationServiceImpl implements OperationService {
    private final OperationRepository repository;
    private final ModelMapper mapper;

    @Override
    public JpaRepository<Operation, UUID> getRepository() {
        return this.repository;
    }

    @Override
    public ModelMapper getMapper() {
        return this.mapper;
    }

    @Override
    public List<Operation> findAllByIds(Set<UUID> ids) {
        return repository.findAllById(ids);
    }
}