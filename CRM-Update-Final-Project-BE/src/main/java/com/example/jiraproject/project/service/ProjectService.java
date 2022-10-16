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
import java.util.List;
import java.util.UUID;

public interface ProjectService extends GenericService<Project, ProjectDto, UUID> {
    Project findProjectById(UUID projectId);
    ProjectWithInfoDto findByIdWithInfo(UUID projectId);
    List<ProjectWithInfoDto> findAllWithInfo();
    List<ProjectWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex);
    ProjectWithInfoDto addCreator(UUID projectId, UUID userId);
    ProjectWithInfoDto removeCreator(UUID projectId);
    ProjectWithInfoDto addLeader(UUID projectId, UUID userId);
    ProjectWithInfoDto removeLeader(UUID projectId);
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

    @Override
    public Project findProjectById(UUID projectId) {
        return repository.findById(projectId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
    }

    @Override
    public ProjectWithInfoDto findByIdWithInfo(UUID projectId) {
        Project project = repository.findByIdWithInfo(projectId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        return mapper.map(project, ProjectWithInfoDto.class);
    }

    @Override
    public List<ProjectWithInfoDto> findAllWithInfo() {
        return repository.findAllWithCreatorAndLeader().stream()
                .map(model -> mapper.map(model, ProjectWithInfoDto.class)).toList();
    }

    @Override
    public List<ProjectWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex) {
        return repository.findAllWithUserWithPaging(PageRequest.of(pageIndex, size, Sort.by("createdAt")))
                .stream()
                .map(model -> mapper.map(model, ProjectWithInfoDto.class))
                .toList();
    }

    @Override
    public ProjectWithInfoDto addCreator(UUID projectId, UUID userId) {
        Project project = repository.findById(projectId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        User user = userService.findUserById(userId);
        project.setCreator(user);
        return mapper.map(project, ProjectWithInfoDto.class);
    }

    @Override
    public ProjectWithInfoDto removeCreator(UUID projectId) {
        Project project = repository.findById(projectId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        project.setCreator(null);
        return mapper.map(project, ProjectWithInfoDto.class);
    }

    @Override
    public ProjectWithInfoDto addLeader(UUID projectId, UUID userId) {
        Project project = repository.findById(projectId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        User user = userService.findUserById(userId);
        project.setLeader(user);
        return mapper.map(project, ProjectWithInfoDto.class);
    }

    @Override
    public ProjectWithInfoDto removeLeader(UUID projectId) {
        Project project = repository.findById(projectId)
                .orElseThrow(() ->
                        new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        project.setLeader(null);
        return mapper.map(project, ProjectWithInfoDto.class);
    }
}
