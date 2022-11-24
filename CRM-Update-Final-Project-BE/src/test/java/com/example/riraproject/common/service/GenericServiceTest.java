package com.example.riraproject.common.service;

import com.example.riraproject.common.model.BaseEntity;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.ValidationException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@ExtendWith(MockitoExtension.class)
class GenericServiceTest {

    @Mock
    JpaRepository<BaseEntity, UUID> repository;

    @Mock
    ModelMapper mapper;

    @InjectMocks
    GenericService<BaseEntity, Object, UUID> service = new GenericService<>() {
        @Override
        public JpaRepository<BaseEntity, UUID> getRepository() {
            return repository;
        }

        @Override
        public ModelMapper getMapper() {
            return mapper;
        }
    };

    private final UUID id = UUID.randomUUID();
    private final BaseEntity model = new BaseEntity();
    private final Object dto = new Object();

    @Test
    void findByIdTest() {
        //MOCK
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(model));
        Mockito.when(mapper.map(model, Object.class)).thenReturn(dto);
        //CHECK RESULT
        //CASE 1: ID IS VALID
        Assertions.assertEquals(dto, service.findById(Object.class, id));
        Mockito.verify(repository).findById(id);
        //CASE 2: ID IS INVALID
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions
                .assertThrowsExactly(ValidationException.class ,
                        () -> service.findById(Object.class, id));
        Mockito.verify(repository, Mockito.times(2)).findById(id);
    }

    @Test
    void findAllWithPagingTest() {
        Pageable pageable = PageRequest.of(2, 5, Sort.by("createdAt"));
        Page page = Mockito.mock(Page.class);
        Stream stream = Mockito.mock(Stream.class);
        Mockito.when(repository.findAll(pageable))
                .thenReturn(page);
        Mockito.when(page.stream()).thenReturn(stream);
        Mockito.when(stream.toList()).thenReturn(List.of(model));
        Mockito.when(mapper.map(model, Object.class)).thenReturn(dto);
        Assertions.assertEquals(List.of(dto),
                service.findAllWithPaging(Object.class, 5, 2));
        Mockito.verify(repository).findAll(pageable);
    }

    @Test
    void findAllTest() {
        Mockito.when(repository.findAll()).thenReturn(List.of(model));
        Mockito.when(mapper.map(model, Object.class)).thenReturn(dto);
        Assertions.assertEquals(List.of(dto), service.findAll(Object.class));
        Mockito.verify(repository).findAll();
    }

    @Test
    void saveTest() {
        BaseEntity savedModel = Mockito.mock(BaseEntity.class);
        Object savedDto = Mockito.mock(Object.class);
        Mockito.when(mapper.map(dto, BaseEntity.class)).thenReturn(model);
        Mockito.when(repository.save(model)).thenReturn(savedModel);
        Mockito.when(mapper.map(savedModel, (Type) dto.getClass())).thenReturn(savedDto);
        Assertions.assertEquals(savedDto, service.save(BaseEntity.class, dto));
        Mockito.verify(repository).save(model);
    }

    @Test
    void updateTest() {
        //MOCKING
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(model));
        Mockito.doNothing().when(mapper).map(dto, model);
        Mockito.when(repository.save(model)).thenReturn(model);
        Mockito.when(mapper.map(model, (Type) dto.getClass())).thenReturn(dto);
        //TRY
        //CASE 1 : ID IS VALID
        Assertions.assertEquals(dto, service.update(id, dto));
        Mockito.verify(repository).findById(id);
        //CASE 2 : ID IS INVALID
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class, () -> service.update(id, dto));
    }

    @Test
    void deleteByIdTest() {
        //MOCKING
        Mockito.when(repository.findById(id)).thenReturn(Optional.of(model));
        Mockito.doNothing().when(repository).delete(model);
        //CASE 1 : ID IS VALID
        Assertions.assertDoesNotThrow(() -> service.deleteById(id));
        //CASE 2 : ID IS INVALID
        Mockito.when(repository.findById(id)).thenReturn(Optional.empty());
        Assertions.assertThrowsExactly(ValidationException.class, () -> service.deleteById(id));
    }
}
