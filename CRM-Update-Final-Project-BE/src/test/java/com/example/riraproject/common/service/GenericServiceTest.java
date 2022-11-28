package com.example.riraproject.common.service;

import com.example.riraproject.role.dto.RoleDto;
import com.example.riraproject.role.model.Role;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.ValidationException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GenericServiceTest {
    @Mock private JpaRepository<Role, UUID> repository;
    @Autowired private ModelMapper mapper;
    private GenericService<Role, RoleDto, UUID> service;
    private final UUID id = UUID.randomUUID();
    private Role model;
    private RoleDto dto;

    @BeforeEach
    void init() {
        service = new GenericService<>() {
            @Override
            public JpaRepository<Role, UUID> getRepository() {
                return repository;
            }

            @Override
            public ModelMapper getMapper() {
                return mapper;
            }
        };
        model = Role.builder()
                .description("this is a model")
                .build();
        dto = RoleDto.builder()
                .description("this is a dto")
                .build();
    }

    @Test
    void findByIdTest() {
        //MOCK
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(model));
        //CHECK RESULT
        //CASE 1: ID IS VALID
        RoleDto result = service.findById(RoleDto.class, id);
        Assertions.assertEquals(model.getDescription(), result.getDescription());
        Mockito.verify(repository).findById(id);
        //CASE 2: ID IS INVALID
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions
                .assertThrowsExactly(ValidationException.class ,
                        () -> service.findById(RoleDto.class, id));
        Mockito.verify(repository, Mockito.times(2)).findById(id);
    }

    @Test
    void findAllWithPagingTest() {
        Pageable pageable = PageRequest.of(2, 5, Sort.by("createdAt"));
        Page page = Mockito.mock(Page.class);
        Mockito.when(repository.findAll(pageable))
                .thenReturn(page);
        Mockito.when(page.stream()).thenReturn(List.of(model).stream());
        List<RoleDto> list = service.findAllWithPaging(RoleDto.class, 5, 2);
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(model.getDescription(), list.get(0).getDescription());
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    void findAllTest() {
        Mockito.when(repository.findAll()).thenReturn(List.of(model));
        List<RoleDto> list = service.findAll(RoleDto.class);
        Assertions.assertEquals(1, list.size());
        Assertions.assertEquals(model.getDescription(), list.get(0).getDescription());
        Mockito.verify(repository).findAll();
    }

    @Test
    void saveTest() {
        //SETUP
        Role savedModel = Role.builder()
                .description("this is a saved model")
                .build();
        ArgumentCaptor<Role> captor = ArgumentCaptor.forClass(Role.class);
        Mockito.when(repository.save(Mockito.any(Role.class))).thenReturn(savedModel);
        //TRY
        RoleDto savedDto = service.save(Role.class, dto);
        Mockito.verify(repository).save(captor.capture());
        Assertions.assertEquals(dto.getDescription(), captor.getValue().getDescription());
        Assertions.assertEquals(savedModel.getDescription(), savedDto.getDescription());
    }

    @Test
    void updateTest() {
        //SETUP
        Role savedModel = Role.builder()
                .description("this is a saved model")
                .build();
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(model));
        Mockito.when(repository.save(model)).thenReturn(savedModel);
        //TRY
        //CASE 1 : ID IS VALID
        RoleDto result = service.update(id, dto);
        Assertions.assertEquals(dto.getDescription(), result.getDescription());
        Mockito.verify(repository).findById(id);
        //CASE 2 : ID IS INVALID
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class, () -> service.update(id, dto));
        Mockito.verify(repository, Mockito.times(2)).findById(id);
    }

    @Test
    void deleteByIdTest() {
        //SETUP
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(model));
        Mockito.doNothing().when(repository).delete(model);
        //CASE 1 : ID IS VALID
        Assertions.assertDoesNotThrow(() -> service.deleteById(id));
        Mockito.verify(repository).findById(id);
        //CASE 2 : ID IS INVALID
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class, () -> service.deleteById(id));
        Mockito.verify(repository, Mockito.times(2)).findById(id);
    }
}
