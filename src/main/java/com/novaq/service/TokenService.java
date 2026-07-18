package com.novaq.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.novaq.model.User;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.List;

@Service
public class TokenService {

    @Value("${JWT_SECRET}")
    private String secret;

    @Value("${api.security.token.expiration_hours}")
    private Integer expirationHours;

    public String generateToken(User user){

        try{

            Algorithm algorithm = Algorithm.HMAC256(secret);

            List<String> roles = user.getUserRole().stream()
                    .map(Enum::name)
                    .toList();

            return JWT.create()
                    .withIssuer("novaq-api")
                    .withSubject(user.getEmail())
                    .withClaim("roles", roles)
                    .withExpiresAt(genExpirationDate())
                    .sign(algorithm);

        } catch (JWTCreationException exception) {
            throw new RuntimeException("Fatal error generating JWT token", exception);
        }
    }

    public String validateToken(String token){
        try{
            Algorithm algorithm = Algorithm.HMAC256(secret);

            return JWT.require(algorithm)
                    .withIssuer("novaq-api")
                    .build()
                    .verify(token)
                    .getSubject();
        }
        catch (JWTVerificationException exception) {
            return "";
        }
    }

    private Instant genExpirationDate() {
        return LocalDateTime.now().plusHours(expirationHours).toInstant(ZoneOffset.UTC);
    }
}
