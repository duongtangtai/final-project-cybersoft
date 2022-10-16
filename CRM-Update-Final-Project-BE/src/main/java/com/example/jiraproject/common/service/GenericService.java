package com.example.jiraproject.common.service;

import com.example.jiraproject.common.model.BaseEntity;
import com.example.jiraproject.common.util.MessageUtil;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.validation.ValidationException;
import java.lang.reflect.Type;
import java.util.List;

public interface GenericService <T extends BaseEntity, D , U> {

    JpaRepository <T, U> getRepository();
    ModelMapper getMapper();

    default D findById(Class<D> dtoClass, U id) {
        T model = getRepository().findById(id)
                .orElseThrow(() -> new ValidationException(MessageUtil.UUID_NOT_FOUND));
        return getMapper().map(model, dtoClass);
    }

    default List<D> findAllWithPaging(Class<D> dtoClass, int size, int pageIndex) {
        List<T> modelList =getRepository()
                .findAll(PageRequest.of(pageIndex, size, Sort.by("createdAt"))).stream().toList();
        return modelList.stream().map(model -> getMapper().map(model, dtoClass)).toList();
    }

    default List<D> findAll(Class<D> dtoClass) {
        List<T> modelList = getRepository().findAll().stream().sorted().toList(); //using comparable
        return modelList.stream().map(model -> getMapper().map(model, dtoClass)).toList();
    }

    default D save(Class<T> modelClass, D dto) {
        T model = getMapper().map(dto, modelClass);
        T savedModel = getRepository().save(model);
        return getMapper().map(savedModel, (Type) dto.getClass());
    }

    default D update(U id, D dto) {
        T model = getRepository().findById(id)
                .orElseThrow(() -> new ValidationException(MessageUtil.UUID_NOT_FOUND));
        getMapper().map(dto, model);
        getRepository().save(model);
        return getMapper().map(model, (Type) dto.getClass());
    }

    default void deleteById(U id) {
        T model = getRepository().findById(id)
                .orElseThrow(() -> new ValidationException(MessageUtil.UUID_NOT_FOUND));
        getRepository().delete(model);
    }
}
