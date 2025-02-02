package com.example.health_management.domain.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.persistence.Column;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import lombok.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "keys")
public class Key {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id", unique = true)
    private User user;

    @Column(name = "public_key", nullable = false, length = 4096)
    private String publicKey;

    @Column(name = "private_key", nullable = false, length = 4096)
    private String privateKey;

    @Column(name = "refresh_token", length = 4096)
    private String refreshToken;

    @Column(name = "version", nullable = false)
    private Integer version;

    @Column(name = "notification_key", nullable = false)
    private String notificationKey;
}
