package com.example.riraproject.project.service;

import com.example.riraproject.common.service.GenericService;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.notification.service.NotificationService;
import com.example.riraproject.project.dto.ProjectDto;
import com.example.riraproject.project.dto.ProjectWithInfoDto;
import com.example.riraproject.project.model.Project;
import com.example.riraproject.project.repository.ProjectRepository;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.service.UserService;
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
    ProjectDto update(ProjectDto dto);
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
    private final NotificationService notificationService;
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
        //save & send notification to leader
        String content = MessageUtil.getMessage(messageSource,
                new Object[]{creator.getUsername(), project.getName()},
                "notification.leader.manager-add-leader-to-project");
        notificationService.saveNotification(content, creator, leader);
        notificationService.sendNotificationToUser(leader.getUsername(), content);
        return mapper.map(repository.save(project), ProjectDto.class);
    }

    @Override
    public ProjectDto update(ProjectDto dto) {
        Project project = findProjectById(dto.getId());
        User creator = userService.findByUsername(dto.getCreatorUsername());
        User newLeader = userService.findByUsername(dto.getLeaderUsername());
        //store oldLeader to check
        User oldLeader = project.getLeader();
        project.setCreator(creator);
        project.setLeader(newLeader);
        mapper.map(dto, project);
        //check oldLeader and newLeader are the same after the logic is complete
        if (!oldLeader.getUsername().equals(newLeader.getUsername())) { //if leader is replaced
            //save & send notification to oldLeader
            String content = MessageUtil.getMessage(messageSource,
                    new Object[]{ creator.getUsername(), project.getName()},
                    "notification.leader.manager-remove-leader-from-project");
            notificationService.saveNotification(content, creator, oldLeader);
            notificationService.sendNotificationToUser(oldLeader.getUsername(), content);
            //save & send notification to newLeader
            content = MessageUtil.getMessage(messageSource,
                    new Object[]{ creator.getUsername(), project.getName()},
                    "notification.leader.manager-add-leader-to-project");
            notificationService.saveNotification(content, creator, newLeader);
            notificationService.sendNotificationToUser(newLeader.getUsername(), content);
        }
        return mapper.map(project, ProjectDto.class);
    }

    @Override
    public ProjectWithInfoDto addUsers(UUID projectId, Set<UUID> userIds) {
        Project project = findProjectById(projectId);
        List<User> users = userService.findAllByIds(userIds);
        users.forEach(project::addUser);
        //save & send notifications to all staffs
        users.forEach(user -> {
            String content = MessageUtil.getMessage(messageSource,
                    new Object[]{ project.getCreator().getUsername(), project.getName()},
                    "notification.staff.manager-add-staff-to-project");
            notificationService.saveNotification(content, project.getCreator(), user);
            notificationService.sendNotificationToUser(user.getUsername(), content);
            //save & send notifications to leaders
            content = MessageUtil.getMessage(messageSource,
                    new Object[]{ project.getCreator().getUsername(), user.getUsername(), project.getName()},
                    "notification.leader.manager-add-staff-to-project");
            notificationService.saveNotification(content, project.getCreator(), project.getLeader());
            notificationService.sendNotificationToUser(project.getLeader().getUsername(), content);
        });
        return mapper.map(project, ProjectWithInfoDto.class);
    }

    @Override
    public ProjectWithInfoDto removeUsers(UUID projectId, Set<UUID> userIds) {
        Project project = findProjectById(projectId);
        List<User> users = userService.findAllByIds(userIds);
        users.forEach(project::removeUser);
        //save & send notifications to all staffs
        users.forEach(user -> {
            String content = MessageUtil.getMessage(messageSource,
                    new Object[]{ project.getCreator().getUsername(), project.getName()},
                    "notification.staff.manager-remove-staff-from-project");
            notificationService.saveNotification(content, project.getCreator(), user);
            notificationService.sendNotificationToUser(user.getUsername(), content);
            //save & send notifications to leaders
            content = MessageUtil.getMessage(messageSource,
                    new Object[]{ project.getCreator().getUsername(), user.getUsername(), project.getName()},
                    "notification.leader.manager-remove-staff-from-project");
            notificationService.saveNotification(content, project.getCreator(), project.getLeader());
            notificationService.sendNotificationToUser(project.getLeader().getUsername(), content);
        });
        return mapper.map(project, ProjectWithInfoDto.class);
    }
}
