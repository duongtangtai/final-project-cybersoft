package com.example.riraproject.comment.service;

import com.example.riraproject.comment.dto.CommentDto;
import com.example.riraproject.comment.dto.CommentWithInfoDto;
import com.example.riraproject.comment.model.Comment;
import com.example.riraproject.comment.repository.CommentRepository;
import com.example.riraproject.common.service.GenericService;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.task.model.Task;
import com.example.riraproject.task.service.TaskService;
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
import java.util.List;
import java.util.UUID;

public interface CommentService extends GenericService<Comment, CommentDto, UUID> {
    CommentWithInfoDto findByIdWithInfo(UUID id);
    List<CommentWithInfoDto> findAllWithInfo();
    List<CommentWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex);
    List<CommentWithInfoDto> findAllWithInfoByTaskId(UUID taskId);
    CommentWithInfoDto saveComment(CommentDto commentDto);
}
@Service
@Transactional
@RequiredArgsConstructor
class CommentServiceImpl implements CommentService {
    private final CommentRepository repository;
    private final ModelMapper mapper;
    private final TaskService taskService;
    private final UserService userService;
    private final MessageSource messageSource;
    private static final String UUID_NOT_FOUND = "comment.id.not-found";

    @Override
    public JpaRepository<Comment, UUID> getRepository() {
        return this.repository;
    }

    @Override
    public ModelMapper getMapper() {
        return this.mapper;
    }

    @Transactional(readOnly = true)
    @Override
    public CommentWithInfoDto findByIdWithInfo(UUID id) {
        Comment comment = repository.findByIdWithInfo(id)
                .orElseThrow(() -> new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        return mapper.map(comment, CommentWithInfoDto.class);
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentWithInfoDto> findAllWithInfo() {
        return repository.findAllWithInfo().stream()
                .map(model -> mapper.map(model, CommentWithInfoDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex) {
        return repository.findAllWithInfoWithPaging(PageRequest.of(pageIndex, size, Sort.by("createdAt")))
                .stream()
                .map(model -> mapper.map(model, CommentWithInfoDto.class))
                .toList();
    }

    @Transactional(readOnly = true)
    @Override
    public List<CommentWithInfoDto> findAllWithInfoByTaskId(UUID taskId) {
        Task task = taskService.findTaskById(taskId);
        List<CommentWithInfoDto> comments = repository.findAllWithInfoByTaskId(task.getId())
                .stream()
                .map(model ->  mapper.map(model, CommentWithInfoDto.class))
                .toList();
        comments.forEach(model -> model.getWriter().setPassword(null)); //set password null
        return comments;
    }

    @Override
    public CommentWithInfoDto saveComment(CommentDto commentDto) {
        Task task = taskService.findTaskById(commentDto.getTaskId());
        User user = userService.findUserById(commentDto.getWriterId());
        Comment comment = mapper.map(commentDto, Comment.class);
        if (commentDto.getResponseToId() != null) {
            Comment respondedCmt = repository.findCommentById(commentDto.getResponseToId())
                    .orElseThrow(() -> new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
            comment.setResponseTo(respondedCmt);
        }
        comment.setTask(task);
        comment.setWriter(user);
        repository.save(comment);
        return mapper.map(comment, CommentWithInfoDto.class);
    }
}
