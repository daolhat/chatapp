package com.project.chatapp.config;

import com.project.chatapp.constant.JwtConstant;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.util.Date;

@Service
public class TokenProvider {

    public String generateToken(Authentication authentication) {
        SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());
        String jwt = Jwts.builder()
                .setIssuer("Chat app")
                .setIssuedAt(new Date())
                .setExpiration(new Date(new Date().getTime() + 86400000))
                .claim("email", authentication.getName())
                .signWith(key)
                .compact();
        return jwt;
    }

    public String getEmailFromToken(String jwt) {
        jwt = jwt.substring(7);
        SecretKey key = Keys.hmacShaKeyFor(JwtConstant.SECRET_KEY.getBytes());

        Claims claim = Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(jwt).getBody();
        String email = String.valueOf(claim.get("email"));
        return email;
    }



}
