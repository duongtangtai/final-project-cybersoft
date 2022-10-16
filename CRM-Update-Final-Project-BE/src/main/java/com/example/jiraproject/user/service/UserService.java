package com.example.jiraproject.user.service;

import com.example.jiraproject.common.service.GenericService;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.role.service.RoleService;
import com.example.jiraproject.user.dto.UserDto;
import com.example.jiraproject.user.dto.UserWithInfoDto;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.repository.UserRepository;
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

public interface UserService extends GenericService<User, UserDto, UUID> {
    User findUserById(UUID id);
    User findByUsername(String username);
    UserWithInfoDto findByIdWithInfo(UUID userId);
    List<UserWithInfoDto> findAllWithInfo();
    List<UserWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex);
    UserWithInfoDto addRoles(UUID userId, Set<UUID> roleIds);
    UserWithInfoDto removeRoles(UUID userId, Set<UUID> roleIds);
}

@Service
@Transactional
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final ModelMapper mapper;
    private final RoleService roleService;
    private final MessageSource messageSource;
    private static final String UUID_NOT_FOUND = "user.id.not-found";

    @Override
    public JpaRepository<User, UUID> getRepository() {
        return this.repository;
    }

    @Override
    public ModelMapper getMapper() {
        return this.mapper;
    }

    @Override
    public User findUserById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
    }

    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() ->
                new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
    }

    @Override
    public UserWithInfoDto findByIdWithInfo(UUID userId) {
        User user = repository.findByIdWithInfo(userId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        return mapper.map(user, UserWithInfoDto.class);
    }

    @Override
    public List<UserWithInfoDto> findAllWithInfo() {
        return repository.findAllWithInfo().stream()
                .map(model -> mapper.map(model, UserWithInfoDto.class))
                .toList();
    }

    @Override
    public List<UserWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex) {
        return repository.findAllWithInfoWithPaging(PageRequest.of(pageIndex, size, Sort.by("createdAt")))
                .stream()
                .map(model -> mapper.map(model, UserWithInfoDto.class))
                .toList();
    }

    @Override
    public UserWithInfoDto addRoles(UUID userId, Set<UUID> roleIds) {
        User user = repository.findById(userId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        roleService.findAllByIds(roleIds).forEach(user::addRole);
        return mapper.map(user, UserWithInfoDto.class);
    }

    @Override
    public UserWithInfoDto removeRoles(UUID userId, Set<UUID> roleIds) {
        User user = repository.findById(userId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        roleService.findAllByIds(roleIds).forEach(user::removeRole);
        return mapper.map(user, UserWithInfoDto.class);
    }

}
