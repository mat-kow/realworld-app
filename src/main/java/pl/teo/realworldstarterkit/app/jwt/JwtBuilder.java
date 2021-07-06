package pl.teo.realworldstarterkit.app.jwt;

import io.jsonwebtoken.Jwts;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.time.LocalDate;
import java.util.*;

@Component
public class JwtBuilder {
    private final SecretKey secretKey;

    public JwtBuilder(SecretKey secretKey) {
        this.secretKey = secretKey;
    }

    public String getToken(String subject) {
        Set<GrantedAuthority> grantedAuthorities = new HashSet<>();
        GrantedAuthority userAuthority = () -> "ROLE_USER";
        grantedAuthorities.add(userAuthority);
        return Jwts.builder()
                .setSubject(subject)
                .claim("authorities", grantedAuthorities)
                .setIssuedAt(new Date())
                .setExpiration(java.sql.Date.valueOf(LocalDate.now().plusDays(14)))
                .signWith(secretKey)
                .compact();
    }
}