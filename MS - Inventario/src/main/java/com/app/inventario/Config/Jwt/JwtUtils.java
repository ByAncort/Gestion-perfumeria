package com.app.inventario.Config.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
@RequiredArgsConstructor
public class JwtUtils {

    @Value("${auth.app.jwtSecret}")
    private String jwtSecret;

    @Value("${auth.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    @Value("${auth.app.jwtCookieName}")
    private String jwtCookie;
    @Value("${auth.app.jwtRefresh}")
    private String jwtRefreshMs;
    private static final Logger logger = LoggerFactory.getLogger(AuthEntryPointJwt.class);

    public String getUsernameFromToken(String token) {
        return getClaims(token, Claims::getSubject);
    }

    public String getToken(UserDetails user) {
        return generateTokenFromUsername(new HashMap<>(), user);
    }

    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    private String generateTokenFromUsername(HashMap<String, Object> extraClaims, UserDetails user) {
        logger.info("Generando token con clave: {}", jwtSecret.substring(0, 4) + "...");

        Date issuedAt = new Date();
        Date expiration = new Date(System.currentTimeMillis() + jwtExpirationMs);

        extraClaims.put("issuedAt", issuedAt);
        extraClaims.put("expiration", expiration);

        String token = Jwts.builder()
                .claims(extraClaims)
                .subject(user.getUsername())
                .issuedAt(issuedAt)
                .expiration(expiration)
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
        return token;
    }

    public Claims getAllClaims(String token) {
        logger.info("Validando token con clave: {}", jwtSecret.substring(0, 4) + "...");
        try {
            return Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();
        } catch (Exception e) {
            logger.error("Error al validar token: " + e.getMessage());
            throw e;
        }
    }

    public <T> T getClaims(String token, Function<Claims,T> claimsReslver){
        final Claims claims=getAllClaims(token);
        return claimsReslver.apply(claims);
    }
    private Date getExpiration(String token){
        return getClaims(token,Claims::getExpiration);
    }
    public boolean isTokenExpired(String token) {
        return getExpiration(token).before(new Date());
    }
    public boolean isTokenValid(String token, UserDetails userDetails) {
        logger.info("Validando si el token");
        try {
            // Validación EXPLÍCITA de la firma primero
            Claims claims = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            // Luego validar usuario y expiración
            String username = claims.getSubject();
            boolean isExpired = claims.getExpiration().before(new Date());

            return username.equals(userDetails.getUsername()) && !isExpired;
        } catch (Exception e) {
            logger.error("Error validando token: {}", e.getMessage());
            return false;
        }
    }
}
