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
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public interface TaskService extends GenericService<Task, TaskDto, UUID> {
    Task findTaskById(UUID taskId);
    TaskWithInfoDto findByIdWithInfo(UUID taskId);
    List<TaskWithInfoDto> findAllWithInfo();
    List<TaskWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex);
    List<String> findAllStatus();
    TaskDto save(TaskDto taskDto);
    TaskDto update(TaskDto taskDto);
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
    public List<String> findAllStatus() {
        return Arrays.stream(Task.Status.values()).map(Enum::toString).toList();
    }

    @Override
    public TaskDto save(TaskDto taskDto) {
        Project project = projectService.findProjectByName(taskDto.getProjectName());
        User reporter = userService.findByUsername(taskDto.getReporterUsername());
        Task task = mapper.map(taskDto, Task.class);
        task.setProject(project);
        task.setReporter(reporter);
        return mapper.map(repository.save(task), TaskDto.class);
    }

    @Override
    public TaskDto update(TaskDto taskDto) {
        Task task = findTaskById(taskDto.getId());
        Project project = projectService.findProjectByName(taskDto.getProjectName());
        User user = userService.findByUsername(taskDto.getReporterUsername());
        mapper.map(taskDto, task);
        task.setReporter(user);
        task.setProject(project);
        return mapper.map(task, TaskDto.class);
    }
}
