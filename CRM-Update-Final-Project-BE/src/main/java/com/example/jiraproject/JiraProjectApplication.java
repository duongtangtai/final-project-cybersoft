package com.example.jiraproject;

import com.example.jiraproject.comment.model.Comment;
import com.example.jiraproject.comment.repository.CommentRepository;
import com.example.jiraproject.comment.service.CommentService;
import com.example.jiraproject.common.model.BaseEntity;
import com.example.jiraproject.common.util.ApiUtil;
import com.example.jiraproject.file.service.FileService;
import com.example.jiraproject.notification.model.Notification;
import com.example.jiraproject.notification.repository.NotificationRepository;
import com.example.jiraproject.notification.service.NotificationService;
import com.example.jiraproject.operation.model.Operation;
import com.example.jiraproject.operation.repository.OperationRepository;
import com.example.jiraproject.operation.service.OperationService;
import com.example.jiraproject.project.model.Project;
import com.example.jiraproject.project.repository.ProjectRepository;
import com.example.jiraproject.project.service.ProjectService;
import com.example.jiraproject.role.model.Role;
import com.example.jiraproject.role.repository.RoleRepository;
import com.example.jiraproject.role.service.RoleService;
import com.example.jiraproject.task.model.Task;
import com.example.jiraproject.task.repository.TaskRepository;
import com.example.jiraproject.task.service.TaskService;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.repository.UserRepository;
import com.example.jiraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.time.LocalDate;
import java.util.Arrays;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@SpringBootApplication
@RequiredArgsConstructor
public class JiraProjectApplication implements CommandLineRunner {
    private final RoleRepository roleRepository;
    private final OperationRepository operationRepository;
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
        Role role1 = Role
                .builder()
                .name("ADMIN")
                .code("AD")
                .description("Quyền của admin")
                .build();
        Role role2 = Role
                .builder()
                .name("MANAGER")
                .code("MGR")
                .description("Quyền của giám đốc")
                .build();
        Role role3 = Role
                .builder()
                .name("LEADER")
                .code("LEAD")
                .description("Quyền của trưởng nhóm")
                .build();
        Role role4 = Role
                .builder()
                .name("EMPLOYEE")
                .code("EMP")
                .description("Quyền của nhân viên")
                .build();
        roleRepository.save(role1);
        roleRepository.save(role2);
        roleRepository.save(role3);
        roleRepository.save(role4);

        //-------------------------AUTHORIZATION-----------------------------------
        //----------ADMIN-------------
        // + can fetch, update and remove in OPERATION API and ROLE API and USER API
        //----------MANAGER-------------
        // + can update and remove in PROJECT API
        //-----------LEADER--------------
        // + can fetch in PROJECT API
        // + can update and remove in TASK API
        // + can remove in COMMENT API
        //-----------EMPLOYEE--------------
        // + can fetch in TASK API
        // + can fetch, update in COMMENT API
        // + can fetch, update, remove in NOTIFICATION API




