package com.example.jiraproject;

import com.example.jiraproject.comment.model.Comment;
import com.example.jiraproject.comment.repository.CommentRepository;
import com.example.jiraproject.comment.service.CommentService;
import com.example.jiraproject.common.model.BaseEntity;
import com.example.jiraproject.file.service.FileService;
import com.example.jiraproject.notification.model.Notification;
import com.example.jiraproject.notification.repository.NotificationRepository;
import com.example.jiraproject.notification.service.NotificationService;
import com.example.jiraproject.project.model.Project;
import com.example.jiraproject.project.repository.ProjectRepository;
import com.example.jiraproject.project.service.ProjectService;
import com.example.jiraproject.role.model.Role;
import com.example.jiraproject.role.repository.RoleRepository;
import com.example.jiraproject.role.service.RoleService;
import com.example.jiraproject.role.util.RoleUtil;
import com.example.jiraproject.task.model.Task;
import com.example.jiraproject.task.repository.TaskRepository;
import com.example.jiraproject.task.service.TaskService;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.repository.UserRepository;
import com.example.jiraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.OperationService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootApplication
@RequiredArgsConstructor
public class JiraProjectApplication implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final RoleService roleService;
    private final OperationService operationService;
    private final UserRepository userRepository;
    private final UserService userService;
    private final ProjectRepository projectRepository;
    private final ProjectService projectService;
    private final TaskRepository taskRepository;
    private final TaskService taskservice;
    private final CommentRepository commentRepository;
    private final CommentService commentService;
    private final NotificationRepository notificationRepository;
    private final NotificationService notificationService;
    private final BCryptPasswordEncoder passwordEncoder;
    private final FileService fileService;
    public static void main(String[] args) {
        SpringApplication.run(JiraProjectApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        //4 ROLES: ADMIN - MANAGER -
        Role admin = Role
                .builder()
                .name("ADMIN")
                .code(RoleUtil.ADMIN)
                .description("Quyền của admin")
                .build();
        Role manager = Role
                .builder()
                .name("MANAGER")
                .code(RoleUtil.MANAGER)
                .description("Quyền của giám đốc")
                .build();
        Role leader = Role
                .builder()
                .name("LEADER")
                .code(RoleUtil.LEADER)
                .description("Quyền của trưởng nhóm")
                .build();
        Role employee = Role
                .builder()
                .name("EMPLOYEE")
                .code(RoleUtil.EMPLOYEE)
                .description("Quyền của nhân viên")
                .build();
        roleRepository.save(admin);
        roleRepository.save(manager);
        roleRepository.save(leader);
        roleRepository.save(employee);

        //-------------------------AUTHORIZATION-----------------------------------
        //----------ADMIN-------------
        // + can fetch, update and remove in ROLE API
        // + can update, remove in USER API
        //----------MANAGER-------------
        // + can update and remove in PROJECT API
        //-----------LEADER--------------
        // + can fetch in PROJECT API
        // + can update and remove in TASK API
        // + can remove in COMMENT API
        // + can fetch in USER API
        //-----------EMPLOYEE--------------
        // + can fetch in TASK API
        // + can fetch, update in COMMENT API
        // + can fetch, update, remove in NOTIFICATION API

        //-----------------------------ADD USERS------------------------------
        User user1 = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .firstName("ADMIN")
                .lastName("ADMIN")
                .gender(User.Gender.MALE)
                .email("admin@gmail.com")
                .facebookUrl("facebook1 url")
                .occupation("administrator")
                .department("MANAGING DEPARTMENT")
                .hobbies("Listen to music, watching TV")
                .build();
        User user2 = User.builder()
                .username("duongtangtai")
                .password(passwordEncoder.encode("12345"))
                .firstName("Tài")
                .lastName("Dương")
                .gender(User.Gender.MALE)
                .email("duongtangtai@gmail.com")
                .facebookUrl("facebook2 url")
                .occupation("Java developer")
                .department("IT DEPARTMENT")
                .hobbies("Doing some kinda magic")
                .build();
        User user3 = User.builder()
                .username("tranmynhi")
                .password(passwordEncoder.encode("12345"))
                .firstName("Nhi")
                .lastName("Trần")
                .gender(User.Gender.FEMALE)
                .email("tranmynhi@gmail.com")
                .facebookUrl("facebook3 url")
                .occupation("Business")
                .department("WORKING DEPARTMENT")
                .hobbies("Pet lover")
                .accountStatus(User.AccountStatus.ACTIVE)
                .build();
        User user4 = User.builder()
                .username("lexuanhieu")
                .password(passwordEncoder.encode("12345"))
                .firstName("Hiếu")
                .lastName("Lê")
                .gender(User.Gender.MALE)
                .email("lexuanhieu@gmail.com")
                .facebookUrl("facebook4 url")
                .occupation("Java developer")
                .department("IT DEPARTMENT")
                .hobbies("Doing some stuff")
                .accountStatus(User.AccountStatus.ACTIVE)
                .build();
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);
        user4 = userRepository.save(user4);

        //ADD ROLES TO ADMIN
        addRolesToUser(user1, admin, manager, leader, employee);
        //ADD ROLES TO MANAGER
        addRolesToUser(user2, manager);
        //ADD ROLES TO LEADER
        addRolesToUser(user3, leader);
        //ADD ROLES TO EMPLOYEE
        addRolesToUser(user4, employee);

        //ADD PROJECT
        Project project1 = Project.builder()
                .name("Dự án khởi điểm")
                .description("Dự án đầu tiên trong tháng")
                .status(Project.Status.DONE)
                .symbol("default1")
                .creator(user1)
                .leader(user3)
                .build();
        Project project2 = Project.builder()
                .name("Dự án thi công")
                .description("Dự án thứ hai trong tháng")
                .creator(user1)
                .symbol("default1")
                .leader(user3)
                .build();
        Project project3 = Project.builder()
                .name("Dự án bảo dưỡng")
                .description("Dự án thứ ba trong tháng")
                .creator(user1)
                .symbol("default1")
                .leader(user3)
                .build();
        projectRepository.save(project1);
        projectRepository.save(project2);
        projectRepository.save(project3);

        //CREATE USERS TO ADD TO PROJECT
        User userLeader1 = User.builder()
                .username("leader1")
                .password(passwordEncoder.encode("12345"))
                .firstName("Leader1")
                .lastName("Leader1")
                .gender(User.Gender.MALE)
                .email("leader1@gmail.com")
                .facebookUrl("facebook url")
                .occupation("Technical Leader")
                .department("IT DEPARTMENT")
                .hobbies("Doing some stuff")
                .accountStatus(User.AccountStatus.ACTIVE)
                .build();
        User userStaff1 = User.builder()
                .username("staff1")
                .password(passwordEncoder.encode("12345"))
                .firstName("Staff1")
                .lastName("Staff1")
                .gender(User.Gender.MALE)
                .email("staff1@gmail.com")
                .facebookUrl("facebook url")
                .occupation("Technical Staff")
                .department("IT DEPARTMENT")
                .hobbies("Doing some stuff")
                .accountStatus(User.AccountStatus.ACTIVE)
                .build();
        User userStaff2 = User.builder()
                .username("staff2")
                .password(passwordEncoder.encode("12345"))
                .firstName("Staff2")
                .lastName("Staff2")
                .gender(User.Gender.MALE)
                .email("staff2@gmail.com")
                .facebookUrl("facebook url")
                .occupation("Technical Staff")
                .department("IT DEPARTMENT")
                .hobbies("Doing some stuff")
                .accountStatus(User.AccountStatus.ACTIVE)
                .build();
        User userStaff3 = User.builder()
                .username("staff3")
                .password(passwordEncoder.encode("12345"))
                .firstName("Staff3")
                .lastName("Staff3")
                .gender(User.Gender.FEMALE)
                .email("staff3@gmail.com")
                .facebookUrl("facebook url")
                .occupation("Technical Staff")
                .department("IT DEPARTMENT")
                .hobbies("Doing some stuff")
                .accountStatus(User.AccountStatus.ACTIVE)
                .build();
        User userStaff4 = User.builder()
                .username("staff4")
                .password(passwordEncoder.encode("12345"))
                .firstName("Staff4")
                .lastName("Staff4")
                .gender(User.Gender.FEMALE)
                .email("staff4@gmail.com")
                .facebookUrl("facebook url")
                .occupation("Technical Staff")
                .department("IT DEPARTMENT")
                .hobbies("Doing some stuff")
                .accountStatus(User.AccountStatus.ACTIVE)
                .build();
        userRepository.save(userLeader1);
        userRepository.save(userStaff1);
        userRepository.save(userStaff2);
        userRepository.save(userStaff3);
        userRepository.save(userStaff4);
        //Add staffs to projects
        addUsersToProject(project1, userStaff1, userStaff2);
        addUsersToProject(project2, userStaff3);
        //add Roles to users
        addRolesToUser(userLeader1, leader);
        addRolesToUser(userStaff1, employee);
        addRolesToUser(userStaff2, employee);
        addRolesToUser(userStaff3, employee);
        addRolesToUser(userStaff4, employee);

        // ADD TASKS
        Task task1 = Task.builder()
                .name("DA1 - Chuẩn bị cho dự án")
                .description("Review kế hoạch, team meeting")
                .startDateExpected(LocalDate.now())
                .endDateExpected(LocalDate.now())
                .startDateInFact(LocalDate.now())
                .endDateInFact(LocalDate.now())
                .status(Task.Status.UNASSIGNED)
                .project(project1)
                .reporter(user1)
                .build();
        Task task2 = Task.builder()
                .name("DA1 - Sắp xếp nhân sự cho dự án")
                .description("Điều phối nhân sự, phân chia trách nhiệm")
                .startDateExpected(LocalDate.now())
                .endDateExpected(LocalDate.now())
                .startDateInFact(LocalDate.now())
                .endDateInFact(LocalDate.now())
                .status(Task.Status.STARTED)
                .project(project1)
                .reporter(user2)
                .build();
        Task task3 = Task.builder()
                .name("DA2 - Duy trì tiến độ")
                .description("Bảo trì, bảo dưỡng")
                .startDateExpected(LocalDate.now())
                .endDateExpected(LocalDate.now())
                .startDateInFact(LocalDate.now())
                .endDateInFact(LocalDate.now())
                .status(Task.Status.COMPLETED)
                .project(project2)
                .reporter(user3)
                .build();
        taskRepository.save(task1);
        taskRepository.save(task2);
        taskRepository.save(task3);

        //ADD COMMENTS
        Comment comment1 = Comment.builder()
                .description("Công nhận bạn làm nhanh thiệt")
                .writer(user1)
                .task(task1)
                .build();
        Comment comment2 = Comment.builder()
                .description("Hay quá, làm tiếp phần kia đi")
                .writer(user2)
                .task(task1)
                .build();
        Comment comment3 = Comment.builder()
                .description("Kì này lên lương chắc rồi bro")
                .writer(user3)
                .task(task2)
                .build();
        Comment comment4 = Comment.builder()
                .description("Sao biết hay vậy?")
                .writer(user1)
                .responseTo(comment3)
                .task(task2)
                .build();
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);
        //response one comment to another

        //ADD NOTIFICATIONS
        Notification notification1 = Notification.builder()
                .sender(user2)
                .receiver(user1)
                .description(user2.getFirstName() + " vừa thêm bạn vào dự án mới")
                .build();
        Notification notification2 = Notification.builder()
                .sender(user2)
                .receiver(user1)
                .description(user2.getFirstName() + " rủ bạn tối nay đi chơi")
                .build();
        Notification notification3 = Notification.builder()
                .sender(user3)
                .receiver(user1)
                .description(user3.getFirstName() + " đã thêm " + user2.getFirstName() + " vào danh sách bạn thân")
                .status(Notification.Status.READ)
                .build();
        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);
    }
    private void addRolesToUser(User user, Role...role) {
        Set<UUID> roleIds = Arrays.stream(role).map(BaseEntity::getId).collect(Collectors.toSet());
        userService.updateRoles(user.getId(), roleIds);
    }
    private void addUsersToProject(Project project, User...user) {
        Set<UUID> userIds = Arrays.stream(user).map(BaseEntity::getId).collect(Collectors.toSet());
        projectService.addUsers(project.getId(), userIds);
    }
}
