package com.example.health_management.application.guards;

import com.example.health_management.common.utils.TokenExpiration;
import com.example.health_management.domain.entities.Payload;
import com.example.health_management.domain.repositories.AccountRepository;
import com.example.health_management.domain.services.KeyService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.*;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.security.*;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.time.Instant;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class JwtProvider {
    private final ObjectMapper objectMapper = new ObjectMapper();
    private final KeyService keyService;
    private final AccountRepository accountRepository;
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

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
        Integer userId = accountRepository.findByEmail(email).getUser().getId();
        return keyService.findKeyByUserId(userId).getPrivateKey();
    }

    public String getPublicKeyByEmail(String email){
        Integer userId = accountRepository.findByEmail(email).getUser().getId();
        return keyService.findKeyByUserId(userId).getPublicKey();
    }

    public int getVersionByEmail(String email){
        Integer userId = accountRepository.findByEmail(email).getUser().getId();
        return keyService.findKeyByUserId(userId).getVersion();
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

    public Claims extractClaimsFromToken(String token) {
        try{
            Claims claims = Jwts.parser()
                    .setSigningKeyResolver(new SigningKeyResolverAdapter() {
                        @Override
                        public Key resolveSigningKey(JwsHeader header, Claims claims1) {
                            String email = claims1.get("email", String.class);
                            String publicKeyPEM = getPublicKeyByEmail(email);
                            if (publicKeyPEM == null) {
                                throw new RuntimeException("Public key not found for user: " + email);
                            }
                            try {
                                byte[] encoded = Base64.getDecoder().decode(publicKeyPEM);
                                X509EncodedKeySpec spec = new X509EncodedKeySpec(encoded);
                                KeyFactory keyFactory = KeyFactory.getInstance("RSA");
                                return keyFactory.generatePublic(spec);
                            } catch (Exception e) {
                                throw new RuntimeException("Error generating public key", e);
                            }
                        }
                    })
                    .build()
                    .parseSignedClaims(token)
                    .getPayload();


            int version = claims.get("version", Double.class).intValue();
            String email = claims.get("email", String.class);
            if(versioningVerify(version, email)){
                return claims;
            } else {
                return null;
            }
        } catch (ExpiredJwtException e) {
            logger.warn("Token expired");
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    public boolean versioningVerify(int version, String email){
        //get version from database
        int dbVersion = getVersionByEmail(email);
        return version == dbVersion;
    }

    public Map<String, String> refreshToken(String refreshToken) {
        Claims claims = extractClaimsFromToken(refreshToken);
        if (claims == null) {
            return null;
        }
        String email = claims.get("email", String.class);
        String role = claims.get("role", String.class);
        Double id = claims.get("id", Double.class);
        int uid = id.intValue();
        if (!verifyToken(refreshToken, getPublicKeyByEmail(email))) {
            return null;
        }

        String privateKeyPEM = getPrivateKeyByEmail(email);
        Payload payload = Payload.builder()
                .email(email)
                .id(uid)
                .role(role)
                .build();
        return Map.of(
                "accessToken", generateAccessToken(payload, privateKeyPEM),
                "refreshToken", refreshToken
        );
    }

    public MyUserDetails extractUserDetailsFromToken() {
        try{
            MyUserDetails myUserDetails = null;
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.getPrincipal() instanceof MyUserDetails) {
                myUserDetails = (MyUserDetails) authentication.getPrincipal();
            }
            return myUserDetails;
        } catch (Exception e) {
            return null;
        }
    }

    public void updateVersion(Integer userId, int version) {
        keyService.updateVersion(userId, version);
    }
}

