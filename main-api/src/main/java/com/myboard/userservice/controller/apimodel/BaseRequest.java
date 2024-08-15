package com.myboard.userservice.controller.apimodel;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;
import org.springframework.stereotype.Component;

@Data
public class BaseRequest {

    private JsonNode data;

}
