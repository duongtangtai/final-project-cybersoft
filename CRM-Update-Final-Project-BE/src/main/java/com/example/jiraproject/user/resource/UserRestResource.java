package com.example.jiraproject.user.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.user.dto.UserDto;
import com.example.jiraproject.user.model.User;
import com.example.jiraproject.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/users")
@Validated
@RequiredArgsConstructor
public class UserRestResource {
    private final UserService service;
    private final MessageSource messageSource;

    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable("id") @UUIDConstraint String id) {
        return ResponseUtil.get(service.findById(UserDto.class, UUID.fromString(id)), HttpStatus.OK);
    }

    @GetMapping
    public ResponseEntity<ResponseDto> findAll() {
        return ResponseUtil.get(service.findAll(UserDto.class), HttpStatus.OK);
    }

    @GetMapping("/paging")
    public ResponseEntity<ResponseDto> findAllWithPaging(@RequestParam("size") int size,
                                                         @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithPaging(UserDto.class, size, pageIndex), HttpStatus.OK);
    }

    @GetMapping("/{id}/with-info")
    public ResponseEntity<ResponseDto> findByIdWithInfo(@PathVariable("id") @UUIDConstraint String id){
        return ResponseUtil.get(service.findByIdWithInfo(UUID.fromString(id)), HttpStatus.OK);
    }

    @GetMapping("/with-info")
    public ResponseEntity<ResponseDto> findAllWithInfo(){
        return ResponseUtil.get(service.findAllWithInfo(), HttpStatus.OK);
    }

    @GetMapping("/with-info/paging")
    public ResponseEntity<ResponseDto> findAllWithInfoWithPaging(@RequestParam("size") int size,
                                                                 @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithInfoWithPaging(size, pageIndex), HttpStatus.OK);
    }

    @PostMapping
    public ResponseEntity<ResponseDto> save(@RequestBody @Validated(SaveInfo.class) UserDto dto) {
        return ResponseUtil.get(service.save(User.class, dto), HttpStatus.OK);
    }

    @PostMapping("/{id}/add-roles")
    public ResponseEntity<ResponseDto> addRoles(@PathVariable("id") @UUIDConstraint String userId,
                                                @RequestBody Set<UUID> roleIds) {
        return ResponseUtil.get(service.addRoles(UUID.fromString(userId), roleIds), HttpStatus.OK);
    }

    @PostMapping("/{id}/remove-roles")
    public ResponseEntity<ResponseDto> removeRoles(@PathVariable("id") @UUIDConstraint String userId,
                                                @RequestBody Set<UUID> roleIds) {
        return ResponseUtil.get(service.removeRoles(UUID.fromString(userId), roleIds), HttpStatus.OK);
    }

    @PutMapping
    public ResponseEntity<ResponseDto> update(@RequestBody @Validated(UpdateInfo.class) UserDto dto) {
        return ResponseUtil.get(service.update(dto.getId(), dto), HttpStatus.OK);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@PathVariable("id") @UUIDConstraint String id) {
        service.deleteById(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "user.deleted"), HttpStatus.OK);
    }
}
