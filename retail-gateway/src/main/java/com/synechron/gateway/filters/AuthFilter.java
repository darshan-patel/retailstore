package com.synechron.gateway.filters;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilter;
import org.springframework.cloud.gateway.filter.factory.AbstractGatewayFilterFactory;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import com.synechron.dto.AuthenticationResponseDto;
import com.synechron.exception.ApplicationRuntimeException;
import com.synechron.gateway.Exception.InvalidTokenStructureException;
import com.synechron.gateway.Exception.MissingAuthException;

/**
 * Authentication filter that will call before all API call. It will validate user token
 * @author darshan
 */
@Component
public class AuthFilter extends AbstractGatewayFilterFactory{
	
	public static class Config{
	}
	
	private final WebClient.Builder webClientBuilder;

	public AuthFilter(WebClient.Builder webClientBuilder) {
		this.webClientBuilder = webClientBuilder;
	}
	@Value("${token.validator.url}")
	private String tokenURL;

	/**
	 * Extract token and verify token by calling users validate token API
	 */
	@Override
	public GatewayFilter apply(Object config) {
		return (exchange, chain) -> {
			if(!exchange.getRequest().getHeaders().containsKey(HttpHeaders.AUTHORIZATION)) {
				throw new MissingAuthException("Missing Auth Information");
			}
			String authHeader = exchange.getRequest().getHeaders().get(HttpHeaders.AUTHORIZATION).get(0);
			String[] parts = authHeader.split(" ");
			
			if(parts.length !=2 || !"Bearer".equals(parts[0])) {
				throw new InvalidTokenStructureException("Incorrect Auth Structure");
			}
			
			return webClientBuilder.build()
					.get()
					.uri(tokenURL).header(HttpHeaders.AUTHORIZATION, parts[1])
					.retrieve()
					.bodyToMono(AuthenticationResponseDto.class).map(user->{
//						exchange.getRequest().mutate().header("userId", String.valueOf(user.getUserId()));
						return exchange;
					})
					.onErrorMap(Exception.class, ex -> new ApplicationRuntimeException(ex))
					.flatMap(chain::filter);
			
		};
	}
	
}
