package com.minicloud.gateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;

import jakarta.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class RateLimitFilter extends AbstractGatewayFilterFactory<RateLimitFilter.Config> {

    private static final Logger log = LoggerFactory.getLogger(RateLimitFilter.class);
    private final ConcurrentHashMap<String, AtomicInteger> requests = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();

    public RateLimitFilter() {
        super(Config.class);
    }

    @PostConstruct
    public void startResetScheduler() {
        // Reset all counters every 60 seconds (fixed-window rate limiting)
        scheduler.scheduleAtFixedRate(() -> {
            if (!requests.isEmpty()) {
                log.debug("Rate-limit window reset: clearing {} tracked IPs", requests.size());
                requests.clear();
            }
        }, 60, 60, TimeUnit.SECONDS);
    }

    @Override
    public GatewayFilter apply(Config config) {
        return (exchange, chain) -> {
            String ip = exchange.getRequest().getRemoteAddress().getAddress().getHostAddress();
            int count = requests.computeIfAbsent(ip, k -> new AtomicInteger(0)).incrementAndGet();

            if (count > config.getLimit()) {
                exchange.getResponse().setStatusCode(HttpStatus.TOO_MANY_REQUESTS);
                return exchange.getResponse().setComplete();
            }

            return chain.filter(exchange);
        };
    }

    public static class Config {
        private int limit = 100;
        public int getLimit() { return limit; }
        public void setLimit(int limit) { this.limit = limit; }
    }
}
