package com.springBoot.Oauth2Example.config.jwt;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;

import java.util.Date;

@Component
public class JwtService {

    private String secret = "kA93jfUq8Z1xLpBn7Vr2WmCd6Y0eXtFsaskdjhkjshdkhashdhaskfhkjdhfkjdhfkjhsdkjhsdkjfhskdjfhsdkjfhksdhfksdhfkhdfsh";

    public String generateToken(String username) {
        return Jwts.builder()
                .setSubject(username)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + Integer.MAX_VALUE))
                .signWith(SignatureAlgorithm.HS512, secret)
                .compact();
    }

    public String getUsername(String token) {
        return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateToken(String token, String username) {
        return getUsername(token).equals(username) && !isExpired(token);
    }

    private boolean isExpired(String token) {
        Date exp = Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody().getExpiration();
        return exp.before(new Date());
    }
}