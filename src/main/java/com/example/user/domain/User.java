package com.example.user.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.UUID;

@Data
@Entity
@Table(name = "users")
@EqualsAndHashCode(callSuper = false)
public class User extends Auditable {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @NotEmpty
    @Size(max = 255)
    @Column(name = "first_name", nullable = false)
    private String firstName;

    @NotEmpty
    @Size(max = 255)
    @Column(name = "last_name", nullable = false)
    private String lastName;

    @NotEmpty
    @Size(max = 255)
    @Column(name = "nickname", nullable = false, unique = true)
    private String nickname;

    @NotEmpty
    @Column(name = "password", nullable = false)
    private String password;

    @Email
    @NotEmpty
    @Size(max = 255)
    @Column(name = "email", nullable = false, unique = true)
    private String email;

    @Size(max = 255)
    @Column(name = "country")
    private String country;
} 