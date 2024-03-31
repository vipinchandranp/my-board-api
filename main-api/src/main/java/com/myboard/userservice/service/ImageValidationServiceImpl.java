package com.myboard.userservice.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import com.google.cloud.vision.v1.AnnotateImageRequest;
import com.google.cloud.vision.v1.AnnotateImageResponse;
import com.google.cloud.vision.v1.BatchAnnotateImagesResponse;
import com.google.cloud.vision.v1.EntityAnnotation;
import com.google.cloud.vision.v1.Feature;
import com.google.cloud.vision.v1.Image;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.protobuf.ByteString;

public class ImageValidationServiceImpl implements ImageValidationService {

	public String analyzeImage(byte[] imageData) throws IOException {
		try (ImageAnnotatorClient vision = ImageAnnotatorClient.create()) {
			ByteString imgBytes = ByteString.copyFrom(imageData);
			Image img = Image.newBuilder().setContent(imgBytes).build();
			Feature feat = Feature.newBuilder().setType(Feature.Type.LABEL_DETECTION).build();
			AnnotateImageRequest request = AnnotateImageRequest.newBuilder().addFeatures(feat).setImage(img).build();
			List<AnnotateImageRequest> requests = new ArrayList<>();
			requests.add(request);

			// Perform label detection on the image file
			BatchAnnotateImagesResponse response = vision.batchAnnotateImages(requests);
			List<AnnotateImageResponse> responses = response.getResponsesList();

			StringBuilder resultBuilder = new StringBuilder();
			for (AnnotateImageResponse res : responses) {
				if (res.hasError()) {
					resultBuilder.append("Error: ").append(res.getError().getMessage()).append("\n");
				} else {
					for (EntityAnnotation annotation : res.getLabelAnnotationsList()) {
						annotation.getAllFields()
								.forEach((k, v) -> resultBuilder.append(String.format("%s : %s%n", k, v.toString())));
					}
				}
			}
			return resultBuilder.toString();
		}
	}

	public byte[] readImageAsBytes(String imagePath) throws IOException {
		Path path = Path.of(imagePath);
		return Files.readAllBytes(path);
	}

	private static void setGoogleCredentials(String serviceAccountKeyFile) {
		// Set the environment variable GOOGLE_APPLICATION_CREDENTIALS
		// to point to the service account key file
		try {
			System.setProperty("GOOGLE_APPLICATION_CREDENTIALS", serviceAccountKeyFile);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) throws IOException {
		setGoogleCredentials("C:\\Users\\vipin\\AppData\\Roaming\\gcloud\\application_default_credentials.json");
		ImageValidationServiceImpl imageValidationServiceImpl = new ImageValidationServiceImpl();
		byte[] imageAsBytes = imageValidationServiceImpl.readImageAsBytes(
				"C:/Users/vipin/development/myboard-uploaded-data/board/vipin/vipin_65beae6f0f07857726f137a8_05ae1483-8aed-4ddb-af33-f72283fe77de_20240204_025151.jpg");
		imageValidationServiceImpl.analyzeImage(imageAsBytes);
	}
}
