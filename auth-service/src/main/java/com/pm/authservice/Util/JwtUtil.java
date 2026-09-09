package com.pm.authservice.Util;

import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import io.jsonwebtoken.security.SignatureException;
import java.util.Base64;
import java.util.Date;

@Component
public class JwtUtil {
    private final Key secretkey;
    public JwtUtil(@Value("${jwt.secret}") String secret) {
        byte[] Bytes = Base64.getDecoder().decode(secret.getBytes(StandardCharsets.UTF_8));
        this.secretkey = Keys.hmacShaKeyFor(Bytes);

    }

    public String generateToken(String email, String role) {
        return Jwts.builder()
                .subject(email) // this is expected from jwt
                .claim("role", role) // custom property that can be added to jwt
                .issuedAt(new Date())
                .expiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 10)) // 10 hours
                .signWith(secretkey)
                .compact();
    }

    public void validateToken(String token){
        try{
            Jwts.parser().verifyWith((SecretKey) secretkey) // check the signature which was signed by secret key
                    .build()
                    .parseSignedClaims(token);
        }catch (SignatureException e){
            throw new JwtException("Invalid jwt signature");
        }catch (JwtException e){
            throw new JwtException("Invalid jwt");
        }
    }
}
