package com.example.jiraproject.role.service;

import com.example.jiraproject.common.service.GenericService;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.operation.service.OperationService;
import com.example.jiraproject.role.dto.RoleDto;
import com.example.jiraproject.role.dto.RoleWithInfo;
import com.example.jiraproject.role.model.Role;
import com.example.jiraproject.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleService extends GenericService<Role, RoleDto, UUID> {
    RoleWithInfo addOperations(UUID roleId, Set<UUID> operationIds);
    RoleWithInfo removeOperations(UUID roleId, Set<UUID> operationIds);
    RoleWithInfo findByIdWithInfo(UUID id);
    List<RoleWithInfo> findAllWithInfo();
    List<RoleWithInfo> findAllWithInfoWithPaging(int size, int pageIndex);
    List<Role> findAllByIds(Set<UUID> roleIds);
}
@Service
@Transactional
@RequiredArgsConstructor
class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private final ModelMapper mapper;
    private final OperationService operationService;
    private final MessageSource messageSource;
    private static final String UUID_NOT_FOUND = "role.id.not-found";

    @Override
    public JpaRepository<Role, UUID> getRepository() {
        return this.repository;
    }

    @Override
    public ModelMapper getMapper() {
        return this.mapper;
    }

    @Override
    public RoleWithInfo findByIdWithInfo(UUID id) {
        Role role = repository.findByIdWithInfo(id)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        return mapper.map(role, RoleWithInfo.class);
    }

    @Override
    public List<RoleWithInfo> findAllWithInfo() {
        return repository.findAllWithInfo().stream()
                .map(role -> mapper.map(role, RoleWithInfo.class))
                .toList();
    }

    @Override
    public List<RoleWithInfo> findAllWithInfoWithPaging(int size, int pageIndex) {
        return repository.findAllWithInfoWithPaging(PageRequest.of(pageIndex, size, Sort.by("createdAt")))
                .stream()
                .map(model -> mapper.map(model, RoleWithInfo.class))
                .toList();
    }

    @Override
    public List<Role> findAllByIds(Set<UUID> roleIds) {
        return repository.findAllById(roleIds);
    }

    @Override
    public RoleWithInfo addOperations(UUID roleId, Set<UUID> operationIds) {
        Role role = repository.findById(roleId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        operationService.findAllByIds(operationIds).forEach(role::addOperation);
        return mapper.map(role, RoleWithInfo.class);
    }

    @Override
    public RoleWithInfo removeOperations(UUID roleId, Set<UUID> operationIds) {
        Role role = repository.findById(roleId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        operationService.findAllByIds(operationIds).forEach(role::removeOperation);
        return mapper.map(role, RoleWithInfo.class);
    }

}
