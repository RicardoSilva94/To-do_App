package com.example.todo.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.Objects;

@Component
public class JWTUtil {

    @Value("${jwt.secret}")
    private String secret;

    @Value("${jwt.expiration}")
    private Long expiration;

    // Método para gerar um token JWT com base no nome de utilizador
    public String generateToken(String username) {
        SecretKey key = getKeyBySecret(); // Obtém uma secret key para assinar o token
        return Jwts.builder().setSubject(username) // Define o Subject do token como o nome de user
                .setExpiration(new Date(System.currentTimeMillis()+ this.expiration)) // Define a data de expiração do token
                .signWith(key) // Assina o token com a chave secreta
                .compact(); // Gera o token compacto
    }

    // Obtém uma Secret key com base na configuração
    private SecretKey getKeyBySecret(){
        SecretKey key = Keys.hmacShaKeyFor(this.secret.getBytes());  // Gera uma secret key a partir do secret
        return key;
    }
    // Verifica se um token JWT é válido
    public boolean isValidToken(String token) {
        Claims claims = getClaims(token); // Obtém as informações do token
        if (Objects.nonNull(claims)) {
            String username = claims.getSubject(); // Obtém o nome de user do token
            Date expirationDate = claims.getExpiration();  // Obtém a data de expiração do token
            Date now = new Date(System.currentTimeMillis()); // Obtém a data atual
            if (Objects.nonNull(username) && Objects.nonNull(expirationDate) && now.before(expirationDate))
                return true; // O token é válido se o nome de user existe, a data de expiração é posterior à data atual
        }
        return false; // O token não é válido
    }

    // Obtém o nome de user de um token JWT
    public String getUsername(String token) {
    Claims claims = getClaims(token);
    if(Objects.nonNull(claims))
        return claims.getSubject();
    return null;
    }

    // Obtém as informações do token JWT
    private Claims getClaims(String token){
        SecretKey key = getKeyBySecret();
        try {
            return Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token).getBody();
        }catch (Exception e){
            return null;
        }
    }

}
