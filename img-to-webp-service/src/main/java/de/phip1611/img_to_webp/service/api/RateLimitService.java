package de.phip1611.img_to_webp.service.api;

import javax.servlet.http.HttpServletRequest;

public interface RateLimitService {

    void assertRequest(HttpServletRequest request) throws RateLimitException;

}
