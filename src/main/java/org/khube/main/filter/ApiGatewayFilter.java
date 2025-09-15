package org.khube.main.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.core.Ordered;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Component
public class ApiGatewayFilter implements GatewayFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(ApiGatewayFilter.class);

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        exchange.getRequest()
                .mutate()
                .header("X-API-GATEWAY", "ApiGatewayFilter")
                .header("X-Custom-Header", "MyValue")
                .build();

        return chain.filter(exchange).then(
                Mono.fromRunnable(() ->
                        log.info("Response status: {}", exchange.getResponse().getStatusCode())
        ));
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
