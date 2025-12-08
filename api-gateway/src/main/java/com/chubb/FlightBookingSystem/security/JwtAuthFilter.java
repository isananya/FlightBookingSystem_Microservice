package com.chubb.FlightBookingSystem.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpCookie;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;

import io.jsonwebtoken.Claims;
import reactor.core.publisher.Mono;

@Component
public class JwtAuthFilter extends AbstractGatewayFilterFactory<JwtAuthFilter.Config> {

    public JwtAuthFilter(JwtUtil jwtUtil) {
        super(Config.class);
        this.jwtUtil = jwtUtil;
    }

    private final JwtUtil jwtUtil;

    public static class Config {
        private String requiredRole;

        public String getRequiredRole() { return requiredRole; }
        public void setRequiredRole(String requiredRole) { this.requiredRole = requiredRole; }
    }

    @Override
    public GatewayFilter apply(Config config) {
    	System.out.println(">>> Route matched filter config: requiredRole=" + config.requiredRole);

        return (exchange, chain) -> {
        	System.out.println(">>> JwtAuthFilter triggered for request: " 
                    + exchange.getRequest().getURI());

            HttpCookie cookie = exchange.getRequest()
                    .getCookies()
                    .getFirst("jwt_token");

            if (cookie == null) {
                return unauthorized(exchange);
            }

            try {
                Claims claims = jwtUtil.validateToken(cookie.getValue());
                String userRole = claims.get("role", String.class);

                if (config.requiredRole != null &&
                    !config.requiredRole.equalsIgnoreCase(userRole)) {

                    exchange.getResponse().setStatusCode(HttpStatus.FORBIDDEN);
                    return exchange.getResponse().setComplete();
                }
                System.out.println(">>> Extracted role = " + userRole);
            	System.out.println(">>> Required role = " + config.getRequiredRole());
                return chain.filter(exchange);

            } catch (Exception e) {
                return unauthorized(exchange);
            }
            
        };
    }

    private Mono<Void> unauthorized(ServerWebExchange exchange) {
        exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
        return exchange.getResponse().setComplete();
    }
}
