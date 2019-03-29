package com.ezgroceries.shoppinglist.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Component
public class SecurityManager {

    @Value("${security.key}")
    private String SECRET_KEY;

    @Value("${security.issuer}")
    private String ISSUER;

    @Value("${security.expiration}")
    private long EXPIRATION;

    @Value("${security.signature.algorithm}")
    private String SIGNATURE;

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String createJWT(String userId) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.forName(SIGNATURE);

        byte[] apiKeySecretBytes = DatatypeConverter.parseBase64Binary(SECRET_KEY);
        Key signingKey = new SecretKeySpec(apiKeySecretBytes, signatureAlgorithm.getJcaName());

        JwtBuilder builder = Jwts.builder()
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setSubject(userId)
                .setIssuer(ISSUER)
                .signWith(signatureAlgorithm, signingKey);

        long expMillis = System.currentTimeMillis() + EXPIRATION;
        Date exp = new Date(expMillis);
        builder.setExpiration(exp);

        return builder.compact();
    }

    public Claims validateJWT(String jwt) {
        return Jwts.parser()
                .setSigningKey(DatatypeConverter.parseBase64Binary(SECRET_KEY))
                .parseClaimsJws(jwt).getBody();
    }

    public boolean verifyJWT(String jwt) {
        try {
            validateJWT(jwt);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public BCryptPasswordEncoder getPasswordEncoder() {
        return passwordEncoder;
    }
}
