package com.myboard.userservice.service.google;

import com.google.cloud.vision.v1.*;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

@Service
public class GoogleVisionService {

    private final ImageAnnotatorClient visionClient;

    public GoogleVisionService(ImageAnnotatorClient visionClient) {
        this.visionClient = visionClient;
    }

    public String detectExplicitContent(byte[] imageBytes) throws IOException {
        // Convert image bytes to ByteString
        ByteString imgBytes = ByteString.copyFrom(imageBytes);

        // Create an Image object
        Image image = Image.newBuilder().setContent(imgBytes).build();

        // Configure features for SafeSearch Detection
        Feature feature = Feature.newBuilder()
                .setType(Feature.Type.SAFE_SEARCH_DETECTION)
                .build();

        // Build the request
        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feature)
                .setImage(image)
                .build();

        // Call the Vision API
        List<AnnotateImageResponse> responses = visionClient
                .batchAnnotateImages(List.of(request))
                .getResponsesList();

        for (AnnotateImageResponse response : responses) {
            if (response.hasError()) {
                throw new RuntimeException("Error during Vision API call: " + response.getError().getMessage());
            }

            // SafeSearch results
            SafeSearchAnnotation annotation = response.getSafeSearchAnnotation();
            return String.format(
                    "Adult: %s\nViolence: %s\nRacy: %s\nSpoof: %s\nMedical: %s",
                    annotation.getAdult(),
                    annotation.getViolence(),
                    annotation.getRacy(),
                    annotation.getSpoof(),
                    annotation.getMedical()
            );
        }

        return "No response from Vision API.";
    }
}
