package com.myboard.userservice.repository;

import com.myboard.userservice.controller.model.common.AbstractFilterRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.support.PageableExecutionUtils;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CommonFilteredMongoRepositoryImpl<T> implements CommonFilteredMongoRepository<T> {

    @Autowired
    private MongoTemplate mongoTemplate;

    /**
     * Find all entities filtered and paginated based on AbstractFilterRequest.
     *
     * @param filterRequest The filter request that contains query parameters
     * @param clazz         The class type of the entity to query
     * @param pageable      Pagination details (page and size)
     * @return A paginated list of filtered entities
     */
    @Override
    public Page<T> findAllByFilter(AbstractFilterRequest filterRequest, Class<T> clazz, Pageable pageable) {
        // Build the query using AbstractFilterRequest's buildFilterQuery method
        Query query = filterRequest.buildFilterQuery();

        // Apply pagination with the provided Pageable object
        Pageable pageRequest = PageRequest.of(filterRequest.getPage(), filterRequest.getSize());

        // Execute the query with pagination and return results
        List<T> results = mongoTemplate.find(query.with(pageRequest), clazz);

        // Return a Page object with results and total count
        return PageableExecutionUtils.getPage(
                results,
                pageRequest,
                () -> mongoTemplate.count(query, clazz)
        );
    }
}
