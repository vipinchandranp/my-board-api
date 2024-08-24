package com.myboard.userservice.entity;

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

    public Display(Media media) {
        this.media = media;
    }

    private String name;

    @DBRef(lazy = true)
    private List<Rating> ratings = new ArrayList<>();

    @DBRef(lazy = true)
    private List<Comment> comments = new ArrayList<>();

    private StatusType validationStatus = StatusType.WAITING_FOR_APPROVAL;

    private Media media;

    // Reference to the Availability collection
    @DBRef(lazy = true)
    private List<Availability> availabilityList;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private double[] location;
}