        //--------------------------ADD OPERATIONS-------------------------------
        //----------OPERATION API-----------
        Operation operation1 = Operation
                .builder()
                .name(ApiUtil.OPERATION)
                .description("Lấy thông tin chức năng")
                .type(Operation.Type.FETCH)
                .build();
        Operation operation2 = Operation
                .builder()
                .name(ApiUtil.OPERATION)
                .description("Lưu thông tin chức năng")
                .type(Operation.Type.SAVE_OR_UPDATE)
                .build();
        Operation operation3 = Operation
                .builder()
                .name(ApiUtil.OPERATION)
                .description("Xóa chức năng")
                .type(Operation.Type.REMOVE)
                .build();
        saveOperation(operation1, operation2, operation3);
        //add to ROLE ADMIN
        addOperationsToRole(role1, operation1, operation2, operation3);
        //----------ROLE API-----------
        operation1 = Operation
                .builder()
                .name(ApiUtil.ROLE)
                .description("Lấy thông tin quyền")
                .type(Operation.Type.FETCH)
                .build();
        operation2 = Operation
                .builder()
                .name(ApiUtil.ROLE)
                .description("Lưu thông tin quyền")
                .type(Operation.Type.SAVE_OR_UPDATE)
                .build();
        operation3 = Operation
                .builder()
                .name(ApiUtil.ROLE)
                .description("Xóa quyền")
                .type(Operation.Type.REMOVE)
                .build();
        saveOperation(operation1, operation2, operation3);
        //add to ROLE ADMIN
        addOperationsToRole(role1, operation1, operation2, operation3);
        //----------USER API-----------
        operation1 = Operation
                .builder()
                .name(ApiUtil.USER)
                .description("Lấy thông tin người dùng")
                .type(Operation.Type.FETCH)
                .build();
        operation2 = Operation
                .builder()
                .name(ApiUtil.USER)
                .description("Lưu thông tin người dùng")
                .type(Operation.Type.SAVE_OR_UPDATE)
                .build();
        operation3 = Operation
                .builder()
                .name(ApiUtil.USER)
                .description("Xóa người dùng")
                .type(Operation.Type.REMOVE)
                .build();
        saveOperation(operation1, operation2, operation3);
        //add to ROLE ADMIN
        addOperationsToRole(role1, operation1, operation2, operation3);
        //----------PROJECT API-----------
        operation1 = Operation
                .builder()
                .name(ApiUtil.PROJECT)
                .description("Lấy thông tin dự án")
                .type(Operation.Type.FETCH)
                .build();
        operation2 = Operation
                .builder()
                .name(ApiUtil.PROJECT)
                .description("Lưu thông tin dự án")
                .type(Operation.Type.SAVE_OR_UPDATE)
                .build();
        operation3 = Operation
                .builder()
                .name(ApiUtil.PROJECT)
                .description("Xóa dự án")
                .type(Operation.Type.REMOVE)
                .build();
        saveOperation(operation1, operation2, operation3);
        //add to ROLE MANAGER, LEADER
        addOperationsToRole(role2, operation2, operation3);
        addOperationsToRole(role3, operation1);
        //----------TASK API-----------
        operation1 = Operation
                .builder()
                .name(ApiUtil.TASK)
                .description("Lấy thông tin công việc")
                .type(Operation.Type.FETCH)
                .build();
        operation2 = Operation
                .builder()
                .name(ApiUtil.TASK)
                .description("Lưu thông tin công việc")
                .type(Operation.Type.SAVE_OR_UPDATE)
                .build();
        operation3 = Operation
                .builder()
                .name(ApiUtil.TASK)
                .description("Xóa công việc")
                .type(Operation.Type.REMOVE)
                .build();
        saveOperation(operation1, operation2, operation3);
        //add to ROLE LEADER and EMPLOYEE
        addOperationsToRole(role3, operation2, operation3);
        addOperationsToRole(role4, operation1);
        //----------COMMENT API-----------
        operation1 = Operation
                .builder()
                .name(ApiUtil.COMMENT)
                .description("Lấy thông tin bình luận")
                .type(Operation.Type.FETCH)
                .build();
        operation2 = Operation
                .builder()
                .name(ApiUtil.COMMENT)
                .description("Lưu thông tin bình luận")
                .type(Operation.Type.SAVE_OR_UPDATE)
                .build();
        operation3 = Operation
                .builder()
                .name(ApiUtil.COMMENT)
                .description("Xóa bình luận")
                .type(Operation.Type.REMOVE)
                .build();
        saveOperation(operation1, operation2, operation3);
        //add to ROLE LEADER, EMPLOYEE
        addOperationsToRole(role3, operation3);
        addOperationsToRole(role4, operation1, operation2);
        //----------NOTIFICATION API-----------
        operation1 = Operation
                .builder()
                .name(ApiUtil.NOTIFICATION)
                .description("Lấy thông tin thông báo")
                .type(Operation.Type.FETCH)
                .build();
        operation2 = Operation
                .builder()
                .name(ApiUtil.NOTIFICATION)
                .description("Lưu thông tin thông báo")
                .type(Operation.Type.SAVE_OR_UPDATE)
                .build();
        operation3 = Operation
                .builder()
                .name(ApiUtil.NOTIFICATION)
                .description("Xóa thông báo")
                .type(Operation.Type.REMOVE)
                .build();
        saveOperation(operation1, operation2, operation3);
        //add to ROLE EMPLOYEE
        addOperationsToRole(role4, operation1, operation2, operation3);
        //----------FILE API-----------
//        operation1 = Operation
//                .builder()
//                .name(ApiUtil.FILE)
//                .description("Lấy thông tin file")
//                .type(Operation.Type.FETCH)
//                .build();
        operation2 = Operation
                .builder()
                .name(ApiUtil.FILE)
                .description("Lưu thông tin file")
                .type(Operation.Type.SAVE_OR_UPDATE)
                .build();
        operation3 = Operation
                .builder()
                .name(ApiUtil.FILE)
                .description("Xóa file")
                .type(Operation.Type.REMOVE)
                .build();
        saveOperation(operation1, operation2, operation3);
        //add to ROLE ADMIN, EMPLOYEE
        addOperationsToRole(role1, operation3);
        addOperationsToRole(role4, operation2);

