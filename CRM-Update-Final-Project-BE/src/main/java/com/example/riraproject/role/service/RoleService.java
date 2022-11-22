package com.example.riraproject.role.service;

import com.example.riraproject.common.service.GenericService;
import com.example.riraproject.role.dto.RoleDto;
import com.example.riraproject.role.model.Role;
import com.example.riraproject.role.repository.RoleRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface RoleService extends GenericService<Role, RoleDto, UUID> {
    List<Role> findAllByIds(Set<UUID> roleIds);
}
@Service
@Transactional
@RequiredArgsConstructor
class RoleServiceImpl implements RoleService {
    private final RoleRepository repository;
    private final ModelMapper mapper;
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

    @Transactional(readOnly = true)
    @Override
    public List<Role> findAllByIds(Set<UUID> roleIds) {
        return repository.findAllById(roleIds);
    }
}
