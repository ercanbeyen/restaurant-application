package com.ercanbeyen.restaurantapplication.config;

import com.ercanbeyen.restaurantapplication.constant.message.LogMessage;
import com.ercanbeyen.restaurantapplication.dto.ProfileProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Slf4j
@Configuration
public class ProfileConfig {
    @Value("${app.profile}")
    private String profile;
    @Value("${server.port}")
    private Integer port;
    @Value("${spring.datasource.url}")
    private String databaseUrl;

    @Bean("profileProperties")
    @Profile("dev")
    public ProfileProperties devProfile() {
        return getProfileProperties();
    }

    @Bean("profileProperties")
    @Profile("test")
    public ProfileProperties testProfile() {
        return getProfileProperties();
    }

    private ProfileProperties getProfileProperties() {
        log.info(LogMessage.PROFILE_PROPERTIES, profile, port, databaseUrl);
        return new ProfileProperties(profile, port, databaseUrl);
    }
}
