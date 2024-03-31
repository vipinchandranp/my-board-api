package com.myboard.userservice.dto;

import lombok.Data;

import java.util.List;

@Data
public class DisplayDetailsDTO {
    private String id;
    private double latitude;
    private double longitude;
    private String description;
    private String name;
    private String fileName;
    private String userName;
    private List<DisplayCommentDTO> comments;
    private double rating;
    private String userId;
}
