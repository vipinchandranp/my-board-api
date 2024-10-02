package com.myboard.userservice.entity;

import com.myboard.userservice.controller.model.common.MediaFile;
import com.myboard.userservice.types.StatusType;
import lombok.*;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "display")
public class Display extends Base {

    private String name;

    @DBRef(lazy = true)
    private List<Rating> ratings = new ArrayList<>();

    @DBRef(lazy = true)
    private List<Comment> comments = new ArrayList<>();

    private StatusType status = StatusType.WAITING_FOR_APPROVAL;

    private List<MediaFile> mediaFiles = new ArrayList<>();

    // Reference to the Availability collection
    @DBRef(lazy = true)
    private List<Availability> availabilityList;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private double[] location;

    // New field to store associated boards
    @DBRef(lazy = true) // Create a DB reference to Board entities
    private List<Board> boards = new ArrayList<>();
}
