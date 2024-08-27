package com.example.health_management.application.guards;

import com.example.health_management.common.utils.TokenExpiration;
import com.example.health_management.domain.entities.Payload;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.services.KeyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Jwts;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtProvider {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KeyService keyService;
    private final AccountRepository accountRepository;

    public Map<String, String> generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
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
        return Jwts
                .builder()
                .claims(objectMapper.convertValue(payload, Map.class))
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


    public String getPrivateKeyByEmail(String email){
        Integer userId = accountRepository.findByEmail(email).getId();
        return keyService.findKeyByUserId(userId).getPrivateKey();
    }

    public String getPublicKeyByEmail(String email){
        Integer userId = accountRepository.findByEmail(email).getId();
        return keyService.findKeyByUserId(userId).getPublicKey();
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

    public String extractToken(HttpServletRequest request) {
        String header = request.getHeader("Authorization");
        if (header == null || !header.startsWith("Bearer ")) {
            return null;
        }
        return header.substring(7);
    }
}

