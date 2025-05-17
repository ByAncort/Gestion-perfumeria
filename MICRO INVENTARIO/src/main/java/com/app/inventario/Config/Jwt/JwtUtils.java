package com.app.inventario.Config.Jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.function.Function;


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


    public Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(jwtSecret);
        return Keys.hmacShaKeyFor(keyBytes);
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
        try {
            Claims claims = Jwts.parser()
                    .setSigningKey(getSigningKey())
                    .build()
                    .parseClaimsJws(token)
                    .getBody();

            final String username = claims.getSubject();
            final boolean isExpired = claims.getExpiration().before(new Date());

            if (!username.equals(userDetails.getUsername())) {
                throw new JwtValidationException("Usuario no coincide");
            }

            if (isExpired) {
                throw new JwtValidationException("Token expirado");
            }

            return true;

        } catch (JwtValidationException e) {
            throw e;
        } catch (Exception e) {
            throw new JwtProcessingException("Error procesando JWT: " + e.getMessage(), e); // 500
        }
    }
    public class JwtValidationException extends RuntimeException {
        public JwtValidationException(String message) {
            super(message);
        }
    }

    public class JwtProcessingException extends RuntimeException {
        public JwtProcessingException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
