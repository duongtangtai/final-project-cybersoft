package com.example.riraproject.comment.service;

import com.example.riraproject.comment.dto.CommentDto;
import com.example.riraproject.comment.dto.CommentWithInfoDto;
import com.example.riraproject.comment.model.Comment;
import com.example.riraproject.comment.repository.CommentRepository;
import com.example.riraproject.task.model.Task;
import com.example.riraproject.task.service.TaskService;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock private CommentRepository repository;
    @Autowired private ModelMapper mapper;
    @Autowired private MessageSource messageSource;
    @Mock private TaskService taskService;
    @Mock private UserService userService;
    private CommentServiceImpl service;
    private Comment model;
    private CommentDto dto;
    private final UUID id = UUID.randomUUID();

    @BeforeEach
    void init() {
        service = new CommentServiceImpl(repository, mapper, taskService, userService, messageSource);
        model = Comment.builder()
                .description("this is a model")
                .build();
        dto = CommentDto.builder()
                .description("this is a dto")
                .build();
    }

    @Test
    void getRepositoryAndMapperTest() {
        Assertions.assertEquals(repository, service.getRepository());
        Assertions.assertEquals(mapper, service.getMapper());
    }

    @Test
    void findByIdWithInfoTest() {
        //SETUP
        Mockito.when(repository.findByIdWithInfo(id)).thenReturn(Optional.of(model));
        //TRY
        //CASE 1 : ID IS VALID
        CommentWithInfoDto result = service.findByIdWithInfo(id);
        Assertions.assertEquals(model.getDescription(), result.getDescription());
        Mockito.verify(repository).findByIdWithInfo(id);
        //CASE 2 : ID IS INVALID
        Mockito.when(repository.findByIdWithInfo(id)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class, () -> service.findByIdWithInfo(id));
        Mockito.verify(repository, Mockito.times(2)).findByIdWithInfo(id);
    }

    @Test
    void findAllWithInfoTest() {
        //SETUP
        Mockito.when(repository.findAllWithInfo()).thenReturn(Set.of(model));
        //TRY
        List<CommentWithInfoDto> result = service.findAllWithInfo();
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(model.getDescription(), result.get(0).getDescription());
        Mockito.verify(repository).findAllWithInfo();
    }

    @Test
    void findAllWithInfoWithPagingTest() {
        //SETUP
        Pageable pageable = PageRequest.of(2, 3, Sort.by("createdAt"));
        Page page = Mockito.mock(Page.class);
        Mockito.when(repository.findAllWithInfoWithPaging(pageable))
                .thenReturn(page);
        Mockito.when(page.stream())
                .thenReturn(List.of(model).stream());
        //TRY
        List<CommentWithInfoDto> result = service.findAllWithInfoWithPaging(3, 2);
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(model.getDescription(), result.get(0).getDescription());
        Mockito.verify(repository).findAllWithInfoWithPaging(pageable);
    }

    @Test
    void findAllWithInfoByTaskIdTest() {
        //SETUP
        Task task = Task.builder()
                .id(UUID.randomUUID())
                .build();
        //set writer with username and password => password should be null after the transaction
        User writer = User.builder()
                .username("username")
                .password("password")
                .build();
        model.setWriter(writer);
        Mockito.when(taskService.findTaskById(task.getId())).thenReturn(task);
        Mockito.when(repository.findAllWithInfoByTaskId(task.getId()))
                .thenReturn(Set.of(model));
        //TRY
        List<CommentWithInfoDto> result = service.findAllWithInfoByTaskId(task.getId());
        Assertions.assertEquals(1, result.size());
        Assertions.assertEquals(model.getDescription(), result.get(0).getDescription());
        Assertions.assertEquals(writer.getUsername(), result.get(0).getWriter().getUsername());
        Assertions.assertNull(result.get(0).getWriter().getPassword());
    }

    @Test
    void saveCommentTest() {
        //SETUP
        Task task = Mockito.mock(Task.class);
        User user = Mockito.mock(User.class);
        Mockito.when(taskService.findTaskById(dto.getTaskId())).thenReturn(task);
        Mockito.when(userService.findUserById(dto.getWriterId())).thenReturn(user);
        Mockito.when(repository.save(Mockito.any(Comment.class))).thenReturn(model);
        dto.setId(UUID.randomUUID());
        //TRY
        //CASE 1: ResponseToId = NULL
        CommentWithInfoDto result = service.saveComment(dto);
        Assertions.assertEquals(dto.getDescription(), result.getDescription());
        Mockito.verify(taskService).findTaskById(dto.getTaskId());
        Mockito.verify(userService).findUserById(dto.getWriterId());
        //CASE 2: ResponseToId != NULL and CommentID is valid
        Comment respondedCmt = Comment.builder()
                .id(UUID.randomUUID())
                .build();
        dto.setResponseToId(respondedCmt.getId());
        Mockito.when(repository.findCommentById(respondedCmt.getId()))
                .thenReturn(Optional.of(respondedCmt));
        CommentWithInfoDto result2 = service.saveComment(dto);
        Assertions.assertEquals(dto.getDescription(), result2.getDescription());
        Assertions.assertEquals(respondedCmt.getId(), result2.getResponseToId());
        Mockito.verify(taskService, Mockito.times(2))
                .findTaskById(dto.getTaskId());
        Mockito.verify(userService, Mockito.times(2))
                .findUserById(dto.getWriterId());
        //CASE 3: ResponseToId != NULL and CommentID is invalid
        Mockito.when(repository.findCommentById(respondedCmt.getId()))
                .thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.saveComment(dto));
    }
}