        //-----------------------------ADD USERS------------------------------
        User user1 = User.builder()
                .username("admin")
                .password(passwordEncoder.encode("admin"))
                .firstName("ADMIN")
                .lastName("ADMIN")
                .avatar("avatar1 url")
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
                .avatar("avatar2 url")
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
                .avatar("avatar3 url")
                .email("tranmynhi@gmail.com")
                .facebookUrl("facebook3 url")
                .occupation("Business")
                .department("WORKING DEPARTMENT")
                .hobbies("Pet lover")
                .build();
        User user4 = User.builder()
                .username("lexuanhieu")
                .password(passwordEncoder.encode("12345"))
                .firstName("Hiếu")
                .lastName("Lê")
                .avatar("avatar4 url")
                .email("lexuanhieu@gmail.com")
                .facebookUrl("facebook4 url")
                .occupation("Java developer")
                .department("IT DEPARTMENT")
                .hobbies("Doing some stuff")
                .build();
        user1 = userRepository.save(user1);
        user2 = userRepository.save(user2);
        user3 = userRepository.save(user3);
        user4 = userRepository.save(user4);

        //ADD ROLES TO ADMIN
        addRolesToUser(user1, role1, role2, role3, role4);
        //ADD ROLES TO MANAGER
        addRolesToUser(user2, role2, role3, role4);
        //ADD ROLES TO LEADER
        addRolesToUser(user3, role3, role4);
        //ADD ROLES TO EMPLOYEE
        addRolesToUser(user4, role4);

        //ADD PROJECT
        Project project1 = Project.builder()
                .name("Dự án khởi điểm")
                .description("Dự án đầu tiên trong tháng")
                .creator(user1)
                .leader(user2)
                .build();
        Project project2 = Project.builder()
                .name("Dự án thi công")
                .description("Dự án thứ hai trong tháng")
                .creator(user1)
                .leader(user3)
                .build();
        Project project3 = Project.builder()
                .name("Dự án bảo dưỡng")
                .description("Dự án thứ ba trong tháng")
                .creator(user1)
                .leader(user3)
                .build();
        projectRepository.save(project1);
        projectRepository.save(project2);
        projectRepository.save(project3);

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
                .task(task2)
                .responseTo(comment3)
                .build();
        commentRepository.save(comment1);
        commentRepository.save(comment2);
        commentRepository.save(comment3);
        commentRepository.save(comment4);

        //ADD NOTIFICATIONS
        Notification notification1 = Notification.builder()
                .sender(user1)
                .receiver(user2)
                .description(user1.getFirstName() + " vừa thêm bạn vào dự án mới")
                .build();
        Notification notification2 = Notification.builder()
                .sender(user2)
                .receiver(user1)
                .description(user2.getFirstName() + " rủ bạn tối nay đi chơi")
                .build();
        Notification notification3 = Notification.builder()
                .sender(user3)
                .receiver(user2)
                .description(user3.getFirstName() + " đã thêm " + user2.getFirstName() + " vào danh sách bạn thân")
                .build();
        notificationRepository.save(notification1);
        notificationRepository.save(notification2);
        notificationRepository.save(notification3);
    }
    private void saveOperation(Operation...operation) {
        Arrays.stream(operation).forEach(operationRepository::save);
    }
    private void addOperationsToRole(Role role, Operation...operation) {
        Set<UUID> operationIds = Arrays.stream(operation).map(BaseEntity::getId).collect(Collectors.toSet());;
        roleService.addOperations(role.getId(), operationIds);
    }
    private void addRolesToUser(User user, Role...role) {
        Set<UUID> roleIds = Arrays.stream(role).map(BaseEntity::getId).collect(Collectors.toSet());
        userService.addRoles(user.getId(), roleIds);
    }
}
