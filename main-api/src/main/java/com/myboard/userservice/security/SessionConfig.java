package com.myboard.userservice.security;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.session.data.redis.config.annotation.web.http.EnableRedisHttpSession;

@Configuration
public class SessionConfig {

    @Value("${spring.session.redis.enabled:false}")
    private boolean isRedisEnabled;

    @Configuration
    @ConditionalOnProperty(name = "spring.session.redis.enabled", havingValue = "true", matchIfMissing = true)
    @EnableRedisHttpSession  // This will enable Redis-based session management if the property is true
    static class RedisSessionConfig {
    }
}
