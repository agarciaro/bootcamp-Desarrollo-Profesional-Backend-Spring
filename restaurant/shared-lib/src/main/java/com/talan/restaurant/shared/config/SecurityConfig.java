package com.talan.restaurant.shared.config;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.authentication.AbstractAuthenticationToken;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {
	
	@Value("${security.public.path}")
	private String publicPath;
	
	@Bean
	SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
        .csrf(AbstractHttpConfigurer::disable)
        .authorizeHttpRequests(auth -> {
        	//auth.requestMatchers("/api/products/**").hasRole("ADMIN");
        	//auth.requestMatchers("/api/products/2").hasRole("USER");
        	auth.requestMatchers(request -> 
        		request.getRequestURI().contains(publicPath)).permitAll()
        		.anyRequest().authenticated();
        }
        ).oauth2ResourceServer(configure -> configure.jwt(jwt -> jwt.jwtAuthenticationConverter(jwtAuthConverter())))
        .build();
	}
	
	private Converter<Jwt, ? extends AbstractAuthenticationToken> jwtAuthConverter() {
		JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
		converter.setJwtGrantedAuthoritiesConverter(new KeycloakRealmRoleConverter());
		return converter;
	}
	
	@SuppressWarnings("unchecked")
	class KeycloakRealmRoleConverter implements Converter<Jwt, Collection<GrantedAuthority>> {

		@Override
		public Collection<GrantedAuthority> convert(Jwt jwt) {
			if(jwt.getClaims() == null) {
				return List.of();
			}
			
			final Map<String, List<String>> realmAccess = (Map<String, List<String>>) jwt.getClaims().get("realm_access");
			
			return realmAccess.get("roles")
					.stream()
					.map(roleName -> "ROLE_" + roleName)
					.map(prefixedRole -> new SimpleGrantedAuthority(prefixedRole))
					.collect(Collectors.toList());
		}
		
	}
	
}
