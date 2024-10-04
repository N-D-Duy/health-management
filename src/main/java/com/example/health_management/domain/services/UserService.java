package com.example.health_management.domain.services;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.health_management.domain.repositories.UserRepository;
import com.example.health_management.domain.entities.User;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public CompletableFuture<List<User>> getUsers(User user) {
        return CompletableFuture.completedFuture(userRepository.findAll());
    }

    public CompletableFuture<User> getUserById(Long id) {
        return CompletableFuture.supplyAsync(() -> userRepository.findById(id).orElse(null));
    }

    public CompletableFuture<User> createUser(User user) {
        return CompletableFuture.supplyAsync(() -> userRepository.save(user));
    }

    public CompletableFuture<User> updateUser(User user) {
        return CompletableFuture.supplyAsync(() -> userRepository.save(user));
    }
}
