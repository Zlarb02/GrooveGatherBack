package com.groovegather.back.config;

import com.groovegather.back.entities.UserEntity;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

@Service
public class JWTService {

  private final UserDetailsService userDetailsService;

  @Value("${jwt-secret}")
  private String secret;

  // @Value("#{${jwt.expiration:1440} * 60 * 1000}")
  @Value("${jwt-expiration:1440}")
  private Long tokenExpiration;

  private SecretKey key;

  public JWTService(UserDetailsService userDetailsService) {
    this.userDetailsService = userDetailsService;
  }

  @PostConstruct
  private void init() {
    this.key = Keys.hmacShaKeyFor(secret.getBytes(StandardCharsets.UTF_8));
  }

  public Map<String, String> generateToken(String email) {
    UserDetails user = this.userDetailsService.loadUserByUsername(email);
    return generateJWT((UserEntity) user);
  }

  private Map<String, String> generateJWT(UserEntity user) {
    final Long currentTime = System.currentTimeMillis();

    final Long jwtExpiration = currentTime + (this.tokenExpiration * 60 * 1000);

    Map<String, ?> claims = Map.of(
        "email", user.getUsername(),
        "role", user.getRole(),
        Claims.EXPIRATION, new Date(jwtExpiration),
        Claims.ISSUED_AT, new Date(),
        Claims.SUBJECT, user.getUsername());

    final String token = Jwts.builder()
        .claims(claims)
        .signWith(key)
        .compact();

    return Map.of("Bearer", token);
  }

  public String extractUsername(String token) {
    return getPayload(token).getSubject();
  }

  public String extractRole(String token) {
    return (String) getPayload(token).get("role");
  }

  public boolean isExpired(String token) {
    Date date = new Date();
    Date exp = getPayload(token).getExpiration();
    return date.after(exp);
  }

  private Claims getPayload(String token) {
    return (Claims) Jwts.parser()
        .verifyWith(key)
        .build()
        .parse(token)
        .getPayload();
  }
}
