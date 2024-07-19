package com.groovegather.back.config;

import java.util.Map;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.http.HttpServletResponse;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

	private final JWTFilter jwtFilter;

	public SecurityConfiguration(JWTFilter jwtFilter) {
		this.jwtFilter = jwtFilter;
	}

	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(
						request -> {
							request
									.requestMatchers("/api/v1/users/register").permitAll()
									.requestMatchers("/api/v1/users/login").permitAll()
									// .requestMatchers(HttpMethod.GET, "/api/v1/users/user*").permitAll()
									.requestMatchers(HttpMethod.GET, "/api/v1/projects*").permitAll()
									.requestMatchers(HttpMethod.GET, "/api/v1/projects/*").permitAll()

									.requestMatchers("/api/v1/users/user*").permitAll()
									.requestMatchers("/api/v1/users*").permitAll()
									.requestMatchers("/api/v1/files/**").permitAll()
									.anyRequest().authenticated();
						})
				.addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
				.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
				.exceptionHandling(exception -> exception
						.authenticationEntryPoint((request, response, authException) -> {
							response.setContentType("application/json;charset=UTF-8");
							response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
							Map<String, ?> errors = Map.of("status", HttpServletResponse.SC_UNAUTHORIZED,
									"error_message",
									"Non autorisé");
							response.getWriter().write(new ObjectMapper().writeValueAsString(errors));
						}).accessDeniedHandler((request, response, accessDeniedException) -> {
							response.setContentType("application/json;charset=UTF-8");
							response.setStatus(HttpServletResponse.SC_FORBIDDEN);
							Map<String, ?> errors = Map.of("status", HttpServletResponse.SC_FORBIDDEN, "error_message",
									"Accès interdit");
							response.getWriter().write(new ObjectMapper().writeValueAsString(errors));
						}))
				.build();
	}

	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
			throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}

}
