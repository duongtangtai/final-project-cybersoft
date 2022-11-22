package com.example.riraproject.user.resource;

import com.example.riraproject.common.dto.ResponseDto;
import com.example.riraproject.common.util.MessageUtil;
import com.example.riraproject.common.util.ResponseUtil;
import com.example.riraproject.common.validation.annotation.UUIDConstraint;
import com.example.riraproject.common.validation.group.SaveInfo;
import com.example.riraproject.common.validation.group.UpdateInfo;
import com.example.riraproject.role.util.RoleUtil;
import com.example.riraproject.security.aop.Authorized;
import com.example.riraproject.user.dto.UserDto;
import com.example.riraproject.user.service.UserService;
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

    @Authorized(roles = {RoleUtil.MANAGER})
    @GetMapping
    public ResponseEntity<ResponseDto> findAll() {
        List<UserDto> userList = service.findAll(UserDto.class);
        userList.forEach(user -> user.setPassword(null));
        return ResponseUtil.get(userList, HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER})
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

    @Authorized(roles = {RoleUtil.MANAGER})
    @GetMapping("/with-info")
    public ResponseEntity<ResponseDto> findAllWithInfo(){
        return ResponseUtil.get(service.findAllWithInfo(), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER})
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

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER})
    @GetMapping("/inside-project/{projectId}")
    public ResponseEntity<ResponseDto> findAllInsideProject(@PathVariable("projectId")
                                                            @UUIDConstraint String projectId) {
        return ResponseUtil.get(service.findAllInsideProject(UUID.fromString(projectId)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER})
    @GetMapping("/inside-project-with-task/{projectId}")
    public ResponseEntity<ResponseDto> findAllInsideProjectWithTask(@PathVariable("projectId")
                                                            @UUIDConstraint String projectId) {
        return ResponseUtil.get(service.findAllInsideProjectWithTask(UUID.fromString(projectId)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER})
    @GetMapping("/outside-project/{projectId}")
    public ResponseEntity<ResponseDto> findAllByProject(@PathVariable("projectId")
                                                        @UUIDConstraint String projectId) {
        return ResponseUtil.get(service.findAllOutsideProject(UUID.fromString(projectId)), HttpStatus.OK);
    }

    @Authorized(roles = {RoleUtil.MANAGER, RoleUtil.LEADER})
    @GetMapping("/leader-role")
    public ResponseEntity<ResponseDto> findAllLeaderRole() {
        return ResponseUtil.get(service.findAllLeaderRole(), HttpStatus.OK);
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
