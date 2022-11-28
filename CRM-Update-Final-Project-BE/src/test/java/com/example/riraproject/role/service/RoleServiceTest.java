package com.example.riraproject.role.service;

import com.example.riraproject.role.model.Role;
import com.example.riraproject.role.repository.RoleRepository;
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

import java.util.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
class RoleServiceTest {
    @Mock private RoleRepository repository;
    @Autowired private ModelMapper mapper;
    private RoleServiceImpl service;

    @BeforeEach
    void init() {
        service = new RoleServiceImpl(repository, mapper);
    }

    @Test
    void getRepositoryAndMapper() {
        Assertions.assertEquals(repository, service.getRepository());
        Assertions.assertEquals(mapper, service.getMapper());
    }

    @Test
    void findAllByIdsTest() {
        Set<UUID> uuids = new HashSet<>();
        uuids.add(UUID.randomUUID());
        uuids.add(UUID.randomUUID());
        uuids.add(UUID.randomUUID());
        List<Role> list = new ArrayList<>();
        list.add(new Role());
        list.add(new Role());
        list.add(new Role());
        Mockito.when(repository.findAllById(uuids)).thenReturn(list);
        Assertions.assertEquals(list, service.findAllByIds(uuids));
        Mockito.verify(repository).findAllById(uuids);
    }
}
