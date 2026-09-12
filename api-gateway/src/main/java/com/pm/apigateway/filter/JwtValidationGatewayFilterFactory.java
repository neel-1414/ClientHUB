package com.pm.apigateway.filter;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class JwtValidationGatewayFilterFactory extends AbstractGatewayFilterFactory<Object> {

    private final WebClient webClient;
    public JwtValidationGatewayFilterFactory(WebClient webClient,
            @Value("${auth.service.url}") String authServiceUrl) {
        this.webClient = webClient.mutate().baseUrl(authServiceUrl).build();

    }
    //exchange variable holds all the property passed by api gateway
    @Override
    public GatewayFilter apply(Object config) {
        return (exchange, chain) -> {
            String token = exchange.getRequest().getHeaders().getFirst(HttpHeaders.AUTHORIZATION);
            if(token == null || !token.startsWith("Bearer ")) {
                exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
                return exchange.getResponse().setComplete();
                //returns to frontend if the token is invalid
            }
           return webClient.get()//get request
                    .uri("/validate") // get request on this uri
                    .header(HttpHeaders.AUTHORIZATION, token)/*taking authorization header from prev req and
                                                                adding that token to this request*/
                    .retrieve()//retrieves the response
                    .toBodilessEntity()//the response has not body no expectations
                    .then(chain.filter(exchange));//continue the request
        };
    }
}
