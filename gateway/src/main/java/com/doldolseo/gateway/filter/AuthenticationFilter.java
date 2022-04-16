package com.doldolseo.gateway.filter;

import com.doldolseo.gateway.route.RouterValidator;
import com.doldolseo.utils.JwtUtil;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;
import java.time.LocalDateTime;

@RefreshScope
@Component
public class AuthenticationFilter implements GatewayFilter {
    @Autowired
    RouterValidator routerValidator;
    @Autowired
    JwtUtil jwtUtil;

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
        ServerHttpRequest request = exchange.getRequest();

        if (routerValidator.isAuthRequire.test(request)) {
            if (this.isAuthMissing(request))
                return this.onError(
                        exchange,
                        "Authorization header is missing in request"
                );

            String requestTokenHeader = request
                    .getHeaders()
                    .getOrEmpty("Authorization")
                    .toString();
            final String token = requestTokenHeader
                    .substring(8, requestTokenHeader.length() - 1);

            if (token.equals("null")) {
                return this.onError(
                        exchange,
                        "Token is null"
                );
            }

            try {
                if (jwtUtil.isInvalid(token))
                    return this.onError(
                            exchange,
                            "Authorization Fail"
                    );

                if (jwtUtil.isTokenExpired(token))
                    return this.onError(
                            exchange,
                            "Token Expired"
                    );

            } catch (JwtException e) {
                System.out.println(e.getMessage());
                return this.onError(
                        exchange,
                        "Authorization Fail"
                );
            }

            this.populateRequestWithHeaders(exchange, token);
        }

        return chain.filter(exchange);
    }

    private Mono<Void> onError(ServerWebExchange exchange, String err) {
        ServerHttpResponse response = exchange.getResponse();
        response.setStatusCode(HttpStatus.UNAUTHORIZED);
        System.out.println(LocalDateTime.now() + " [인증 실패]-" + err);
        return response.setComplete();
    }

    private boolean isAuthMissing(ServerHttpRequest request) {
        return !request.getHeaders().containsKey("Authorization");
    }

    private void populateRequestWithHeaders(ServerWebExchange exchange, String token) {
        Claims claims = jwtUtil.getAllClaimsFromToken(token);
        exchange.getRequest().mutate()
                .header("userId", claims.getId())
                .header("role", String.valueOf(claims.get("role")))
                .build();
    }
}