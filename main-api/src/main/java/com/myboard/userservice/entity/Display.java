package com.myboard.userservice.entity;

import com.myboard.userservice.controller.model.common.MediaFile;
import com.myboard.userservice.types.StatusType;
import jakarta.persistence.PostPersist;
import jakarta.persistence.PrePersist;
import lombok.*;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Base64;
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

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private double[] location;

    // New field to store associated boards
    @DBRef(lazy = true) // Create a DB reference to Board entities
    private List<Board> boards = new ArrayList<>();

    private String displayPin;

    private Double price;


    // Added setters for latitude and longitude
    public void setLatitude(Double latitude) {
        if (latitude != null && this.location != null) {
            this.location[0] = latitude;  // Assuming location[0] is latitude
        }
    }

    public void setLongitude(Double longitude) {
        if (longitude != null && this.location != null) {
            this.location[1] = longitude;  // Assuming location[1] is longitude
        }
    }

    public void setLocation(Double latitude, Double longitude) {
        this.location = new double[] {latitude, longitude};
    }

}
