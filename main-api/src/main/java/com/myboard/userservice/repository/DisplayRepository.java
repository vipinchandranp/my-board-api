package com.myboard.userservice.repository;

import com.myboard.userservice.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.geo.Point;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.mongodb.repository.MongoRepository;

import com.myboard.userservice.entity.Display;
import java.util.List;
import java.util.Optional;

public interface DisplayRepository extends MongoRepository<Display, String>, CommonFilteredMongoRepository<Display>  {

    boolean existsByName(String name);

    List<Display> findByCreatedBy(User user);

    Optional<Display> findByName(String dipsplayName);

    Page<Display> findAll(Pageable pageable);

    Optional<Display> findByCreatedByAndDisplayPin(User createdBy, String displayPin);

    Optional<Display> findByDisplayPin(String displayPin);

    Optional<Display> findByDisplayPinAndCreatedBy(String displayPin, User createdByUser);

    @Query("{ 'location': { $near: { $geometry: { type: 'Point', coordinates: [?0, ?1] }, $maxDistance: ?2 } } }")
    List<Display> findNearbyDisplays(double longitude, double latitude, double radius);

    Page<Display> findByNameContainingIgnoreCase(String name, Pageable pageable);

}