package com.example.jiraproject.task.service;

import com.example.jiraproject.common.service.GenericService;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.notification.service.NotificationService;
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
import java.time.LocalDate;
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

    void workWithTask(UUID id);

    void completeTask(UUID id);
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
    private final NotificationService notificationService;

    @Override
    public JpaRepository<Task, UUID> getRepository() {
        return this.repository;
    }

    @Override
    public ModelMapper getMapper() {
        return this.mapper;
    }

    @Transactional(readOnly = true)
    @Override
    public Task findTaskById(UUID taskId) {
        return repository.findById(taskId)
                .orElseThrow(() -> new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
    }

    @Transactional(readOnly = true)
    @Override
    public TaskWithInfoDto findByIdWithInfo(UUID taskId) {
        Task task = repository.findByIdWithInfo(taskId)
                .orElseThrow(() -> new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        return mapper.map(task, TaskWithInfoDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskWithInfoDto> findAllWithInfo() {
        return repository.findAllWithInfo().stream()
                .map(model -> mapper.map(model, TaskWithInfoDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<TaskWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex) {
        return repository.findAllWithInfoWithPaging(PageRequest.of(pageIndex, size, Sort.by("createdAt")))
                .stream().map(model -> mapper.map(model, TaskWithInfoDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<String> findAllStatus() {
        return Arrays.stream(Task.Status.values()).map(Enum::toString).toList();
    }

    @Override
    public TaskDto save(TaskDto taskDto) {
        Project project = projectService.findProjectByName(taskDto.getProjectName());
        Task task = mapper.map(taskDto, Task.class);
        task.setProject(project);
        if (taskDto.getReporterUsername() != null) { //if reporter is not null
            User reporter = userService.findByUsername(taskDto.getReporterUsername());
            addReporterToTask(task, reporter);
        }
        return mapper.map(repository.save(task), TaskDto.class);
    }

    @Override
    public TaskDto update(TaskDto taskDto) { //update task can't change project, only reporters
        Task task = findTaskById(taskDto.getId());
        User oldReporter = task.getReporter();
        if (taskDto.getReporterUsername() != null) { //if update with newReporter, reporter -> staff
            User newReporter = userService.findByUsername(taskDto.getReporterUsername());
            //check whether oldReporter and newReporter are the same
            if (oldReporter == null) {
                addReporterToTask(task, newReporter);
            } else if (!oldReporter.getUsername().equals(newReporter.getUsername())) {
                removeReporterFromTask(task, oldReporter);
                addReporterToTask(task, newReporter);
            }
        } //else do nothing with reporter
        mapper.map(taskDto, task);
        return mapper.map(task, TaskDto.class);
    }

    public void workWithTask(UUID id) {
        // work task will affect endDateInFact
        Task task = findTaskById(id);
        task.setStatus(Task.Status.IN_PROGRESS);
        task.setStartDateInFact(LocalDate.now());
    }

    @Override
    public void completeTask(UUID id) {
        //complete task will affect endDateInFact
        Task task = findTaskById(id);
        task.setStatus(Task.Status.DONE);
        task.setEndDateInFact(LocalDate.now());
    }

    private void addReporterToTask(Task task, User reporter) {
        task.setReporter(reporter);
        String content = MessageUtil.getMessage(messageSource,
                new Object[]{task.getProject().getLeader().getUsername(), task.getName()},
                "notification.add-staff-to-task");
        notificationService.saveNotification(content, task.getProject().getLeader(), reporter);
        notificationService.sendNotificationToUser(reporter.getUsername(), content);
    }

    private void removeReporterFromTask(Task task, User reporter) {
        task.setReporter(null);
        String content = MessageUtil.getMessage(messageSource,
                new Object[]{task.getProject().getLeader().getUsername(), task.getName()},
                "notification.remove-staff-from-task");
        notificationService.saveNotification(content, task.getProject().getLeader(), reporter);
        notificationService.sendNotificationToUser(reporter.getUsername(), content);
    }
}
