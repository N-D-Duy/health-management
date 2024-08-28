package com.example.health_management.domain.entities;

import jakarta.persistence.*;
import org.springframework.lang.Nullable;

import java.time.LocalDate;
import java.util.Set;

@Entity
@Table(name = "users") // Tên bảng là "users" thay vì "user" để tránh từ khóa của SQL
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 50)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 50)
    private String lastName;

    @Column(name = "date_of_birth", nullable = false)
    private LocalDate dateOfBirth;

    @Column(name = "gender", nullable = false, length = 10)
    private String gender;

    @OneToOne(mappedBy = "user")
    private Account account;

    @OneToOne(mappedBy = "user")
    private Key key;
    // Constructors, getters, and setters
    public User() {}

    public User(String firstName, String lastName, LocalDate dateOfBirth, String gender, Account account, Key key) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
        this.account = account;
        this.key = key;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }    

    public Key getKey() {
        return key;
    }

    public void setKey(Key key) {
        this.key = key;
    }

}

