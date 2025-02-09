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

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "display")
public class Display extends Base {

    private String name;

    @DBRef(lazy = true)
    private List<Rating> ratings = new ArrayList<>(); // List to hold ratings for the display

    @DBRef(lazy = true)
    private List<Comment> comments = new ArrayList<>(); // List to hold comments for the display

    private StatusType status = StatusType.WAITING_FOR_APPROVAL;

    private List<MediaFile> mediaFiles = new ArrayList<>();

    @GeoSpatialIndexed(type = GeoSpatialIndexType.GEO_2DSPHERE)
    private double[] location; // [latitude, longitude]

    // New field to store associated boards
    @DBRef(lazy = true)
    private List<Board> boards = new ArrayList<>();

    private String displayPin;

    private Double price;

    // Ensure location array is initialized before setting values
    public void setLatitude(Double latitude) {
        if (latitude != null) {
            if (this.location == null) {
                this.location = new double[2]; // Initialize the location array if null
            }
            this.location[0] = latitude;  // Assuming location[0] is latitude
        }
    }

    public void setLongitude(Double longitude) {
        if (longitude != null) {
            if (this.location == null) {
                this.location = new double[2]; // Initialize the location array if null
            }
            this.location[1] = longitude;  // Assuming location[1] is longitude
        }
    }

    // Set both latitude and longitude in the location array
    public void setLocation(Double latitude, Double longitude) {
        if (latitude != null && longitude != null) {
            this.location = new double[] {latitude, longitude};  // Initialize and set the location array
        }
    }

    private int likes = 0;     // Added field for likes

    private int dislikes = 0;  // Added field for dislikes

    // New fields to maintain who liked/disliked the display
    @DBRef(lazy = true)
    private Set<User> likedBy = new HashSet<>(); // Users who liked this display

    @DBRef(lazy = true)
    private Set<User> dislikedBy = new HashSet<>(); // Users who disliked this display

    // Methods to manage likes
    public void addLike(User user) {
        if (!likedBy.contains(user)) {
            likedBy.add(user);
            likes++;
            dislikedBy.remove(user); // Remove the user from dislikedBy if they previously disliked
            dislikes = Math.max(0, dislikes - 1); // Ensure dislikes do not go negative
        }
    }

    public void removeLike(User user) {
        if (likedBy.remove(user)) {
            likes = Math.max(0, likes - 1); // Ensure likes do not go negative
        }
    }

    public void addDislike(User user) {
        if (!dislikedBy.contains(user)) {
            dislikedBy.add(user);
            dislikes++;
            likedBy.remove(user); // Remove the user from likedBy if they previously liked
            likes = Math.max(0, likes - 1); // Ensure likes do not go negative
        }
    }

    public void removeDislike(User user) {
        if (dislikedBy.remove(user)) {
            dislikes = Math.max(0, dislikes - 1); // Ensure dislikes do not go negative
        }
    }

    // Methods to manage ratings
    public void addRating(Rating rating) {
        // Check if the user has already rated the same item (display)
        if (ratings.stream().noneMatch(r -> r.getRatedBy().equals(rating.getRatedBy()))) {
            ratings.add(rating); // Add new rating
        } else {
            // Replace existing rating from the same user
            ratings.replaceAll(r -> r.getRatedBy().equals(rating.getRatedBy()) ? rating : r);
        }
    }

    public void removeRating(User user) {
        ratings.removeIf(r -> r.getRatedBy().equals(user)); // Remove rating by the user
    }

    // Calculate average rating
    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream().mapToDouble(Rating::getValue).average().orElse(0.0);
    }
}
