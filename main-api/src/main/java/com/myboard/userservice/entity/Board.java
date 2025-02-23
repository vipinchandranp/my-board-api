package com.myboard.userservice.entity;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.myboard.userservice.controller.model.common.MediaFile;
import com.myboard.userservice.types.StatusType;
import jakarta.persistence.Transient;
import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "board")
public class Board extends Base {

    private String name;

    private List<Rating> ratings = new ArrayList<>(); // List to hold ratings for the board

    private List<Comment> comments = new ArrayList<>(); // List to hold comments for the board

    private List<MediaFile> mediaFiles = new ArrayList<>();

    private StatusType status = StatusType.WAITING_FOR_APPROVAL;

    // Fields to maintain which users liked/disliked the board
    @DBRef(lazy = true)
    private Set<User> likedBy = new HashSet<>(); // Users who liked this board

    @DBRef(lazy = true)
    private Set<User> dislikedBy = new HashSet<>(); // Users who disliked this board

    @Transient
    private boolean likedByCurrentUser = false;

    @Transient
    private boolean dislikedByCurrentUser = false;


    // Methods to manage likes
    public void addLike(User user) {
        if (!likedBy.contains(user)) {
            likedBy.add(user);
            // If the user had previously disliked the board, remove the dislike
            if (dislikedBy.remove(user)) {
                // No need to adjust a separate count field if using size()
            }
        }
    }

    public void removeLike(User user) {
        likedBy.remove(user);
    }

    // Methods to manage dislikes
    public void addDislike(User user) {
        if (!dislikedBy.contains(user)) {
            dislikedBy.add(user);
            // If the user had previously liked the board, remove the like
            if (likedBy.remove(user)) {
                // No need to adjust a separate count field if using size()
            }
        }
    }

    public void removeDislike(User user) {
        dislikedBy.remove(user);
    }

    // Methods to manage ratings
    public void addRating(Rating rating) {
        // Check if the user has already rated the board
        if (ratings.stream().noneMatch(r -> r.getRatedBy().equals(rating.getRatedBy()))) {
            ratings.add(rating);
        } else {
            // Replace existing rating from the same user
            ratings.replaceAll(r -> r.getRatedBy().equals(rating.getRatedBy()) ? rating : r);
        }
    }

    public void removeRating(User user) {
        ratings.removeIf(r -> r.getRatedBy().equals(user));
    }

    // Calculate average rating
    public double getAverageRating() {
        if (ratings.isEmpty()) {
            return 0.0;
        }
        return ratings.stream().mapToDouble(Rating::getValue).average().orElse(0.0);
    }

    // Get count of likes and dislikes from the sets
    public Integer getLikeCount() {
        return likedBy.size();
    }

    public Integer getDislikeCount() {
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
