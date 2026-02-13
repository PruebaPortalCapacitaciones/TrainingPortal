package com.gis.trainingportal.modules.user;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

/* Repositorio para manejar la persistencia de los usuarios */
public interface UserRepo extends JpaRepository<UserEntity, Long> {

    // Método para buscar un usuario por su documento
    Optional<UserEntity> findByDocument(String document);

}
