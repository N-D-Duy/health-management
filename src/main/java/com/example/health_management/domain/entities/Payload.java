package com.example.health_management.domain.entities;

public class Payload {
    private int id;
    private String role;
    private String email;

    public Payload(int id, String role, String email) {
        this.id = id;
        this.role = role;
        this.email = email;
    }

    public Payload() {
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
