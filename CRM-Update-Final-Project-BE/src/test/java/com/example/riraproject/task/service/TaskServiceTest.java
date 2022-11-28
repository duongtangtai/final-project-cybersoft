package com.example.riraproject.task.service;

import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.notification.service.NotificationService;
import com.example.riraproject.project.model.Project;
import com.example.riraproject.project.service.ProjectService;
import com.example.riraproject.task.dto.TaskDto;
import com.example.riraproject.task.dto.TaskWithInfoDto;
import com.example.riraproject.task.model.Task;
import com.example.riraproject.task.repository.TaskRepository;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.validation.ValidationException;
import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class TaskServiceTest {
    @Mock private TaskRepository repository;
    @Autowired private ModelMapper mapper;
    @Autowired private MessageSource messageSource;
    @Mock private ProjectService projectService;
    @Mock private UserService userService;
    @Mock private NotificationService notificationService;
    private TaskServiceImpl service;
    private Task model;
    private TaskDto dto;
    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void init() {
        service = new TaskServiceImpl(repository, mapper, messageSource, projectService,
                userService, notificationService);
        model = Task.builder()
                .description("this is a model")
                .build();
        dto = TaskDto.builder()
                .description("this is a dto")
                .build();
    }

    @Test
    void getRepositoryAndMapper() {
        Assertions.assertEquals(repository, service.getRepository());
        Assertions.assertEquals(mapper, service.getMapper());
    }

    @Test
    void findTaskByIdTest() {
        //SETUP
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(model));
        //CASE 1: ID IS VALID
        Task result = service.findTaskById(id);
        Assertions.assertEquals(model.getDescription(), result.getDescription());
        Mockito.verify(repository).findById(id);
        //CASE 2: ID IS INVALID
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.findTaskById(id));
    }

    @Test
    void findByIdWithInfoTest(){
        //SETUP
        Mockito.when(repository.findByIdWithInfo(id))
                .thenReturn(Optional.of(model));
        //CASE 1: ID IS VALID
        TaskWithInfoDto result = service.findByIdWithInfo(id);
        Assertions.assertEquals(model.getDescription(), result.getDescription());
        Mockito.verify(repository).findByIdWithInfo(id);
        //CASE 2: ID IS INVALID
        Mockito.when(repository.findByIdWithInfo(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.findByIdWithInfo(id));
    }

    @Test
    void findAllWithInfoTest() {
        //SETUP
        Mockito.when(repository.findAllWithInfo())
                .thenReturn(Set.of(model));
        //TRY
        List<TaskWithInfoDto> result = service.findAllWithInfo();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(model.getDescription(), result.get(0).getDescription());
        Mockito.verify(repository).findAllWithInfo();
    }

    @Test
    void findAllWithInfoWithPagingTest() {
        //SETUP
        Page<Task> page = Mockito.mock(Page.class);
        Mockito.when(repository.findAllWithInfoWithPaging(
                PageRequest.of(2, 3, Sort.by("createdAt"))))
                .thenReturn(page);
        Mockito.when(page.stream()).thenReturn(List.of(model).stream());
        //TRY
        List<TaskWithInfoDto> result = service.findAllWithInfoWithPaging(3, 2);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(model.getName(), result.get(0).getName());
        Mockito.verify(repository).findAllWithInfoWithPaging(
                PageRequest.of(2, 3, Sort.by("createdAt")));
    }

    @Test
    void findAllByProjectTest() {
        //SETUP
        Mockito.when(repository.findAllByProject(id))
                .thenReturn(Set.of(model));
        //TRY
        List<TaskWithInfoDto> result = service.findAllByProject(id);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(model.getDescription(), result.get(0).getDescription());
        Mockito.verify(repository).findAllByProject(id);
    }

    @Test
    void findAllByProjectAndUserTest() {
        //SETUP
        UUID userId = UUID.randomUUID();
        UUID projectId = UUID.randomUUID();
        Mockito.when(repository.findAllByProjectAndUser(projectId, userId))
                .thenReturn(Set.of(model));
        //TRY
        List<TaskWithInfoDto> result = service.findAllByProjectAndUser(projectId, userId);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(model.getDescription(), result.get(0).getDescription());
        Mockito.verify(repository).findAllByProjectAndUser(projectId, userId);
    }

    @Test
    void findAllByUserTest() {
        //SETUP
        Mockito.when(repository.findAllByUser(id))
                .thenReturn(Set.of(model));
        //TRY
        List<TaskWithInfoDto> result = service.findAllByUser(id);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(model.getDescription(), result.get(0).getDescription());
        Mockito.verify(repository).findAllByUser(id);
    }

    @Test
    void findAllStatus() {
        //SETUP
        List<String> statusList = new ArrayList<>();
        statusList.add(Task.Status.TODO.toString());
        statusList.add(Task.Status.IN_PROGRESS.toString());
        statusList.add(Task.Status.DONE.toString());
        //TRY
        List<String> result = service.findAllStatus();
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(statusList.get(0), result.get(0));
        Assertions.assertEquals(statusList.get(1), result.get(1));
        Assertions.assertEquals(statusList.get(2), result.get(2));
    }

    @Test
    void saveTest() {
        //SETUP
        Project project = Project.builder()
                .name("project")
                .leader(User.builder().username("leader").build())
                .build();
        Task savedTask = Task.builder()
                .description("this is a saved task")
                .build();
        dto.setProjectName("project");
        Mockito.when(projectService.findProjectByName(dto.getProjectName()))
                .thenReturn(project);
        Mockito.when(repository.save(Mockito.any())).thenReturn(savedTask);
        //CASE 1: REPORTER IS NULL
        TaskDto result = service.save(dto);
        Assertions.assertEquals(savedTask.getDescription(), result.getDescription());
        Assertions.assertNull(result.getReporterUsername());
        //CASE 2: REPORTER IS NOT NULL
        User reporter = User.builder()
                .username("reporter")
                .build();
        savedTask.setReporter(reporter);
        dto.setReporterUsername("reporter");
        Mockito.when(userService.findByUsername(dto.getReporterUsername()))
                .thenReturn(reporter);
        TaskDto result2 = service.save(dto);
        Assertions.assertEquals(savedTask.getDescription(), result2.getDescription());
        Assertions.assertEquals(reporter.getUsername(), result2.getReporterUsername());
        Mockito.verify(notificationService).saveNotification(Mockito.anyString(), Mockito.any(User.class), Mockito.any(User.class));
        Mockito.verify(notificationService).sendNotificationToUser(Mockito.anyString(), Mockito.anyString());
    }

    @Test
    void updateTest() {
        //SETUP
        TaskDto input = TaskDto.builder()
                .id(UUID.randomUUID())
                .name("input task")
                .build();
        User oldReporter = User.builder()
                .username("oldReporter")
                .build();
        User newReporter = User.builder()
                .username("newReporter")
                .build();
        Task task = Task.builder()
                .id(input.getId())
                .name("task")
                .project(Project.builder()
                        .leader(User.builder().username("leader").build())
                        .build())
                .build();
        //CASE 1: input has an invalid ID
        Mockito.when(repository.findById(input.getId())).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.update(input));
        //CASE 2: input has valid ID, the task didn't have reporter, the new task doesn't have reporter
        Mockito.when(repository.findById(input.getId())).thenReturn(Optional.of(task));
        TaskDto result2 = service.update(input);
        //check result
        Assertions.assertEquals(input.getName(), result2.getName());
        Mockito.verify(notificationService, Mockito.never())
                .saveNotification(Mockito.anyString(), Mockito.any(User.class), Mockito.any(User.class));
        Mockito.verify(notificationService, Mockito.never())
                .sendNotificationToUser(Mockito.anyString(), Mockito.anyString());
        //CASE 3: the task and the new task have the same reporter
        task.setReporter(oldReporter);
        input.setReporterUsername(oldReporter.getUsername());
        Mockito.when(userService.findByUsername(oldReporter.getUsername()))
                .thenReturn(oldReporter);
        TaskDto result3 = service.update(input);
        //check result
        Assertions.assertEquals(input.getName(), result3.getName());
        Assertions.assertEquals(oldReporter.getUsername(), result3.getReporterUsername());
        Mockito.verify(notificationService, Mockito.never())
                .saveNotification(Mockito.anyString(), Mockito.any(User.class), Mockito.any(User.class));
        Mockito.verify(notificationService, Mockito.never())
                .sendNotificationToUser(Mockito.anyString(), Mockito.anyString());
        //CASE 4: the task didn't have reporter, the new task has reporter
        task.setReporter(null);
        input.setReporterUsername(newReporter.getUsername());
        Mockito.when(userService.findByUsername(newReporter.getUsername()))
                .thenReturn(newReporter);
        //the newReporter should receive notification
        TaskDto result4 = service.update(input);
        String content4 = MessageUtil.getMessage(messageSource,
                new Object[]{task.getProject().getLeader().getUsername(), task.getName()},
                "notification.add-staff-to-task");
        Mockito.verify(notificationService)
                .saveNotification(content4, task.getProject().getLeader(), newReporter);
        Mockito.verify(notificationService)
                .sendNotificationToUser(newReporter.getUsername(), content4);
        //check result
        Assertions.assertEquals(input.getName(), result4.getName());
        Assertions.assertEquals(newReporter.getUsername(), result4.getReporterUsername());
        //CASE 5: the task had reporter, the new task doesn't have reporter
        task.setReporter(oldReporter);
        input.setReporterUsername(null);
        //old reporter will know he's been removed
        TaskDto result5 = service.update(input);
        String content5 = MessageUtil.getMessage(messageSource,
                new Object[]{task.getProject().getLeader().getUsername(), task.getName()},
                "notification.remove-staff-from-task");
        Mockito.verify(notificationService)
                .saveNotification(content5, task.getProject().getLeader(), oldReporter);
        Mockito.verify(notificationService)
                .sendNotificationToUser(oldReporter.getUsername(), content5);
        //check result
        Assertions.assertEquals(input.getName(), result5.getName());
        Assertions.assertNull(result5.getReporterUsername());
        //CASE 6: the task and the new task have different reporter
        task.setReporter(oldReporter);
        input.setReporterUsername(newReporter.getUsername());
        //both reporters will get notifications
        TaskDto result6 = service.update(input);
        //old reporter will know he's been removed
        String content6 = MessageUtil.getMessage(messageSource,
                new Object[]{task.getProject().getLeader().getUsername(), task.getName()},
                "notification.remove-staff-from-task");
        Mockito.verify(notificationService, Mockito.times(2))
                .saveNotification(content6, task.getProject().getLeader(), oldReporter);
        Mockito.verify(notificationService, Mockito.times(2))
                .sendNotificationToUser(oldReporter.getUsername(), content6);
        //new reporter will know he's been assigned
        content6 = MessageUtil.getMessage(messageSource,
                new Object[]{task.getProject().getLeader().getUsername(), task.getName()},
                "notification.add-staff-to-task");
        Mockito.verify(notificationService, Mockito.times(2))
                .saveNotification(content6, task.getProject().getLeader(), newReporter);
        Mockito.verify(notificationService, Mockito.times(2))
                .sendNotificationToUser(newReporter.getUsername(), content6);
        //check result
        Assertions.assertEquals(input.getName(), result6.getName());
        Assertions.assertEquals(newReporter.getUsername(), result6.getReporterUsername());
    }

    @Test
    void workWithTaskTest() {
        //SETUP
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(model));
        //CASE 1 : VALID ID
        service.workWithTask(id);
        Assertions.assertEquals(Task.Status.IN_PROGRESS, model.getStatus());
        Assertions.assertNotNull(model.getStartDateInFact());
        //CASE 2 : INVALID ID
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.workWithTask(id));
    }

    @Test
    void completeTaskTest() {
        //SETUP
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(model));
        //CASE 1 : VALID ID
        service.completeTask(id);
        Assertions.assertEquals(Task.Status.DONE, model.getStatus());
        Assertions.assertNotNull(model.getEndDateInFact());
        //CASE 2 : INVALID ID
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.workWithTask(id));
    }
}
