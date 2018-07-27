/*
   Copyright ©: Philipp Schuster (2017)
   https://phip1611.de / phip1611@gmail.com
*/
package de.phip1611.img_to_webp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * Konfiguration von CORS.
 */
@Configuration
public class CorsWebConfig implements WebMvcConfigurer {
    private String[] allowedOrigins = {"*"};

    private String[] allowedMethods = {
            "GET",
            "POST",
            "PUT",
            "DELETE",
            "PATCH"
    };

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods(allowedMethods).allowedOrigins(allowedOrigins);
    }
}
