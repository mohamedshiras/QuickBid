package com.example.QuickBid.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**")
                .allowedOriginPatterns(
                        "http://localhost:3000",
                        "http://127.0.0.1:3000",
                        "http://localhost:8080",
                        "http://127.0.0.1:8080"
                        // Add your production domain when ready
                        // "https://your-production-domain.com"
                )
                .allowedMethods("GET", "POST", "PUT", "DELETE", "OPTIONS", "PATCH")
                .allowedHeaders(
                        "Origin",
                        "Content-Type",
                        "Accept",
//                        "Authorization",
                        "X-Requested-With",
                        "X-CSRF-Token"
                )
                .exposedHeaders("Authorization", "Content-Disposition")
                .allowCredentials(true) // IMPORTANT: Required for session-based auth
                .maxAge(3600); // 1 hour
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        // Order matters - more specific patterns should come first
        registry.addResourceHandler("/css/**")
                .addResourceLocations("classpath:/static/css/")
                .setCachePeriod(3600);

        registry.addResourceHandler("/js/**")
                .addResourceLocations("classpath:/static/js/")
                .setCachePeriod(3600);

        registry.addResourceHandler("/images/**")
                .addResourceLocations("classpath:/static/images/")
                .setCachePeriod(3600);

        registry.addResourceHandler("/fonts/**")
                .addResourceLocations("classpath:/static/fonts/")
                .setCachePeriod(86400); // 24 hours for fonts

        // Handle favicon and other root-level files
        registry.addResourceHandler("/favicon.ico")
                .addResourceLocations("classpath:/static/favicon.ico")
                .setCachePeriod(86400);

        // General static content - be more specific than /**
        registry.addResourceHandler("/static/**")
                .addResourceLocations("classpath:/static/")
                .setCachePeriod(3600);
    }
}