package com.example.jiraproject.task.service;

import com.example.jiraproject.common.service.GenericService;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.project.model.Project;
import com.example.jiraproject.project.service.ProjectService;
import com.example.jiraproject.task.dto.TaskDto;
import com.example.jiraproject.task.dto.TaskWithInfoDto;
import com.example.jiraproject.task.model.Task;
import com.example.jiraproject.task.repository.TaskRepository;
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

public interface TaskService extends GenericService<Task, TaskDto, UUID> {
    Task findTaskById(UUID taskId);
    TaskWithInfoDto findByIdWithInfo(UUID taskId);
    List<TaskWithInfoDto> findAllWithInfo();
    List<TaskWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex);
    TaskWithInfoDto createTask(TaskDto taskDto, UUID projectId, UUID reporterID);
    TaskWithInfoDto updateTask(TaskDto taskDto, UUID reporterId);
}
@Service
@Transactional
@RequiredArgsConstructor
class TaskServiceImpl implements TaskService {
    private final TaskRepository repository;
    private final ModelMapper mapper;
    private final MessageSource messageSource;
    private static final String UUID_NOT_FOUND = "task.id.not-found";
    private final ProjectService projectService;
    private final UserService userService;

    @Override
    public JpaRepository<Task, UUID> getRepository() {
        return this.repository;
    }

    @Override
    public ModelMapper getMapper() {
        return this.mapper;
    }

    @Override
    public Task findTaskById(UUID taskId) {
        return repository.findById(taskId)
                .orElseThrow(() -> new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
    }

    @Override
    public TaskWithInfoDto findByIdWithInfo(UUID taskId) {
        Task task = repository.findByIdWithInfo(taskId)
                .orElseThrow(() -> new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        return mapper.map(task, TaskWithInfoDto.class);
    }

    @Override
    public List<TaskWithInfoDto> findAllWithInfo() {
        return repository.findAllWithInfo().stream()
                .map(model -> mapper.map(model, TaskWithInfoDto.class))
                .toList();
    }

    @Override
    public List<TaskWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex) {
        return repository.findAllWithInfoWithPaging(PageRequest.of(pageIndex, size, Sort.by("createdAt")))
                .stream().map(model -> mapper.map(model, TaskWithInfoDto.class))
                .toList();
    }

    @Override
    public TaskWithInfoDto createTask(TaskDto taskDto, UUID projectId, UUID reporterId) {
        Project project = projectService.findProjectById(projectId);
        User reporter = userService.findUserById(reporterId);
        Task task = mapper.map(taskDto, Task.class);
        task.setProject(project);
        task.setReporter(reporter);
        return mapper.map(repository.save(task), TaskWithInfoDto.class);
    }

    @Override
    public TaskWithInfoDto updateTask(TaskDto taskDto, UUID reporterId) {
        Task task = repository.findById(taskDto.getId())
                .orElseThrow(() -> new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        User user = userService.findUserById(reporterId);
        mapper.map(taskDto, task);
        task.setReporter(user);
        return mapper.map(task, TaskWithInfoDto.class);
    }
}
