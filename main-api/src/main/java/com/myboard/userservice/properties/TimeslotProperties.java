package com.myboard.userservice.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix = "myboard.timeslots")
@Data
public class TimeslotProperties {
    private int defaultDuration;
}
