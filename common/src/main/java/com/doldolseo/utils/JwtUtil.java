package com.doldolseo.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.http.ResponseCookie;
import javax.servlet.http.HttpServletResponse;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

public class JwtUtil {
    private final String JWT_SECRET = "DOLDOLSEOTESTSECRET";
    private final String API_KEY = "DOLDOLSEOTESTAPIKEY";
    private static final int JWT_EXPIRATION_MS = 60480000;
    public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;

    public boolean validateToken(String token, String username) {
        final String id = getIdFromToken(token);
        return (id.equals(username) && !isTokenExpired(token));
    }

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

    private Date getExpirationDateFromToken(String token) {
        return getClaimFromToken(token, Claims::getExpiration);
    }

    public String generateToken(String id, String role) {
        return doGenerateToken(id, role, new HashMap<>());
    }

    public String generateToken(String id, String role, Map<String, Object> claims) {
        return doGenerateToken(id, role, claims);
    }

    public String doGenerateToken(String id, String role, Map<String, Object> claims) {
        Date now = new Date();
        Date expiryDate = new Date(now.getTime() + JWT_EXPIRATION_MS);

        claims.put("api-key", API_KEY);
        claims.put("role", role);
        String token = Jwts.builder()
                .setClaims(claims)
                .setId(id)
                .setIssuedAt(new Date())
                .setExpiration(expiryDate)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET)
                .compact();

        System.out.println("token : " + token);

        return token;
    }

    public void setToken(HttpServletResponse response, String token) {
        ResponseCookie jwtCookie = ResponseCookie.from("token", token)
                .maxAge(7 * 24 * 60 * 60)
                .path("/")
                .sameSite("None")
                .secure(true)
                .build();

        response.addHeader("Set-Cookie", jwtCookie.toString());
    }
}
