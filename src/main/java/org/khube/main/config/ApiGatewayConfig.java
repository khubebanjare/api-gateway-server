package org.khube.main.config;

import org.khube.main.filter.ApiGatewayFilter;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

import java.util.Objects;

@Configuration
public class ApiGatewayConfig {

    @Bean
    public RedisRateLimiter redisRateLimiter() {
        // replenishRate = 2 req/sec, burstCapacity = 5
        return new RedisRateLimiter(2, 5);
    }

    @Bean
    public KeyResolver ipKeyResolver() {
        return exchange ->
                Mono.just(
                        Objects.requireNonNull(exchange.getRequest().getRemoteAddress()).getAddress().getHostAddress());
    }

    @Bean
    public RouteLocator customRouteLocator(
            RouteLocatorBuilder builder, ApiGatewayFilter apiGatewayFilter) {
        return builder.routes()
                .route("employee-service", r -> r
                        .path("/employee/**")
                        .filters(f -> f.stripPrefix(1)
                                .requestRateLimiter(config -> {
                                    config.setKeyResolver(ipKeyResolver());
                                    config.setRateLimiter(redisRateLimiter()); // <--- here
                                })
                                .filter(apiGatewayFilter)
                        )
                        .uri("lb://EMPLOYEE-SERVICE")
                )
                .route("department-service", r -> r
                        .path("/department/**")
                        .filters(f -> f.stripPrefix(1)
                                .requestRateLimiter(config -> {
                                    config.setKeyResolver(ipKeyResolver());
                                    config.setRateLimiter(redisRateLimiter()); // <--- here
                                })
                                .filter(apiGatewayFilter)
                        )
                        .uri("lb://DEPARTMENT-SERVICE")
                )
                .route("address-service", r -> r
                        .path("/address/**")
                        .filters(f -> f.stripPrefix(1)
                                .requestRateLimiter(config -> {
                                    config.setKeyResolver(ipKeyResolver());
                                    config.setRateLimiter(redisRateLimiter()); // <--- here
                                })
                                .filter(apiGatewayFilter)
                        )
                        .uri("lb://ADDRESS-SERVICE")
                )
                .route("company-service", r -> r
                        .path("/company/**")
                        .filters(f -> f.stripPrefix(1)
                                .requestRateLimiter(config -> {
                                    config.setKeyResolver(ipKeyResolver());
                                    config.setRateLimiter(redisRateLimiter()); // <--- here
                                })
                                .filter(apiGatewayFilter)
                        )
                        .uri("lb://COMPANY-SERVICE")
                )
                .build();
    }
}
