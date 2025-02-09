package com.myboard.userservice.entity;

import com.myboard.userservice.types.ItemType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "ratings")
public class Rating extends Base{
    private ItemType itemType;
    private String itemID; // Display or BoardID
    private Double value;
    private User ratedBy; // userId
    private Long timestamp;   // Timestamp when the rating was provided
}
