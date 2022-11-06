package com.example.jiraproject.project.service;

import com.example.jiraproject.common.service.GenericService;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.project.dto.ProjectDto;
import com.example.jiraproject.project.dto.ProjectWithInfoDto;
import com.example.jiraproject.project.model.Project;
import com.example.jiraproject.project.repository.ProjectRepository;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.ValidationException;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.UUID;

public interface ProjectService extends GenericService<Project, ProjectDto, UUID> {
    Project findProjectByName(String name);
    ProjectWithInfoDto findByIdWithInfo(UUID projectId);
    List<ProjectWithInfoDto> findAllWithInfo();
    List<ProjectWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex);
    List<String> findAllProjectStatus();
    ProjectDto save(ProjectDto dto);
    ProjectWithInfoDto addUsers(UUID projectId, Set<UUID> userIds);
    ProjectWithInfoDto removeUsers(UUID projectId, Set<UUID> userIds);
}
@Service
@Transactional
@RequiredArgsConstructor
class ProjectServiceImpl implements ProjectService {
    private final ProjectRepository repository;
    private final ModelMapper mapper;
    private final MessageSource messageSource;
    private final UserService userService;
    private static final String UUID_NOT_FOUND = "project.id.not-found";

    @Override
    public JpaRepository<Project, UUID> getRepository() {
        return this.repository;
    }

    @Override
    public ModelMapper getMapper() {
        return this.mapper;
    }

    public Project findProjectById(UUID projectId) {
        return repository.findById(projectId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
    }

    @Transactional(readOnly = true)
    @Override
    public Project findProjectByName(String name) {
        return repository.findByName(name)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, "project.name.not-found")));
    }

    @Transactional(readOnly = true)
    @Override
    public ProjectWithInfoDto findByIdWithInfo(UUID projectId) {
        Project project = repository.findByIdWithInfo(projectId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        return mapper.map(project, ProjectWithInfoDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectWithInfoDto> findAllWithInfo() {
        return repository.findAllWithCreatorAndLeader().stream()
                .map(model -> mapper.map(model, ProjectWithInfoDto.class)).toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<ProjectWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex) {
        return repository.findAllWithUserWithPaging(PageRequest.of(pageIndex, size, Sort.by("createdAt")))
                .stream()
                .map(model -> mapper.map(model, ProjectWithInfoDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findAllProjectStatus() {
        return Arrays.stream(Project.Status.values()).map(Enum::toString).toList();
    }

    @Override
    public ProjectDto save(ProjectDto dto) {
        User creator = userService.findByUsername(dto.getCreatorUsername());
        User leader = userService.findByUsername(dto.getLeaderUsername());
        Project project = mapper.map(dto, Project.class);
        project.setCreator(creator);
        project.setLeader(leader);
        return mapper.map(repository.save(project), ProjectDto.class);
    }

    @Override
    public ProjectWithInfoDto addUsers(UUID projectId, Set<UUID> userIds) {
        Project project = findProjectById(projectId);
        List<User> users = userService.findAllByIds(userIds);
        users.forEach(project::addUser);
        return mapper.map(project, ProjectWithInfoDto.class);
    }

    @Override
    public ProjectWithInfoDto removeUsers(UUID projectId, Set<UUID> userIds) {
        Project project = findProjectById(projectId);
        List<User> users = userService.findAllByIds(userIds);
        users.forEach(project::removeUser);
        return mapper.map(project, ProjectWithInfoDto.class);
    }
}
