package com.example.health_management.domain.services;

import com.example.health_management.common.utils.TokenExpiration;
import com.example.health_management.domain.entities.Payload;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtService {
    private final ObjectMapper objectMapper = new ObjectMapper();

    public Map<String, String> generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(4096);
            KeyPair pair = keyGen.generateKeyPair();
            PrivateKey privateKey = pair.getPrivate();
            PublicKey publicKey = pair.getPublic();

            // convert keys to PEM string for storage
            String privateKeyPEM = java.util.Base64.getEncoder().encodeToString(privateKey.getEncoded());
            String publicKeyPEM = java.util.Base64.getEncoder().encodeToString(publicKey.getEncoded());

            Map<String, String> keys = new HashMap<>();
            keys.put("publicKey", publicKeyPEM);
            keys.put("privateKey", privateKeyPEM);

            return keys;

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating RSA keys", e);
        }
    }

    public String generateToken(Payload payload, String privateKeyPEM, long expirationMillis) {
        //convert privateKeyPEM from PEM string to PrivateKey
        PrivateKey privateKey;
        try {
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(java.util.Base64.getDecoder().decode(privateKeyPEM));
            privateKey = keyFactory.generatePrivate(keySpec);
        } catch (Exception e) {
            return null;
        }
        Map<String, Object> claimsMap = objectMapper.convertValue(payload, Map.class);
        return Jwts
                .builder()
                .claims(claimsMap)
                .issuedAt(Date.from(Instant.now()))
                .expiration(new Date(System.currentTimeMillis() + expirationMillis)) // 15 minutes
                .signWith(privateKey, Jwts.SIG.RS256)
                .compact();
    }

    public String generateAccessToken(Payload payload, String privateKeyPEM) {
        return generateToken(payload, privateKeyPEM, TokenExpiration.ACCESS_TOKEN_EXPIRATION);
    }

    public String generateRefreshToken(Payload payload, String privateKeyPEM) {
        return generateToken(payload, privateKeyPEM, TokenExpiration.REFRESH_TOKEN_EXPIRATION);
    }

    public boolean verifyToken(String token, String publicKeyPEM) {
        // convert publicKeyPEM from PEM string to PublicKey
        PublicKey publicKey;
        try{
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            X509EncodedKeySpec keySpec = new X509EncodedKeySpec(java.util.Base64.getDecoder().decode(publicKeyPEM));
            publicKey = keyFactory.generatePublic(keySpec);
        } catch (Exception e) {
            return false;
        }

        try {
            Jwts.parser()
                    .verifyWith(publicKey)
                    .build()
                    .parseSignedClaims(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }
}

