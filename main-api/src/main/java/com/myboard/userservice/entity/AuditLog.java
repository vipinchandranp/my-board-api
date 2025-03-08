package com.myboard.userservice.entity;

import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "audit_logs")
public class AuditLog {

    private String id;

    private String operation; // CREATE, UPDATE, DELETE

    private String collectionName;

    private String entityId;

    private String userId;     // User ID of the action performer
    private String username;   // Optionally store username for easier querying

    private List<ChangeDetail> changes; // List of changes capturing property name, before and after values

    @CreatedDate
    private LocalDateTime timestamp;

    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    @Builder
    public static class ChangeDetail {
        private String propertyName;
        private Object beforeValue;
        private Object afterValue;
    }
}
