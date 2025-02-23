package com.myboard.userservice.entity;

import com.myboard.userservice.controller.model.common.MediaFile;
import com.myboard.userservice.types.StatusType;
import jakarta.persistence.Transient;
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

    @Transient
    private boolean likedByCurrentUser = false;

    @Transient
    private boolean dislikedByCurrentUser = false;

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

    // New fields to maintain who liked/disliked the display
    @DBRef(lazy = true)
    private Set<User> likedBy = new HashSet<>(); // Users who liked this display

    @DBRef(lazy = true)
    private Set<User> dislikedBy = new HashSet<>(); // Users who disliked this display

    // Methods to manage likes/dislikes using sets for counts
    public void addLike(User user) {
        if (!likedBy.contains(user)) {
            likedBy.add(user);
            // If user was in dislikedBy, remove them
            dislikedBy.remove(user);
        }
    }

    public void removeLike(User user) {
        likedBy.remove(user);
    }

    public void addDislike(User user) {
        if (!dislikedBy.contains(user)) {
            dislikedBy.add(user);
            // If user was in likedBy, remove them
            likedBy.remove(user);
        }
    }

    public void removeDislike(User user) {
        dislikedBy.remove(user);
    }

    // Methods to manage ratings
    public void addRating(Rating rating) {
        // Check if the user has already rated the display
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

    // Get the number of likes directly from the likedBy set
    public Integer getNumberOfLikes() {
        return likedBy.size();
    }

    // Get the number of dislikes directly from the dislikedBy set
    public Integer getNumberOfDislikes() {
        return dislikedBy.size();
    }

    // Check if a specific user has liked or disliked the display
    public boolean isLikedByUser(User user) {
        return likedBy.contains(user);
    }

    public boolean isDislikedByUser(User user) {
        return dislikedBy.contains(user);
    }

    public void updateUserReaction(User currentUser) {
        this.likedByCurrentUser = isLikedByUser(currentUser);
        this.dislikedByCurrentUser = isDislikedByUser(currentUser);
    }


}
