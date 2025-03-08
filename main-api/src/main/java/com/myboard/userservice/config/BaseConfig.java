package com.myboard.userservice.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.google.api.gax.rpc.FixedHeaderProvider;
import com.google.cloud.vision.v1.ImageAnnotatorClient;
import com.google.cloud.vision.v1.ImageAnnotatorSettings;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

@Configuration
public class BaseConfig {

	@Value("${myboard.google.apikey}")
	private String apiKey;


	@Bean
	public GridFsTemplate gridFsTemplate(MongoDatabaseFactory mongoDatabaseFactory,
			MappingMongoConverter mappingMongoConverter) {
		return new GridFsTemplate(mongoDatabaseFactory, mappingMongoConverter);
	}


	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		mapper.registerModule(new JavaTimeModule());
		return mapper;
	}


	@Bean
	public MessageSource messageSource() {
		ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
		messageSource.setBasename("messages");
		messageSource.setDefaultEncoding("UTF-8");
		return messageSource;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Bean
	public ImageAnnotatorClient imageAnnotatorClient() throws IOException {
		// Use the API key to configure the client
		ImageAnnotatorSettings settings = ImageAnnotatorSettings.newBuilder()
				.setHeaderProvider(FixedHeaderProvider.create("Authorization", "Bearer " + apiKey))
				.build();
		return ImageAnnotatorClient.create(settings);
	}
}
