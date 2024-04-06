package com.myboard.userservice.dto;

import com.myboard.userservice.entity.User;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;
@Data
public class RouteDTO {
    private String id;
    private String name;
    private User routeOwner;
    private List<RoutePointsDTO> locations; // Add this field for storing locations

    // Constructors, getters, and setters
}
