package com.example.health_management.domain.repositories;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import com.example.health_management.domain.entities.Key;

@Repository
public interface KeyRepository extends JpaRepository<Key, Long> {
    Key findKeyByUserId(Long userId);

    @Modifying
    @Transactional
    @Query("UPDATE Key k SET k.refreshToken = NULL, k.notificationKey = '' WHERE k.user.id = :uid")
    void signOut(String uid);


    @Modifying
    @Transactional
    @Query("UPDATE Key k SET k.refreshToken = :refreshToken WHERE k.user.id = :userId")
    void updateRefreshTokenByUserId(@Param("refreshToken") String refreshToken, @Param("userId") Long userId);

    @Query("SELECT EXISTS(SELECT 1 FROM Key k WHERE k.refreshToken = :refreshToken)")
    Boolean existsByRefreshToken(String refreshToken);
}

