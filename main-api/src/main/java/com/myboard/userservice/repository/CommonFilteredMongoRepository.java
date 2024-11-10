package com.myboard.userservice.repository;

import com.myboard.userservice.controller.model.common.AbstractFilterRequest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface CommonFilteredMongoRepository<T> {
    Page<T> findAllByFilter(AbstractFilterRequest filterRequest, Class<T> clazz, Pageable pageable);
}
