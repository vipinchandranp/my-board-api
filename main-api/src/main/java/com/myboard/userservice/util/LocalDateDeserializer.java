package com.myboard.userservice.util;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

public class LocalDateDeserializer extends JsonDeserializer<String> {

	@Override
	public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException {
		String dateString = jsonParser.getText();
		// Trim the time part
		int indexOfT = dateString.indexOf('T');
		if (indexOfT != -1) {
			dateString = dateString.substring(0, indexOfT);
		}
		return dateString;
	}
}
