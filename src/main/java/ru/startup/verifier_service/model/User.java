package ru.startup.verifier_service.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Data
@Entity
@Table(name = "\"user\"", schema = "internal")
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "uuid")
    private UUID uuid;

    @Size(min = 1, max = 200, message = "не должен быть пустым")
    @Column(name = "full_name")
    private String fullName;

    @Column(name = "company")
    private String company;

    @Size(min = 1, max = 200, message = "не должен быть пустым")
    @Column(name = "inn")
    private String inn;

    @Size(min = 1, max = 200, message = "не должен быть пустым")
    @Column(name = "ogrn")
    private String ogrn;

    @Size(min = 1, max = 200, message = "не должен быть пустым")
    @Column(name = "position")
    private String position;

    @Column(name = "role")
    private String role;

    @Column(name = "email")
    private String email;

    @Column(name = "password")
    private String password;
}
