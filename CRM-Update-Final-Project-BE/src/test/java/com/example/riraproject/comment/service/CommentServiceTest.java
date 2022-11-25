package com.example.riraproject.comment.service;

import com.example.riraproject.comment.dto.CommentDto;
import com.example.riraproject.comment.dto.CommentWithInfoDto;
import com.example.riraproject.comment.model.Comment;
import com.example.riraproject.comment.repository.CommentRepository;
import com.example.riraproject.task.model.Task;
import com.example.riraproject.task.service.TaskService;
import com.example.riraproject.user.dto.UserDto;
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
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

@ExtendWith(MockitoExtension.class)
class CommentServiceTest {
    @Mock
    CommentRepository repository;
    @Mock
    ModelMapper mapper;
    @Mock
    MessageSource messageSource;
    @InjectMocks
    CommentServiceImpl service;
    @Mock
    private Comment model;
    @Mock
    private CommentDto dto;
    @Mock
    private CommentWithInfoDto dtoWithInfo;

    private final UUID id = UUID.randomUUID();
    @Mock
    private TaskService taskService;
    @Mock
    private UserService userService;

    @Test
    void getRepositoryAndMapperTest() {
        Assertions.assertEquals(repository, service.getRepository());
        Assertions.assertEquals(mapper, service.getMapper());
    }

    @Test
    void findByIdWithInfoTest() {
        //SETUP
        Mockito.when(repository.findByIdWithInfo(id)).thenReturn(Optional.of(model));
        Mockito.when(mapper.map(model, CommentWithInfoDto.class)).thenReturn(dtoWithInfo);
        //TRY
        //CASE 1 : ID IS VALID
        Assertions.assertEquals(dtoWithInfo, service.findByIdWithInfo(id));
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
        Mockito.when(mapper.map(model, CommentWithInfoDto.class)).thenReturn(dtoWithInfo);
        //TRY
        Assertions.assertEquals(List.of(dtoWithInfo), service.findAllWithInfo());
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
        Mockito.when(mapper.map(model, CommentWithInfoDto.class)).thenReturn(dtoWithInfo);
        //TRY
        Assertions.assertEquals(List.of(dtoWithInfo), service.findAllWithInfoWithPaging(3, 2));
        Mockito.verify(repository).findAllWithInfoWithPaging(pageable);
    }

    @Test
    void findAllWithInfoByTaskIdTest() {
        //SETUP
        UUID taskId = UUID.randomUUID();
        Task task = Mockito.mock(Task.class);
        UserDto user = Mockito.mock(UserDto.class);
        Mockito.when(taskService.findTaskById(taskId)).thenReturn(task);
        Mockito.when(repository.findAllWithInfoByTaskId(task.getId()))
                .thenReturn(Set.of(model));
        Mockito.when(mapper.map(model, CommentWithInfoDto.class))
                .thenReturn(dtoWithInfo);
        Mockito.when(dtoWithInfo.getWriter()).thenReturn(user);
        Mockito.doNothing().when(user).setPassword(null);
        //TRY
        Assertions.assertEquals(List.of(dtoWithInfo),
                service.findAllWithInfoByTaskId(taskId));
        Mockito.verify(repository).findAllWithInfoByTaskId(task.getId());
    }

    @Test
    void saveCommentTest() {
        //SETUP
        Task task = Mockito.mock(Task.class);
        User user = Mockito.mock(User.class);
        Mockito.when(taskService.findTaskById(dto.getTaskId())).thenReturn(task);
        Mockito.when(userService.findUserById(dto.getWriterId())).thenReturn(user);
        Mockito.when(mapper.map(dto, Comment.class)).thenReturn(model);
        Mockito.doNothing().when(model).setTask(task);
        Mockito.doNothing().when(model).setWriter(user);
        Mockito.when(repository.save(model)).thenReturn(model);
        Mockito.when(mapper.map(model, CommentWithInfoDto.class)).thenReturn(dtoWithInfo);
        //TRY
        //CASE 1: ResponseToId = NULL
        Mockito.when(dto.getResponseToId()).thenReturn(null);
        //CHECK:
        Assertions.assertEquals(dtoWithInfo, service.saveComment(dto));
        Mockito.verify(taskService).findTaskById(dto.getTaskId());
        Mockito.verify(userService).findUserById(dto.getWriterId());
        //CASE 2: ResponseToId != NULL and CommentID is valid
        UUID responseToId = UUID.randomUUID();
        Comment respondedCmt = Mockito.mock(Comment.class);
        Mockito.when(dto.getResponseToId()).thenReturn(responseToId);
        Mockito.when(repository.findCommentById(dto.getResponseToId()))
                .thenReturn(Optional.of(respondedCmt));
        Mockito.doNothing().when(model).setResponseTo(respondedCmt);
        //CHECK:
        Assertions.assertEquals(dtoWithInfo, service.saveComment(dto));
        Mockito.verify(taskService, Mockito.times(2))
                .findTaskById(dto.getTaskId());
        Mockito.verify(userService, Mockito.times(2))
                .findUserById(dto.getWriterId());
        //CASE 3: ResponseToId != NULL and CommentID is invalid
        Mockito.when(repository.findCommentById(dto.getResponseToId()))
                .thenReturn(Optional.empty());
        //CHECK:
        Assertions.assertThrowsExactly(ValidationException.class,
                () -> service.saveComment(dto));
    }
}
