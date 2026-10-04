package com.example.JWT.springBoot.easy.Util;


import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;

@Component
public class JWTUtil {

     private final Long EXPIRATION_TIME = (long) (1000*60*60);
     private final String SECRET = "a-string-secret-at-least-256-bits-long";
     private final SecretKey key = Keys.hmacShaKeyFor(SECRET.getBytes());
    public String generateToken(String userName){
      return  Jwts.builder()
                .setSubject(userName)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis()+EXPIRATION_TIME))
                .signWith(key, SignatureAlgorithm.HS256)
                .compact();
    }

    public String extracUserNamefromToken(String token){
        return  extractClaims(token).getSubject();
    }

    private Claims extractClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
    }


    public boolean validateToken(String username, UserDetails userDetails, String token) {
        // TODO Check the username is same as userdetails
        // TODO check the token is not expired
       return username.equals(userDetails.getUsername()) && !isTokenExpired(token);
    }

    private boolean isTokenExpired(String token) {
         return extractClaims(token).getExpiration().before(new Date());
    }
}
