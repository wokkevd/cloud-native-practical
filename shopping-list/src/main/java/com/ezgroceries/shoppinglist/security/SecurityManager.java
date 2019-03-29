package com.ezgroceries.shoppinglist.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Component
public class SecurityManager {

    //TODO move to openshift
    private static final String SECRET_KEY = "myVerySecretSecret";
    private static final String ISSUER = "ShoppingListApp";
    private static final long EXPIRATION = 900000; //15 minutes

    private final BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String createJWT(String userId) {
        SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

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
