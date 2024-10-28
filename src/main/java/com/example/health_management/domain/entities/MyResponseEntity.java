package com.example.health_management.domain.entities;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.Serial;
import java.io.Serializable;

public class MyResponseEntity<T> extends ResponseEntity<T> implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L; // Recommended for Serializable classes

    public MyResponseEntity(T body, HttpStatus status) {
        super(body, status);
    }

    // Optionally, you can add other constructors for more flexibility
    public MyResponseEntity(T body) {
        super(body, HttpStatus.OK);
    }

    public MyResponseEntity(HttpStatus status) {
        super(status);
    }

    // You can also add custom methods if needed
}
