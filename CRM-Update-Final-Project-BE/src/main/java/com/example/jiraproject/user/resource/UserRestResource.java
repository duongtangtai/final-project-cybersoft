package com.example.jiraproject.user.resource;

import com.example.jiraproject.common.dto.ResponseDto;
import com.example.jiraproject.common.util.MessageUtil;
import com.example.jiraproject.common.util.ResponseUtil;
import com.example.jiraproject.common.validation.annotation.UUIDConstraint;
import com.example.jiraproject.common.validation.group.SaveInfo;
import com.example.jiraproject.common.validation.group.UpdateInfo;
import com.example.jiraproject.role.util.RoleUtil;
import com.example.jiraproject.security.aop.Authorized;
import com.example.jiraproject.user.dto.UserDto;
import com.example.jiraproject.user.service.UserService;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Set;
import java.util.UUID;

@RestController
@RequestMapping("/v1/users")
@RequiredArgsConstructor
@Validated
@SecurityRequirement(name = "bearerAuth")
public class UserRestResource {
    private final UserService service;
    private final MessageSource messageSource;

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/{id}")
    public ResponseEntity<ResponseDto> findById(@PathVariable("id") @UUIDConstraint String id) {
        UserDto userDto = service.findById(UserDto.class, UUID.fromString(id));
        userDto.setPassword(null);
        return ResponseUtil.get(userDto, HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER})
    @GetMapping
    public ResponseEntity<ResponseDto> findAll() {
        List<UserDto> userList = service.findAll(UserDto.class);
        userList.forEach(user -> user.setPassword(null));
        return ResponseUtil.get(userList, HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER})
    @GetMapping("/paging")
    public ResponseEntity<ResponseDto> findAllWithPaging(@RequestParam("size") int size,
                                                         @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithPaging(UserDto.class, size, pageIndex), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/with-info/{id}")
    public ResponseEntity<ResponseDto> findByIdWithInfo(@PathVariable("id") @UUIDConstraint String id){
        return ResponseUtil.get(service.findByIdWithInfo(UUID.fromString(id)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER})
    @GetMapping("/with-info")
    public ResponseEntity<ResponseDto> findAllWithInfo(){
        return ResponseUtil.get(service.findAllWithInfo(), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER})
    @GetMapping("/with-info/paging")
    public ResponseEntity<ResponseDto> findAllWithInfoWithPaging(@RequestParam("size") int size,
                                                                 @RequestParam("pageIndex") int pageIndex) {
        return ResponseUtil.get(service.findAllWithInfoWithPaging(size, pageIndex), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/account-status")
    public ResponseEntity<ResponseDto> findAllAccountStatus() {
        return ResponseUtil.get(service.findAllAccountStatus(), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER, RoleUtil.EMPLOYEE})
    @GetMapping("/genders")
    public ResponseEntity<ResponseDto> findAllGenders() {
        return ResponseUtil.get(service.findAllGenders(), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER})
    @GetMapping("/inside-project/{projectId}")
    public ResponseEntity<ResponseDto> findAllInsideProject(@PathVariable("projectId")
                                                            @UUIDConstraint String projectId) {
        return ResponseUtil.get(service.findAllInsideProject(UUID.fromString(projectId)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER})
    @GetMapping("/outside-project/{projectId}")
    public ResponseEntity<ResponseDto> findAllByProject(@PathVariable("projectId")
                                                        @UUIDConstraint String projectId) {
        return ResponseUtil.get(service.findAllOutsideProject(UUID.fromString(projectId)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.ADMIN})
    @PostMapping
    public ResponseEntity<ResponseDto> save(@RequestBody @Validated(SaveInfo.class) UserDto dto) {
        service.save(dto);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "user.saved"), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.ADMIN})
    @PostMapping("/update-roles/{id}")
    public ResponseEntity<ResponseDto> addRoles(@PathVariable("id") @UUIDConstraint String userId,
                                                @RequestBody Set<UUID> roleIds) {
        service.updateRoles(UUID.fromString(userId), roleIds);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "user.role.updated"), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.ADMIN})
    @PutMapping
    public ResponseEntity<ResponseDto> update(@RequestBody @Validated(UpdateInfo.class) UserDto dto) {
        service.update(dto);
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "user.updated"), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.ADMIN})
    @DeleteMapping("/{id}")
    public ResponseEntity<ResponseDto> deleteById(@PathVariable("id") @UUIDConstraint String id) {
        service.deleteById(UUID.fromString(id));
        return ResponseUtil.get(MessageUtil.getMessage(messageSource, "user.deleted"), HttpStatus.OK);
    }
}
