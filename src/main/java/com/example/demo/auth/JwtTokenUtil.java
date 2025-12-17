package com.example.demo.auth;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
@Component
public class JwtTokenUtil {
    private final String Secret_Key = "c2568cce684c048bb1cf14228bed5163a48407126ddd0ee749c578f238609c98";

    private final long JWT_TOKEN_VALIDITY = 5 * 60 * 60; // 5 hours

    public String generateToken(UserDetails userDetails)
    {
        Map<String, Object> claims = new HashMap<>();
        claims.put("roles", userDetails.getAuthorities());
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(userDetails.getUsername())
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
                .signWith(getSignKey(), SignatureAlgorithm.HS256)
                .compact();
    }
    private Key getSignKey() {
        byte [] keyBytes = Secret_Key.getBytes();
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public Boolean validateToken(String token, UserDetails userDetails)
    {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));

    }
    public String extractUsername(String token)
    {
        return extractClaim(token, Claims::getSubject);
    }
    public Date extractExpiration(String token)
    {
        return extractClaim(token, Claims::getExpiration);
    }
    private Boolean isTokenExpired(String token)
    {
        return extractExpiration(token).before(new Date());
    }
    private <T> T extractClaim(String token, Function<Claims, T> claimsResolver)
    {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token)
    {
        return Jwts.parserBuilder()
                .setSigningKey(getSignKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

}
