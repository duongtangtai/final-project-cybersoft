package com.example.jiraproject.operation.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.ApiUtil;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.operation.dto.OperationDto;
import com.example.jiraproject.operation.model.Operation;
import com.example.jiraproject.operation.service.OperationService;
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
@RequestMapping("/v1/operations")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class OperationRestResource {
    private final OperationService service;
    private final MessageSource messageSource;

    @Authorized(operation = ApiUtil.OPERATION)
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findById(OperationDto.class, UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.OPERATION)
    @GetMapping
    public ResponseEntity<ResponseDto> findAll() {
        return ResponseUtil.get(service.findAll(OperationDto.class), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.OPERATION)
    @GetMapping("/paging")
    public ResponseEntity<ResponseDto> findAllWithPaging(@RequestParam("size") int size,
                                                         @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithPaging(OperationDto.class, size, pageIndex), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.OPERATION, type = Operation.Type.SAVE_OR_UPDATE)
    @PostMapping
    public ResponseEntity<ResponseDto> save(@RequestBody @Validated(SaveInfo.class) OperationDto dto) {
        return ResponseUtil.get(service.save(Operation.class, dto), HttpStatus.CREATED);
    }

    @Authorized(operation = ApiUtil.OPERATION, type = Operation.Type.SAVE_OR_UPDATE)
    @PutMapping
    public ResponseEntity<ResponseDto> update(@RequestBody @Validated(UpdateInfo.class) OperationDto dto) {
        return ResponseUtil.get(service.update(dto.getId(), dto), HttpStatus.OK);
    }

    @Authorized(operation = ApiUtil.OPERATION, type = Operation.Type.REMOVE)
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@PathVariable("id") @UUIDConstraint String id) {
        service.deleteById(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "operation.deleted"), HttpStatus.OK);
    }
}
