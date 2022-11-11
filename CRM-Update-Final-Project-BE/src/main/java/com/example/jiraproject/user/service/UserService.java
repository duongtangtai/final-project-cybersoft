package com.example.jiraproject.user.service;

import com.example.jiraproject.common.service.GenericService;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.role.model.Role;
import com.example.jiraproject.role.repository.RoleRepository;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.*;
import java.util.stream.Collectors;

public interface UserService extends GenericService<User, UserDto, UUID> {
    User findUserById(UUID id);
    User findByUsername(String username);
    List<User> findAllByIds(Set<UUID> ids);
    UserWithInfoDto findByIdWithInfo(UUID userId);
    List<UserWithInfoDto> findAllWithInfo();
    List<UserWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex);
    List<String> findAllAccountStatus();
    List<String> findAllGenders();
    List<UserWithInfoDto> findAllInsideProject(UUID projectId);
    List<UserWithInfoDto> findAllOutsideProject(UUID projectId);
    List<UserDto> findAllLeaderRole();
    UserWithInfoDto updateRoles(UUID userId, Set<UUID> roleIds);
    UserDto save(UserDto dto);
    UserDto update(UserDto dto);
}

@Service
@Transactional
@RequiredArgsConstructor
class UserServiceImpl implements UserService {
    private final UserRepository repository;
    private final ModelMapper mapper;
    private final RoleService roleService;
    private final RoleRepository roleRepository;
    private final MessageSource messageSource;
    private static final String UUID_NOT_FOUND = "user.id.not-found";
    private static final String USERNAME_NOT_FOUND = "user.username.not-found";
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public JpaRepository<User, UUID> getRepository() {
        return this.repository;
    }

    @Override
    public ModelMapper getMapper() {
        return this.mapper;
    }

    @Transactional(readOnly = true)
    @Override
    public User findUserById(UUID id) {
        return repository.findById(id).orElseThrow(() ->
                new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
    }

    @Transactional(readOnly = true)
    @Override
    public User findByUsername(String username) {
        return repository.findByUsername(username).orElseThrow(() ->
                new ValidationException(MessageUtil.getMessage(messageSource, USERNAME_NOT_FOUND)));
    }

    @Transactional(readOnly = true)
    @Override
    public List<User> findAllByIds(Set<UUID> ids) {
        return repository.findAllById(ids);
    }

    @Transactional(readOnly = true)
    @Override
    public UserWithInfoDto findByIdWithInfo(UUID userId) {
        User user = repository.findByIdWithInfo(userId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        return mapper.map(user, UserWithInfoDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserWithInfoDto> findAllWithInfo() {
        return repository.findAllWithInfo().stream()
                .map(model -> mapper.map(model, UserWithInfoDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<UserWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex) {
        return repository.findAllWithInfoWithPaging(PageRequest.of(pageIndex, size, Sort.by("createdAt")))
                .stream()
                .map(model -> mapper.map(model, UserWithInfoDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findAllAccountStatus() {
        return Arrays.stream(User.AccountStatus.values()).map(Enum::toString).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findAllGenders() {
        return Arrays.stream(User.Gender.values()).map(Enum::toString).toList();
    }

    @Override
    public List<UserWithInfoDto> findAllInsideProject(UUID projectId) {
        return repository.findAllInsideProject(projectId)
                .stream().map(user -> mapper.map(user, UserWithInfoDto.class))
                .toList();
    }

    @Override
    public List<UserWithInfoDto> findAllOutsideProject(UUID projectId) {
        return repository.findAllOutsideProject(projectId)
                .stream().map(user -> mapper.map(user, UserWithInfoDto.class))
                .toList();
    }

    @Override
    public List<UserDto> findAllLeaderRole() {
        return repository.findAllLeaderRole()
                .stream().map(user -> mapper.map(user, UserDto.class))
                .toList();
    }

    @Override
    public UserWithInfoDto updateRoles(UUID userId, Set<UUID> roleIds) {
        User user = repository.findById(userId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        roleRepository.findAll().forEach(role -> {
            user.getRoles().remove(role);
            role.getUsers().remove(user);
        });
        roleService.findAllByIds(roleIds).forEach(user::addRole);
        return mapper.map(user, UserWithInfoDto.class);
    }

    @Override
    public UserDto save(UserDto dto) {
        User user = mapper.map(dto, User.class);
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return mapper.map(repository.save(user), UserDto.class);
    }

    @Override
    public UserDto update(UserDto dto) {
        User user = repository.findById(dto.getId())
                .orElseThrow(() ->
                    new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        String password = user.getPassword(); //update don't change user password
        mapper.map(dto, user);
        user.setPassword(password);
        return mapper.map(user, UserDto.class);
    }
}
