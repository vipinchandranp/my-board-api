package com.myboard.userservice.service;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.Likelihood;
import com.google.cloud.vision.v1.SafeSearchAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.protobuf.ByteString;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

@Service
public class NSWCheckService {

    // Method to analyze explicit content in an image using byte array and return true/false if any likelihood is above 90%
    public boolean detectExplicitContent(byte[] imageBytes) throws IOException {
        // Convert the byte array to ByteString
        ByteString byteString = ByteString.copyFrom(imageBytes);

        // Prepare the image and request configuration
        Image image = Image.newBuilder().setContent(byteString).build();
        Feature feature = Feature.newBuilder().setType(Feature.Type.SAFE_SEARCH_DETECTION).build();

        AnnotateImageRequest request = AnnotateImageRequest.newBuilder()
                .addFeatures(feature)
                .setImage(image)
                .build();

        // List to hold the request for batch processing
        List<AnnotateImageRequest> requests = new ArrayList<>();
        requests.add(request);

        try (ImageAnnotatorClient client = ImageAnnotatorClient.create()) {
            // Perform safe search detection
            AnnotateImageResponse response = client.batchAnnotateImages(requests).getResponses(0);
            if (response.hasError()) {
                throw new IOException("Error: " + response.getError().getMessage());
            }

            // Get the safe search annotation result
            SafeSearchAnnotation annotation = response.getSafeSearchAnnotation();
            Likelihood adultLikelihood = annotation.getAdult();
            Likelihood violenceLikelihood = annotation.getViolence();
            Likelihood racyLikelihood = annotation.getRacy();

            // Convert Likelihood to percentage value (1 = 20%, 2 = 40%, 3 = 60%, 4 = 80%, 5 = 100%)
            int adultPercentage = convertLikelihoodToPercentage(adultLikelihood);
            int violencePercentage = convertLikelihoodToPercentage(violenceLikelihood);
            int racyPercentage = convertLikelihoodToPercentage(racyLikelihood);

            // Check if any likelihood is above 90%
            return adultPercentage > 90 || violencePercentage > 90 || racyPercentage > 90;
        }
    }

    // Helper method to convert Likelihood to a percentage
    private int convertLikelihoodToPercentage(Likelihood likelihood) {
        switch (likelihood) {
            case VERY_UNLIKELY: return 0;
            case UNLIKELY: return 20;
            case POSSIBLE: return 40;
            case LIKELY: return 60;
            case VERY_LIKELY: return 100;
            default: return 0;
        }
    }
}
