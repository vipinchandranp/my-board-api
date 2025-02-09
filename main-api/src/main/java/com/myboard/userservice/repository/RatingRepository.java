package com.myboard.userservice.repository;

import com.myboard.userservice.entity.Rating;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RatingRepository extends MongoRepository<Rating, String> {

    // Find all ratings for a specific item (Board or Display) based on itemType and itemID
    List<Rating> findByItemTypeAndItemID(String itemType, String itemID);

    // Find a rating by its ID
    Optional<Rating> findById(String id);

    // Find a rating by the ratedBy user
    List<Rating> findByRatedById(String userId);

    // Custom query to check if the user has rated the item (board or display) already
    Optional<Rating> findFirstByItemTypeAndItemIDAndRatedById(String itemType, String itemID, String userId);

    // You can add more custom queries here as needed
}
