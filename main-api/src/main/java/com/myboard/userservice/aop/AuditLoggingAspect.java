package com.myboard.userservice.aop;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.myboard.userservice.entity.AuditLog;
import com.myboard.userservice.repository.AuditLogRepository;
import lombok.RequiredArgsConstructor;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.support.Repositories;
import org.springframework.stereotype.Component;

import java.lang.reflect.Field;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Aspect
@Component
@RequiredArgsConstructor
public class AuditLoggingAspect {

    private final AuditLogRepository auditLogRepository;
    private final ObjectMapper objectMapper;

    @Autowired
    private ApplicationContext applicationContext;

    @Pointcut("execution(* org.springframework.data.mongodb.repository.MongoRepository+.save(..)) && !target(com.myboard.userservice.repository.AuditLogRepository)")
    public void onSave() {
    }

    @Before("onSave()")
    public void logBeforeSave(JoinPoint joinPoint) {
        Object savedEntity = joinPoint.getArgs()[0]; // The entity being saved

        if (savedEntity == null || savedEntity instanceof AuditLog) {
            return; // Skip null entities or logging AuditLog itself
        }

        try {
            // Extract the ID and collection name
            String entityId = extractEntityId(savedEntity);
            String collectionName = savedEntity.getClass().getSimpleName();

            // Get the corresponding repository dynamically
            MongoRepository repository = getRepositoryForEntity(savedEntity);

            if (repository == null) {
                throw new IllegalArgumentException("No repository found for collection: " + collectionName);
            }

            // Load the original state of the entity
            Object originalEntity = repository.findById(entityId).orElse(null);

            if (originalEntity == null) {
                // Likely a new entity, create an "INSERT" audit log
                AuditLog auditLog = AuditLog.builder()
                        .operation("INSERT")
                        .collectionName(collectionName)
                        .entityId(entityId)
                        .changes(List.of(new AuditLog.ChangeDetail("entity", null, objectMapper.writeValueAsString(savedEntity))))
                        .timestamp(LocalDateTime.now())
                        .build();

                auditLogRepository.save(auditLog);
                return;
            }

            // Compare states and find changes
            List<AuditLog.ChangeDetail> changedProperties = getChangedProperties(originalEntity, savedEntity);

            if (!changedProperties.isEmpty()) {
                // Build and save the audit log for "UPDATE"
                AuditLog auditLog = AuditLog.builder()
                        .operation("UPDATE")
                        .collectionName(collectionName)
                        .entityId(entityId)
                        .changes(changedProperties) // Set the list of changes
                        .timestamp(LocalDateTime.now())
                        .build();

                auditLogRepository.save(auditLog);
            } else {
                System.out.println("No changes detected for entity with ID: " + entityId);
            }
        } catch (Exception e) {
            System.err.println("Failed to log audit information: " + e.getMessage());
        }
    }

    private MongoRepository getRepositoryForEntity(Object entity) {
        // Use Repositories class for dynamic lookup
        Repositories repositories = new Repositories(applicationContext);
        return (MongoRepository) repositories.getRepositoryFor(entity.getClass())
                .orElseThrow(() -> new IllegalArgumentException(
                        "No repository found for entity class: " + entity.getClass().getName()));
    }

    private List<AuditLog.ChangeDetail> getChangedProperties(Object original, Object updated) {
        List<AuditLog.ChangeDetail> changedProperties = new ArrayList<>();
        if (original == null) {
            return changedProperties; // Original entity doesn't exist; treat as CREATE
        }

        Class<?> clazz = updated.getClass();

        while (clazz != null) { // Traverse the class hierarchy
            for (Field field : clazz.getDeclaredFields()) {
                field.setAccessible(true);
                try {
                    Object originalValue = field.get(original);
                    Object updatedValue = field.get(updated);

                    if (originalValue == null && updatedValue == null) {
                        continue; // Skip if both values are null
                    }

                    if (originalValue == null || updatedValue == null || !originalValue.equals(updatedValue)) {
                        // Capture changes
                        changedProperties.add(AuditLog.ChangeDetail.builder()
                                .propertyName(field.getName())
                                .beforeValue(originalValue)
                                .afterValue(updatedValue)
                                .build());
                    }

                } catch (IllegalAccessException e) {
                    System.err.println("Failed to access field: " + field.getName());
                }
            }
            clazz = clazz.getSuperclass(); // Move to the superclass
        }

        return changedProperties;
    }

    private String extractEntityId(Object entity) throws IllegalAccessException {
        Class<?> currentClass = entity.getClass();

        while (currentClass != null) { // Traverse the class hierarchy
            for (Field field : currentClass.getDeclaredFields()) {
                if (field.getName().equalsIgnoreCase("id")) {
                    field.setAccessible(true);
                    Object idValue = field.get(entity);
                    return idValue != null ? idValue.toString() : "UNKNOWN_ID";
                }
            }
            currentClass = currentClass.getSuperclass(); // Move to the superclass
        }

        return "UNKNOWN_ID"; // Return default if no ID field is found
    }
}
