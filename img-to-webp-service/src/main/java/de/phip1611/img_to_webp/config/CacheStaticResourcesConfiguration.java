package de.phip1611.img_to_webp.config;

import org.springframework.boot.autoconfigure.web.WebProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.CacheControl;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.concurrent.TimeUnit;

@Configuration
public class CacheStaticResourcesConfiguration implements WebMvcConfigurer {

    /**
     * We provide a custom configuration which resolves URL-Requests to static files in the
     * classpath (src/main/resources directory).
     *
     * This overloads a default configuration retrieved at least partly from
     * {@link WebProperties.Resources#getStaticLocations()}.
     *
     * @param registry ResourceHandlerRegistry
     */
    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        /*
         * BE AWARE HERE:
         *
         * .addResourceHandler(): URL Paths
         * .addResourceLocations(): Paths in Classpath to look for file
         *   root "/" refers to src/main/resources
         *   For configuration example, see:
         *     org.springframework.boot.autoconfigure.web.WebProperties.Resources().getStaticLocations()
         *
         * .addResourceLocations("classpath:/static/")
         *   =>
         *      addResourceHandler("/**")
         *      => GET /res/css/main.css
         *         => resolved as: "classpath:/static/res/css/main.css"
         *      BUT
         *      addResourceHandler("/res/**")
         *      => GET /res/css/main.css
         *            (spring only appends the ** to the value from
         *             addResourceLocations())
         *         => resolved as: "classpath:/static/css/main.css"
         */

        registry
                .addResourceHandler("/favicon.ico")
                // trailing slash is important!
                .addResourceLocations("classpath:/static/")
                .setCacheControl(CacheControl.maxAge(1, TimeUnit.DAYS)
                        .noTransform()
                        .mustRevalidate());

        registry
                .addResourceHandler("/res/**")
                // trailing slash is important!
                .addResourceLocations("classpath:/static/res/")
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS)
                        .noTransform()
                        .mustRevalidate());

        registry
                .addResourceHandler("/images/**")
                // trailing slash is important!
                .addResourceLocations("classpath:/static/images/")
                .setCacheControl(CacheControl.maxAge(7, TimeUnit.DAYS)
                        .noTransform()
                        .mustRevalidate());
    }
}
