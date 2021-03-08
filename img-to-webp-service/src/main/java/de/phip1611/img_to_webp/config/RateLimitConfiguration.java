package de.phip1611.img_to_webp.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

@Configuration
@ConfigurationProperties(prefix = "rate-limit")
@Validated
public class RateLimitConfiguration {

    /**
     * Time how many requests are allowed in a specified interval.
     * The interval is specified by {@link #rateLimitIntervalSeconds}.
     */
    @NotNull
    @Min(1)
    private int rateLimitRequestsPerInterval;

    /**
     * Time in seconds the limit specified by {@link #rateLimitRequestsPerInterval}
     * should be enforced.
     */
    @NotNull
    @Min(30) // this is also the interval of the scheduler
    private int rateLimitIntervalSeconds;

    public RateLimitConfiguration() {
    }

    public int getRateLimitRequestsPerInterval() {
        return rateLimitRequestsPerInterval;
    }

    public void setRateLimitRequestsPerInterval(int rateLimitRequestsPerInterval) {
        this.rateLimitRequestsPerInterval = rateLimitRequestsPerInterval;
    }

    public int getRateLimitIntervalSeconds() {
        return rateLimitIntervalSeconds;
    }

    public void setRateLimitIntervalSeconds(int rateLimitIntervalSeconds) {
        this.rateLimitIntervalSeconds = rateLimitIntervalSeconds;
    }
}
