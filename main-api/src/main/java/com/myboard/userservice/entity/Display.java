package com.myboard.userservice.entity;

import com.myboard.userservice.types.StatusType;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexType;
import org.springframework.data.mongodb.core.index.GeoSpatialIndexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@Document(collection = "display")
public class Display extends Base {

    public Display(Media media) {
        this.media = media;
    }

    private List<Rating> ratings = new ArrayList<>();

    private List<Comment> comments = new ArrayList<>();

    private StatusType validationStatus = StatusType.WAITING_FOR_APPROVAL;

    private Media media;

    private HashMap<LocalDate, ArrayList<Timeslot>> availabilityMap;

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)

    private double[] location;
}
