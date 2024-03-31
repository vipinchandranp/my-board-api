package com.myboard.userservice.dto;

import jdk.jfr.DataAmount;
import lombok.Data;

import java.util.List;

@Data
public class MapsPlaceDTO {
    private String businessStatus;
    private String formattedAddress;
    private GeometryDTO geometry;
    private String icon;
    private String iconBackgroundColor;
    private String iconMaskBaseUri;
    private String name;
    private OpeningHoursDTO openingHours;
    private List<PhotoDTO> photos;
    private String placeId;
    private PlusCodeDTO plusCode;
    private double rating;
    private String reference;
    private List<String> types;
    private int userRatingsTotal;

    @Data
    public static class GeometryDTO {
        private LocationDTO location;
        private ViewportDTO viewport;
    }

    @Data
    public static class LocationDTO {
        private double lat;
        private double lng;
    }

    @Data
    public static class ViewportDTO {
        private LocationDTO northeast;
        private LocationDTO southwest;
    }

    @Data
    public static class OpeningHoursDTO {
        private boolean openNow;
    }

    @Data
    public static class PhotoDTO {
        private int height;
        private List<String> htmlAttributions;
        private String photoReference;
        private int width;
    }

    @Data
    public static class PlusCodeDTO {
        private String compoundCode;
        private String globalCode;
    }
}
