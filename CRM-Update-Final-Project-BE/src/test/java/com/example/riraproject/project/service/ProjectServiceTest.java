package com.example.riraproject.project.service;

import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.notification.service.NotificationService;
import com.example.riraproject.project.dto.ProjectDto;
import com.example.riraproject.project.dto.ProjectWithInfoDto;
import com.example.riraproject.project.model.Project;
import com.example.riraproject.project.repository.ProjectRepository;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.service.UserService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.context.MessageSource;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import javax.validation.ValidationException;
import java.util.*;

@ExtendWith(MockitoExtension.class)
class ProjectServiceTest {
    @Mock private ProjectRepository repository;
    @Mock private ModelMapper mapper;
    @Mock private MessageSource messageSource;
    @Mock private UserService userService;
    @Mock private NotificationService notificationService;
    @InjectMocks private ProjectServiceImpl service;
    private final UUID id = UUID.randomUUID();
    @Mock private Project model;
    @Mock private ProjectDto dto;
    @Mock private ProjectWithInfoDto dtoWithInfo;

    @Test
    void getRepositoryAndMapper() {
        Assertions.assertEquals(repository, service.getRepository());
        Assertions.assertEquals(mapper, service.getMapper());
    }

    @Test
    void findProjectByIdTest() {
        //SETUP
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(model));
        //TRY
        //CASE 1: ID IS VALID
        Assertions.assertEquals(model, service.findProjectById(id));
        Mockito.verify(repository).findById(id);
        //CASE 2: ID IS INVALID
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.findProjectById(id));
        Mockito.verify(repository, Mockito.times(2)).findById(id);
    }

    @Test
    void findProjectByName() {
        //SETUP
        String name = "projectName";
        Mockito.when(repository.findByName(name)).thenReturn(Optional.of(model));
        //TRY
        //CASE 1: NAME IS VALID
        Assertions.assertEquals(model, service.findProjectByName(name));
        Mockito.verify(repository).findByName(name);
        //CASE 2: NAME IS INVALID
        Mockito.when(repository.findByName(name)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.findProjectByName(name));
        Mockito.verify(repository, Mockito.times(2)).findByName(name);
    }

    @Test
    void findByIdWithInfoTest() {
        //SETUP
        Mockito.when(repository.findByIdWithInfo(id)).thenReturn(Optional.of(model));
        Mockito.when(mapper.map(model, ProjectWithInfoDto.class))
                .thenReturn(dtoWithInfo);
        //TRY
        //CASE 1: ID IS VALID
        Assertions.assertEquals(dtoWithInfo, service.findByIdWithInfo(id));
        Mockito.verify(repository).findByIdWithInfo(id);
        //CASE 2: ID IS INVALID
        Mockito.when(repository.findByIdWithInfo(id)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.findByIdWithInfo(id));
        Mockito.verify(repository, Mockito.times(2)).findByIdWithInfo(id);
    }

    @Test
    void findAllWithInfoTest() {
        //SETUP
        Mockito.when(repository.findAllWithCreatorAndLeader()).thenReturn(Set.of(model));
        Mockito.when(mapper.map(model, ProjectWithInfoDto.class))
                .thenReturn(dtoWithInfo);
        //TRY
        Assertions.assertEquals(List.of(dtoWithInfo), service.findAllWithInfo());
        Mockito.verify(repository).findAllWithCreatorAndLeader();
    }

    @Test
    void findAllWithInfoWithPagingTest() {
        //SETUP
        Page<Project> page = Mockito.mock(Page.class);
        Mockito.when(repository.findAllWithUserWithPaging(
                PageRequest.of(2, 3, Sort.by("createdAt"))))
                .thenReturn(page);
        Mockito.when(page.stream()).thenReturn(List.of(model).stream());
        Mockito.when(mapper.map(model, ProjectWithInfoDto.class))
                .thenReturn(dtoWithInfo);
        //TRY
        Assertions.assertEquals(List.of(dtoWithInfo),
                service.findAllWithInfoWithPaging(3, 2));
        Mockito.verify(repository).findAllWithUserWithPaging(
                PageRequest.of(2, 3, Sort.by("createdAt")));
    }

    @Test
    void findAllProjectStatusTest() {
        List<String> list = new ArrayList<>();
        list.add(Project.Status.DOING.toString());
        list.add(Project.Status.DONE.toString());
        Assertions.assertEquals(list, service.findAllProjectStatus());
    }

    @Test
    void saveTest() {
        //SETUP
        User creator = User.builder()
                .username("creator")
                .build();
        User leader =  User.builder()
                .username("leader")
                .build();
        ProjectDto input = ProjectDto.builder()
                .name("project")
                .creatorUsername("creator")
                .leaderUsername("leader")
                .build();
        Project savedModel = Project.builder()
                .build();
        Mockito.when(userService.findByUsername("creator"))
                .thenReturn(creator);
        Mockito.when(userService.findByUsername("leader"))
                .thenReturn(leader);
        Mockito.when(mapper.map(input, Project.class))
                .thenReturn(savedModel);
        Mockito.when(repository.save(savedModel)).thenReturn(savedModel);
        Mockito.when(mapper.map(savedModel, ProjectDto.class))
                .thenReturn(dto);
        //TRY
        Assertions.assertEquals(dto, service.save(input));
        Assertions.assertEquals(creator, savedModel.getCreator());
        Assertions.assertEquals(leader, savedModel.getLeader());
        Mockito.verify(userService).findByUsername("creator");
        Mockito.verify(userService).findByUsername("leader");
        Mockito.verify(notificationService).saveNotification(Mockito.any(), Mockito.any(), Mockito.any());
        Mockito.verify(notificationService).sendNotificationToUser(Mockito.any(), Mockito.any());
    }

    @Test
    void updateTest() {
        //SETUP
        User creator = User.builder()
                .username("creator")
                .build();
        User oldLeader = User.builder()
                .username("oldLeader")
                .build();
        User newLeader = User.builder()
                .username("newLeader")
                .build();
        Project project = Project.builder()
                .id(id)
                .leader(oldLeader)
                .creator(creator)
                .build();
        ProjectDto input = ProjectDto.builder()
                .id(id)
                .leaderUsername("oldLeader")
                .creatorUsername("creator")
                .build();
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(project));
        Mockito.when(userService.findByUsername("oldLeader")).thenReturn(oldLeader);
        Mockito.when(userService.findByUsername("creator")).thenReturn(creator);
        Mockito.doNothing().when(mapper).map(input, project);
        //TRY
        //CASE 1: DON'T UPDATE LEADER
        Mockito.when(mapper.map(project, ProjectDto.class)).thenReturn(dto);
        Assertions.assertDoesNotThrow(() -> service.update(input));
        Assertions.assertEquals(creator, project.getCreator());
        Assertions.assertEquals(oldLeader, project.getLeader());
        Mockito.verify(repository).findById(id);
        Mockito.verify(userService).findByUsername("oldLeader");
        Mockito.verify(userService).findByUsername("creator");
        //CASE 2: UPDATE LEADER
        Mockito.when(userService.findByUsername("newLeader")).thenReturn(newLeader);
        ProjectDto input2 = ProjectDto.builder()
                .id(id)
                .leaderUsername("newLeader")
                .creatorUsername("creator")
                .build();
        Mockito.doNothing().when(mapper).map(input2, project);
        Assertions.assertDoesNotThrow(() -> service.update(input2));
        Assertions.assertEquals(creator, project.getCreator());
        Assertions.assertEquals(newLeader, project.getLeader());
        Mockito.verify(repository, Mockito.times(2)).findById(id);
        Mockito.verify(userService, Mockito.times(1)).findByUsername("newLeader");
        Mockito.verify(userService, Mockito.times(2)).findByUsername("creator");
        String content = MessageUtil.getMessage(messageSource,
                new Object[]{ creator.getUsername(), project.getName()},
                "notification.leader.manager-remove-leader-from-project");
        Mockito.verify(notificationService).saveNotification(content, creator, oldLeader);
        Mockito.verify(notificationService).saveNotification(content, creator, newLeader);
        Mockito.verify(notificationService).sendNotificationToUser(oldLeader.getUsername(), content);
        Mockito.verify(notificationService).sendNotificationToUser(newLeader.getUsername(), content);
        //CASE 3: INVALID PROJECT ID
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.update(ProjectDto.builder().id(UUID.randomUUID()).build()));
    }

    @Test
    void addUsersTest() {
        //SETUP
        Set<User> users = new HashSet<>();
        users.add(User.builder().username("user1").projects(new HashSet<>()).build());
        users.add(User.builder().username("user2").projects(new HashSet<>()).build());
        users.add(User.builder().username("user3").projects(new HashSet<>()).build());
        Project project = Project.builder()
                .id(id)
                .users(users)
                .creator(User.builder().username("creator").build())
                .leader(User.builder().username("leader").build())
                .build();
        Set<UUID> userIds = new HashSet<>();
        List<User> newUsers = new ArrayList<>();
        newUsers.add(User.builder().username("user4").projects(new HashSet<>()).build());
        newUsers.add(User.builder().username("user5").projects(new HashSet<>()).build());
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(project));
        Mockito.when(userService.findAllByIds(userIds)).thenReturn(newUsers);
        Mockito.when(mapper.map(project, ProjectWithInfoDto.class)).thenReturn(dtoWithInfo);
        //TRY
        //CASE 1: PROJECT ID IS VALID
        Assertions.assertDoesNotThrow(() -> service.addUsers(id, userIds));
        Assertions.assertEquals(5, project.getUsers().size());
        Mockito.verify(userService).findAllByIds(userIds);
        String content1 = MessageUtil.getMessage(messageSource,
                new Object[]{ project.getCreator().getUsername(), project.getName()},
                "notification.staff.manager-add-staff-to-project");
        String content2 = MessageUtil.getMessage(messageSource,
                new Object[]{ project.getCreator().getUsername(), project.getName()},
                "notification.leader.manager-add-staff-to-project");
        Mockito.verify(notificationService).saveNotification(content1, project.getCreator(), newUsers.get(0));
        Mockito.verify(notificationService).saveNotification(content1, project.getCreator(), newUsers.get(1));
        Mockito.verify(notificationService, Mockito.times(2)).saveNotification(content2, project.getCreator(), project.getLeader());
        Mockito.verify(notificationService).sendNotificationToUser(newUsers.get(0).getUsername(), content1);
        Mockito.verify(notificationService).sendNotificationToUser(newUsers.get(1).getUsername(), content1);
        Mockito.verify(notificationService, Mockito.times(2)).sendNotificationToUser(project.getLeader().getUsername() , content2);
        //CASE 2: PROJECT ID IS INVALID
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.addUsers(UUID.randomUUID(), userIds));
    }

    @Test
    void removeUsersTest() {
        //SETUP
        Set<User> users = new HashSet<>();
        users.add(User.builder().username("user1").projects(new HashSet<>()).build());
        users.add(User.builder().username("user2").projects(new HashSet<>()).build());
        users.add(User.builder().username("user3").projects(new HashSet<>()).build());
        Project project = Project.builder()
                .id(id)
                .users(users)
                .creator(User.builder().username("creator").build())
                .leader(User.builder().username("leader").build())
                .build();
        Set<UUID> userIds = new HashSet<>();
        List<User> removedUsers = new ArrayList<>();
        removedUsers.add(User.builder().username("user1").projects(new HashSet<>()).build());
        removedUsers.add(User.builder().username("user2").projects(new HashSet<>()).build());
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(project));
        Mockito.when(userService.findAllByIds(userIds)).thenReturn(removedUsers);
        Mockito.when(mapper.map(project, ProjectWithInfoDto.class)).thenReturn(dtoWithInfo);
        //TRY
        //CASE 1: PROJECT ID IS VALID
        Assertions.assertDoesNotThrow(() -> service.removeUsers(id, userIds));
        Assertions.assertEquals(1, project.getUsers().size());
        Assertions.assertEquals("user3", project.getUsers().iterator().next().getUsername());
        Mockito.verify(userService).findAllByIds(userIds);
        String content1 = MessageUtil.getMessage(messageSource,
                new Object[]{ project.getCreator().getUsername(), project.getName()},
                "notification.staff.manager-remove-staff-from-project");
        String content2 = MessageUtil.getMessage(messageSource,
                new Object[]{ project.getCreator().getUsername(), project.getName()},
                "notification.leader.manager-remove-staff-from-project");

        Mockito.verify(notificationService).saveNotification(content1, project.getCreator(), removedUsers.get(0));
        Mockito.verify(notificationService).saveNotification(content1, project.getCreator(), removedUsers.get(1));
        Mockito.verify(notificationService, Mockito.times(2)).saveNotification(content2, project.getCreator(), project.getLeader());
        Mockito.verify(notificationService).sendNotificationToUser(removedUsers.get(0).getUsername(), content1);
        Mockito.verify(notificationService).sendNotificationToUser(removedUsers.get(1).getUsername(), content1);
        Mockito.verify(notificationService, Mockito.times(2)).sendNotificationToUser(project.getLeader().getUsername() , content2);
        //CASE 2: PROJECT ID IS INVALID
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.removeUsers(UUID.randomUUID(), userIds));
    }
}
