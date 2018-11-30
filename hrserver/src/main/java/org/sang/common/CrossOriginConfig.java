package org.sang.common;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.servlet.config.annotation.*;

@Configuration
public class CrossOriginConfig extends WebMvcConfigurerAdapter {

    // During development, webpack server runs on localhost:8080
    // Make the browser happy by returning CORS headers in this case
    @Bean
//    @Profile("dev")
    public WebMvcConfigurer corsConfigurer() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addCorsMappings(CorsRegistry registry) {
                registry.addMapping("/**")
                    .allowedOrigins(CorsConfiguration.ALL)
                    .allowedHeaders(CorsConfiguration.ALL)
                    .allowedMethods(CorsConfiguration.ALL)
                    .allowCredentials(true);
            }
        };
    }

}

