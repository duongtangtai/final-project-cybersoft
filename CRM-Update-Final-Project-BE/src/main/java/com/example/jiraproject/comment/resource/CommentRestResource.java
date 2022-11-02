package com.example.jiraproject.comment.resource;

import com.example.jiraproject.comment.dto.CommentDto;
import com.example.jiraproject.comment.service.CommentService;
import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.ApiUtil;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.operation.model.Operation;
import com.example.jiraproject.security.aop.Authorized;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/v1/comments")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class CommentRestResource {
    private final CommentService service;
    private final MessageSource messageSource;

    @Authorized(operation = ApiUtil.COMMENT)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findById(CommentDto.class, UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.COMMENT)
    @GetMapping
    public ResponseEntity<ResponseDto> findAll() {
        return ResponseUtil.get(service.findAll(CommentDto.class), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.COMMENT)
    @GetMapping("/paging")
    public ResponseEntity<ResponseDto> findAllWithPaging(@RequestParam("size") int size,
                                                         @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithPaging(CommentDto.class, size, pageIndex), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.COMMENT)
    @GetMapping("/{id}/with-info")
    public ResponseEntity<ResponseDto> findByIdWithInfo(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findByIdWithInfo(UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.COMMENT)
    @GetMapping("/with-info")
    public ResponseEntity<ResponseDto> findAllWithInfo() {
        return ResponseUtil.get(service.findAllWithInfo(), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.COMMENT)
    @GetMapping("/with-info/paging")
    public ResponseEntity<ResponseDto> findAllWithInfoWithPaging(@RequestParam("size") int size,
                                                                 @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithInfoWithPaging(size, pageIndex), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.COMMENT)
    @GetMapping("/with-info/by-task/{taskId}")
    public ResponseEntity<ResponseDto> findAllWithInfoByTaskId(@PathVariable("taskId") @UUIDConstraint String taskId) {
        return ResponseUtil.get(service.findAllWithInfoByTaskId(UUID.fromString(taskId)), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.COMMENT, type = Operation.Type.SAVE_OR_UPDATE)
    @PostMapping
    public ResponseEntity<ResponseDto> save(@RequestBody @Validated(SaveInfo.class) CommentDto commentDto) {
        service.saveComment(commentDto);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "comment.saved"), HttpStatus.CREATED);
    }

    @Authorized(operation = ApiUtil.COMMENT, type = Operation.Type.SAVE_OR_UPDATE)
    @PostMapping("/{id}/response-to")
    public ResponseEntity<ResponseDto> addResponseToCmt(@PathVariable("id") @UUIDConstraint String id,
                                                     @RequestParam("responseTo") @UUIDConstraint String respondedCmtId) {
        return ResponseUtil.get(service.addResponseToCmt(UUID.fromString(id), UUID.fromString(respondedCmtId)),
                HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.COMMENT, type = Operation.Type.SAVE_OR_UPDATE)
    @PutMapping
    public ResponseEntity<ResponseDto> update(@RequestBody @Validated(UpdateInfo.class) CommentDto commentDto) {
        return ResponseUtil.get(service.update(commentDto.getId(), commentDto), HttpStatus.OK);
    }

    //TODO everyone can delete their own message or only role LEADER can do that?
    @Authorized(operation = ApiUtil.COMMENT, type = Operation.Type.REMOVE)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@PathVariable("id") @UUIDConstraint String id) {
        service.deleteById(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "comment.deleted"), HttpStatus.OK);
    }
}
