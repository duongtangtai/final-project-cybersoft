package com.example.jiraproject.comment.service;

import com.example.jiraproject.comment.dto.CommentDto;
import com.example.jiraproject.comment.dto.CommentWithInfoDto;
import com.example.jiraproject.comment.model.Comment;
import com.example.jiraproject.comment.repository.CommentRepository;
import com.example.jiraproject.common.service.GenericService;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.task.model.Task;
import com.example.jiraproject.task.service.TaskService;
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
import java.util.List;
import java.util.UUID;

public interface CommentService extends GenericService<Comment, CommentDto, UUID> {
    CommentWithInfoDto findByIdWithInfo(UUID id);
    List<CommentWithInfoDto> findAllWithInfo();
    List<CommentWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex);
    CommentWithInfoDto saveComment(CommentDto commentDto, UUID taskId, UUID userId);
    CommentWithInfoDto addResponseToCmt(UUID id, UUID respondedCmtId);
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

    @Override
    public CommentWithInfoDto findByIdWithInfo(UUID id) {
        Comment comment = repository.findByIdWithInfo(id)
                .orElseThrow(() -> new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        return mapper.map(comment, CommentWithInfoDto.class);
    }

    @Override
    public List<CommentWithInfoDto> findAllWithInfo() {
        return repository.findAllWithInfo().stream()
                .map(model -> mapper.map(model, CommentWithInfoDto.class))
                .toList();
    }

    @Override
    public List<CommentWithInfoDto> findAllWithInfoWithPaging(int size, int pageIndex) {
        return repository.findAllWithInfoWithPaging(PageRequest.of(pageIndex, size, Sort.by("createdAt")))
                .stream()
                .map(model -> mapper.map(model, CommentWithInfoDto.class))
                .toList();
    }

    @Override
    public CommentWithInfoDto saveComment(CommentDto commentDto, UUID taskId, UUID userId) {
        Task task = taskService.findTaskById(taskId);
        User user = userService.findUserById(userId);
        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setTask(task);
        comment.setWriter(user);
        repository.save(comment);
        return mapper.map(comment, CommentWithInfoDto.class);
    }

    @Override
    public CommentWithInfoDto addResponseToCmt(UUID id, UUID respondedCmtId) {
        Comment comment = repository.findById(id)
                .orElseThrow(() -> new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        Comment respondedCmt = repository.findById(respondedCmtId)
                .orElseThrow(() -> new ValidationException(MessageUtil.getMessage(messageSource, UUID_NOT_FOUND)));
        comment.setResponseTo(respondedCmt);
        return mapper.map(comment, CommentWithInfoDto.class);
    }
}
