package de.phip1611.img_to_webp.service.api;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.TOO_MANY_REQUESTS, reason = "Rate limit exceeded!")
public class RateLimitException extends RuntimeException {
}
