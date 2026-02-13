package com.gis.trainingportal.modules.user;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/* Entidad JPA para los usuarios en la base de datos */
@Entity
@Table(name = "users", schema = "portal")
@Getter
@Setter
public class UserEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 10)
    private String document;

    @Column(nullable = false, length = 50)
    private String password;

    @Column(length = 100)
    private String name;

    @Column(nullable = true, length = 100)
    private String email;

    @Column(nullable = false, length = 5)
    private String role = "USER";

}
