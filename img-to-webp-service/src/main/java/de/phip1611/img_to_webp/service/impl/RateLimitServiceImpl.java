package de.phip1611.img_to_webp.service.impl;

import de.phip1611.img_to_webp.config.RateLimitConfiguration;
import de.phip1611.img_to_webp.service.api.RateLimitException;
import de.phip1611.img_to_webp.service.api.RateLimitService;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.health.Health;
import org.springframework.boot.actuate.health.HealthIndicator;
import org.springframework.boot.actuate.health.Status;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import jakarta.servlet.http.HttpServletRequest;
import java.time.LocalDateTime;
import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

@Service
public class RateLimitServiceImpl implements RateLimitService, HealthIndicator {

    private static final Logger LOGGER = LoggerFactory.getLogger(RateLimitServiceImpl.class);

    private final RateLimitConfiguration config;

    private final SortedMap<String, List<LocalDateTime>> ipHashToAccessTimesMap = new TreeMap<>();

    private final ReentrantLock lock = new ReentrantLock();

    public RateLimitServiceImpl(RateLimitConfiguration config) {
        this.config = config;
    }

    @Override
    public synchronized void assertRequest(HttpServletRequest request) throws RateLimitException {
        lock.lock();

        var hashedIp = getIpHash(request);
        if (!ipHashToAccessTimesMap.containsKey(hashedIp)) {
            ipHashToAccessTimesMap.put(hashedIp, new ArrayList<>());
        }
        var list = ipHashToAccessTimesMap.get(hashedIp);

        var timeThreshold = LocalDateTime.now()
                .minusSeconds(
                        config.getRateLimitIntervalSeconds()
        );
        var count = (int) list.stream().filter(t -> t.isAfter(timeThreshold)).count();

        if (count >= config.getRateLimitRequestsPerInterval()) {
            LOGGER.debug("Rate Limit exceeded for ip hash {} - count is {}", hashedIp, count);
            lock.unlock();
            throw new RateLimitException();
        } else {
            list.add(LocalDateTime.now());
        }

        lock.unlock();
    }

    // once per 5 minutes
    @Scheduled(fixedDelay = 1000 * 60 * 5)
    private synchronized void deleteOld() {
        lock.lock();
        LOGGER.debug("scheduled deletion started running");

        var deleteIfOlderThreshold = LocalDateTime.now()
                .minusSeconds(
                        config.getRateLimitIntervalSeconds()
        );

        ipHashToAccessTimesMap.forEach((ipHash, accessTimes) -> {
            var deletions = 0;
            // list is strictly monotonic
            while (true) {
                if (accessTimes.isEmpty()) {
                    break;
                }

                var accessTime = accessTimes.get(0);
                if (accessTime.isBefore(deleteIfOlderThreshold)) {
                    accessTimes.remove(0);
                    deletions++;
                } else {
                    break;
                }
            }
            if (deletions > 0) {
                LOGGER.debug("Deleted {} outdated requests for IP-hash {}", deletions, ipHash);
            }
        });

        LOGGER.debug("scheduled deletion stopped running");
        lock.unlock();
    }

    private String getIpHash(HttpServletRequest request) {
        var ip = request.getRemoteAddr();
        return DigestUtils.sha256Hex(ip);
    }

    @Override
    public Health health() {
        Map<String, Object> details = new HashMap<>();
        details.put("description", "IP-Hash to latest access times");
        details.put("accesses", ipHashToAccessTimesMap);
        return new Health.Builder()
                .status(Status.UP)
                .withDetails(details)
                .build();
    }
}
