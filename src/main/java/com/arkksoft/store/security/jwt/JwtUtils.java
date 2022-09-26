package com.arkksoft.store.security.jwt;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.userdetails.UserDetails;

import com.arkksoft.store.models.dao.UserDao;
import com.arkksoft.store.services.UsuarioDetails;

import io.jsonwebtoken.*;

public class JwtUtils {
    @Value("${arkkosoft.app.JWT_SECRET_KEY}")
    private String JWT_SECRET_KEY;

    @Value("${arkkosoft.app.JWT_TOKEN_VALIDITY}")
    private int JWT_TOKEN_VALIDITY;

    @Value("${arkkosoft.app.JWT_REFRESH_TOKEN_VALIDITY}")
    private int JWT_REFRESH_TOKEN_VALIDITY;

    @Autowired
    UserDao userDao;

    //retrieve username from jwt token
    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject).split("\\|")[0];
    }
    
    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser()
                .setSigningKey(JWT_SECRET_KEY)
                .parseClaimsJws(token)
                .getBody()
                .getSubject()
                .split("\\|")[0];
    }

    public String extractId(String token) {        
        return extractClaim(token, Claims::getSubject).split("\\|")[1];
    }

    //retrieve expiration date from jwt token
    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token).getBody();
    }

    //check if the token has expired
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    //generate token for user
    public String generateToken(UsuarioDetails userDetails) {
        String subject = userDetails.getUsername() + "|" + userDetails.getId();

        Map<String, Object> claims = new HashMap<>();
        // Agregando informacion adicional como "claim"
        var role = userDetails.getAuthorities().stream().collect(Collectors.toList());
        if(!role.isEmpty()) {
            claims.put("role", role);
        }
        return createToken(claims, subject);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        Date issueTime = new Date(System.currentTimeMillis());
        Date expirationTime = new Date((new Date()).getTime() + JWT_TOKEN_VALIDITY);

        return Jwts
                .builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(issueTime)
                .setExpiration(expirationTime)
                .signWith(SignatureAlgorithm.HS512, JWT_SECRET_KEY)
                .compact();
    }

    public boolean validateToken(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    public boolean validateJwtRefreshToken(String token) {
        try {
            Jwts.parser().setSigningKey(JWT_SECRET_KEY).parseClaimsJws(token);
            return true;
        } catch (SignatureException | MalformedJwtException | UnsupportedJwtException | IllegalArgumentException ex) {
            throw new BadCredentialsException("INVALID_TOKEN", ex);
        } catch (ExpiredJwtException ex) {
            throw new BadCredentialsException("TOKEN IS EXPIRED", ex);
        }
    }
}
