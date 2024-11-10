package com.myboard.userservice.controller.model.common;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public abstract class AbstractFilterRequest {

    // Filter data properties
    private String searchText;
    private LocalDate startDate;
    private LocalDate endDate;
    private List<String> ids; // New field to filter by a list of IDs

    // Pagination properties with default values
    private int page = 0;  // Default page number is 0 (first page)
    private int size = 10;  // Default page size is 10

    // Additional filters specific to this request
    private String status;   // Filter by status
    private Boolean isRecent; // Filter by recent
    private Boolean isFavorite; // Filter by favorite

    // Default implementation of buildFilterQuery() method
    public Query buildFilterQuery() {
        Query query = new Query();

        if (searchText != null) {
            query.addCriteria(Criteria.where("name").regex(searchText, "i"));
        }

        if (startDate != null && endDate != null) {
            query.addCriteria(Criteria.where("createdTime").gte(startDate).lte(endDate));
        }

        if (ids != null && !ids.isEmpty()) {
            query.addCriteria(Criteria.where("id").in(ids));
        }

        if (status != null) {
            query.addCriteria(Criteria.where("status").is(status));
        }

        if (isRecent != null) {
            query.addCriteria(Criteria.where("isRecent").is(isRecent));
        }

        if (isFavorite != null) {
            query.addCriteria(Criteria.where("isFavorite").is(isFavorite));
        }

        return query;
    }
}
