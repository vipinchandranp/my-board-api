package com.myboard.userservice.entity;

import com.myboard.userservice.types.StarRateType;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

@EqualsAndHashCode(callSuper = true)
@Data
@Document(collection = "rating")
public class Rating extends Base{

    private StarRateType rating; // The rating value (e.g., 1 to 5 stars)

    @DBRef
    private User user; // Reference to the user who provided the rating

}