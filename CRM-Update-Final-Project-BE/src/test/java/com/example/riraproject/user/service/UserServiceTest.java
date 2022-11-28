package com.example.riraproject.user.service;

import com.example.riraproject.project.model.Project;
import com.example.riraproject.role.model.Role;
import com.example.riraproject.role.repository.RoleRepository;
import com.example.riraproject.role.service.RoleService;
import com.example.riraproject.task.model.Task;
import com.example.riraproject.user.dto.UserDto;
import com.example.riraproject.user.dto.UserWithProjectInfoDto;
import com.example.riraproject.user.dto.UserWithTaskInfoDto;
import com.example.riraproject.user.model.User;
import com.example.riraproject.user.repository.UserRepository;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
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
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import javax.validation.ValidationException;
import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class UserServiceTest {
    @Mock private UserRepository repository;
    @Autowired private ModelMapper mapper;
    @Mock private RoleService roleService;
    @Mock private RoleRepository roleRepository;
    @Autowired private MessageSource messageSource;
    @Autowired private BCryptPasswordEncoder passwordEncoder;
    private final UUID id = UUID.randomUUID();
    private UserServiceImpl service;
    private User user;
    private UserDto userDto;
    private UserWithProjectInfoDto userWithProject;
    private UserWithTaskInfoDto userWithTask;

    @BeforeEach
    void init() {
        service = new UserServiceImpl(repository, mapper, roleService, roleRepository, messageSource,
                passwordEncoder);
        user = User.builder()
                .username("user")
                .build();
        userDto = UserDto.builder()
                .username("userDto")
                .build();
        userWithProject = new UserWithProjectInfoDto();
        userWithProject.setUsername("userWithProject");
        userWithTask = new UserWithTaskInfoDto();
        userWithTask.setUsername("userWithTask");
    }

    @Test
    void getRepositoryAndMapper() {
        Assertions.assertEquals(repository, service.getRepository());
        Assertions.assertEquals(mapper, service.getMapper());
    }

    @Test
    void findUserByIdTest() {
        //SETUP
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(user));
        //CASE 1 : VALID ID
        Assertions.assertEquals(user, service.findUserById(id));
        Mockito.verify(repository).findById(id);
        //CASE 2 : INVALID ID
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.findUserById(id));
    }

    @Test
    void findByUsernameTest() {
        //SETUP
        String username = "username";
        Mockito.when(repository.findByUsername(username))
                .thenReturn(Optional.of(user));
        //CASE 1 : VALID ID
        Assertions.assertEquals(user, service.findByUsername(username));
        Mockito.verify(repository).findByUsername(username);
        //CASE 2 : INVALID ID
        Mockito.when(repository.findByUsername(username))
                .thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.findByUsername(username));
    }

    @Test
    void findAllByIdsTest() {
        //SETUP
        Set<UUID> userIds = new HashSet<>();
        userIds.add(UUID.randomUUID());
        userIds.add(UUID.randomUUID());
        userIds.add(UUID.randomUUID());
        List<User> users = new ArrayList<>();
        users.add(user);
        users.add(new User());
        users.add(new User());
        Mockito.when(repository.findAllById(userIds))
                .thenReturn(users);
        //TRY
        List<User> result = service.findAllByIds(userIds);
        Assertions.assertEquals(3, result.size());
        Assertions.assertEquals(user.getUsername(), result.get(0).getUsername());
    }

    @Test
    void findByIdWithInfoTest(){
        //SETUP
        Mockito.when(repository.findByIdWithInfo(id))
                .thenReturn(Optional.of(user));
        //CASE 1: ID IS VALID
        UserWithProjectInfoDto result = service.findByIdWithInfo(id);
        Assertions.assertEquals(user.getUsername(), result.getUsername());
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
                .thenReturn(Set.of(user));
        //TRY
        List<UserWithProjectInfoDto> result = service.findAllWithInfo();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(user.getUsername(), result.get(0).getUsername());
        Mockito.verify(repository).findAllWithInfo();
    }

    @Test
    void findAllWithInfoWithPagingTest() {
        //SETUP
        Page<User> page = Mockito.mock(Page.class);
        Mockito.when(repository.findAllWithInfoWithPaging(
                PageRequest.of(2, 3, Sort.by("createdAt"))))
                .thenReturn(page);
        Mockito.when(page.stream()).thenReturn(List.of(user).stream());
        //TRY
        List<UserWithProjectInfoDto> result = service.findAllWithInfoWithPaging(3, 2);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(user.getUsername(), result.get(0).getUsername());
        Mockito.verify(repository).findAllWithInfoWithPaging(
                PageRequest.of(2, 3, Sort.by("createdAt")));
    }

    @Test
    void findAllAccountStatus() {
        //SETUP
        List<String> statusList = new ArrayList<>();
        statusList.add(User.AccountStatus.ACTIVE.toString());
        statusList.add(User.AccountStatus.TEMPORARILY_BLOCKED.toString());
        statusList.add(User.AccountStatus.PERMANENTLY_BLOCKED.toString());
        //TRY
        List<String> result = service.findAllAccountStatus();
        Assertions.assertEquals(statusList.get(0), result.get(0));
        Assertions.assertEquals(statusList.get(1), result.get(1));
        Assertions.assertEquals(statusList.get(2), result.get(2));
    }

    @Test
    void findAllGendersTest() {
        //SETUP
        List<String> genderList = new ArrayList<>();
        genderList.add(User.Gender.MALE.toString());
        genderList.add(User.Gender.FEMALE.toString());
        //TRY
        List<String> result = service.findAllGenders();
        Assertions.assertEquals(genderList.get(0), result.get(0));
        Assertions.assertEquals(genderList.get(1), result.get(1));
    }

    @Test
    void findAllInsideProjectTest() {
        //SETUP
        Mockito.when(repository.findAllInsideProject(id))
                .thenReturn(Set.of(user));
        //TRY
        List<UserWithProjectInfoDto> result = service.findAllInsideProject(id);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(user.getUsername(), result.get(0).getUsername());
        Mockito.verify(repository).findAllInsideProject(id);
    }

    @Test
    void findAllInsideProjectWithTaskTest() {
        //SETUP
        Mockito.when(repository.findAllInsideProjectWithTask(id))
                .thenReturn(Set.of(user));
        Task task = Task.builder()
                .project(Project.builder().id(id).build())
                .build();
        user.setTasks(Set.of(task));
        //TRY
        List<UserWithTaskInfoDto> result = service.findAllInsideProjectWithTask(id);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(user.getUsername(), result.get(0).getUsername());
        Mockito.verify(repository).findAllInsideProjectWithTask(id);
    }


    @Test
    void findAllOutsideProjectTest() {
        //SETUP
        Mockito.when(repository.findAllOutsideProject(id))
                .thenReturn(Set.of(user));
        //TRY
        List<UserWithProjectInfoDto> result = service.findAllOutsideProject(id);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(user.getUsername(), result.get(0).getUsername());
        Mockito.verify(repository).findAllOutsideProject(id);
    }

    @Test
    void findAllLeaderRoleTest() {
        //SETUP
        Mockito.when(repository.findAllLeaderRole())
                .thenReturn(Set.of(user));
        //TRY
        List<UserDto> result = service.findAllLeaderRole();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(user.getUsername(), result.get(0).getUsername());
        Mockito.verify(repository).findAllLeaderRole();
    }

    @Test
    void updateRolesTest() {
        //SETUP update from manager => admin
        Role adminRole = Role.builder()
                .id(UUID.randomUUID())
                .name("admin")
                .users(new HashSet<>())
                .build();
        Set<User> managers = new HashSet<>();
        managers.add(user);
        Role managerRole = Role.builder()
                .id(UUID.randomUUID())
                .name("manager")
                .users(managers)
                .build();
        List<Role> roleList = new ArrayList<>();
        roleList.add(adminRole);
        roleList.add(managerRole);
        Mockito.when(roleRepository.findAll())
                .thenReturn(roleList);
        Set<Role> userRoles = new HashSet<>();
        userRoles.add(managerRole);
        user.setRoles(userRoles);
        user.setId(id);
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(user));
        Mockito.when(roleService.findAllByIds(Set.of(adminRole.getId())))
                .thenReturn(List.of(adminRole));
        //CASE 1: VALID ID
        UserWithProjectInfoDto result = service.updateRoles(user.getId(), Set.of(adminRole.getId()));
        Assertions.assertEquals(1, result.getRoles().size());
        Assertions.assertEquals(adminRole.getName(), result.getRoles().iterator().next().getName());
        Assertions.assertTrue(managerRole.getUsers().isEmpty());
        Mockito.verify(repository).findById(id);
        Mockito.verify(roleRepository).findAll();
        Mockito.verify(roleService).findAllByIds(Set.of(adminRole.getId()));
        //CASE 2: INVALID ID
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.updateRoles(user.getId(), Set.of(adminRole.getId())));
    }

    @Test
    void saveTest() {
        //SETUP
        User savedUser = User.builder()
                .username("savedUser")
                .build();
        ArgumentCaptor<User> captor = ArgumentCaptor.forClass(User.class);
        Mockito.when(repository.save(Mockito.any(User.class)))
                .thenReturn(savedUser);
        user.setPassword("oldPassword");
        userDto.setPassword("newPassword");
        //TRY
        UserDto result = service.save(userDto);
        Assertions.assertEquals(savedUser.getUsername(), result.getUsername());
        Mockito.verify(repository).save(captor.capture());
        User modifiedUser = captor.getValue();
        Assertions.assertTrue(passwordEncoder.matches("newPassword",
                modifiedUser.getPassword()));
    }

    @Test
    void updateTest() {
        //SETUP => UPDATE DON'T CHANGE PASSWORD
        user.setPassword(passwordEncoder.encode("oldPassword"));
        user.setId(id);
        userDto.setPassword("newPassword");
        userDto.setId(id);
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.of(user));
        //CASE 1: VALID ID
        UserDto result = service.update(userDto);
        Assertions.assertEquals(userDto.getUsername(), result.getUsername());
        Assertions.assertTrue(passwordEncoder.matches("oldPassword",
                result.getPassword()));
        //CASE 2: INVALID ID
        Mockito.when(repository.findById(id))
                .thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.update(userDto));
    }

}
