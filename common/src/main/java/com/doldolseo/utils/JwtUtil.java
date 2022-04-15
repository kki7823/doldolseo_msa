package com.doldolseo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import java.util.Date;
import java.util.function.Function;

public class JwtUtil {
    private final String JWT_SECRET = "DOLDOLSEOTESTSECRET";
    private final String API_KEY = "DOLDOLSEOTESTAPIKEY";

    public boolean isInvalid(String token) {
        return !getAllClaimsFromToken(token).get("api-key").equals(API_KEY);
    }

    public boolean isTokenExpired(String token) {
        return getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET)
                .parseClaimsJws(token).getBody();
    }

    public String getIdFromToken(String token) {
        return getClaimFromToken(token, Claims::getId);
    }

    public <T> T getClaimFromToken(String token, Function<Claims, T> claimResolver) {
        final Claims claims = getAllClaimsFromToken(token);
        return claimResolver.apply(claims);
    }
}
