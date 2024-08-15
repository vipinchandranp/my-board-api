package com.myboard.userservice.mongo.config;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.myboard.userservice.controller.apimodel.BaseResponse;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;
import org.springframework.context.annotation.ScopedProxyMode;
import org.springframework.data.mongodb.MongoDatabaseFactory;
import org.springframework.data.mongodb.core.convert.MappingMongoConverter;
import org.springframework.data.mongodb.gridfs.GridFsTemplate;
import org.springframework.web.context.WebApplicationContext;

@Configuration
public class BaseConfig {

	@Bean
	public GridFsTemplate gridFsTemplate(MongoDatabaseFactory mongoDatabaseFactory,
			MappingMongoConverter mappingMongoConverter) {
		return new GridFsTemplate(mongoDatabaseFactory, mappingMongoConverter);
	}

	@Bean
	@Scope(value = WebApplicationContext.SCOPE_REQUEST, proxyMode = ScopedProxyMode.TARGET_CLASS)
	public <T> BaseResponse<T> baseResponse() {
		return new BaseResponse<>();
	}

	@Bean
	public ObjectMapper objectMapper() {
		ObjectMapper mapper = new ObjectMapper();
		mapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);
		return mapper;
	}

}
