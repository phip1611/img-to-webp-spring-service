package de.phip1611.img_to_webp.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;
import org.springframework.web.servlet.mvc.WebContentInterceptor;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheStaticResourcesConfiguration implements WebMvcConfigurer {

// Doesn't work
//    @Override
//    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
//        registry.addResourceHandler(
//                "/resources/**/*.css",
//                "/resources/**/*.js",
//                "/resources/**/*.png",
//                "/resources/**/*.jpg",
//                "/resources/**/*.jpeg",
//                "/resources/**/*.ico",
//                "/resources/**/*.webp",
//                "/resources/**/*.txt")
//                .addResourceLocations("/resources/static")
//                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS)
//                        .noTransform()
//                        .mustRevalidate());
//    }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        WebContentInterceptor interceptor = new WebContentInterceptor();
        interceptor.addCacheMapping(
                CacheControl.maxAge(7, TimeUnit.DAYS)
                .noTransform()
                .mustRevalidate(),
                // this is the path from the URL/public interface
                // not the path internally in the application/in classpath
                "/res/**",
                "favicon.ico");
        registry.addInterceptor(interceptor);
    }
}
