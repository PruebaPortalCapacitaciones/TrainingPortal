package com.gis.trainingportal.modules.enrollment;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/* Repositorio para acceder a los datos de las inscripciones. */
@Repository
public interface EnrollmentRepo extends JpaRepository<EnrollmentEntity, Long> {

    /* Método para encontrar inscripciones por ID de usuario. */
    List<EnrollmentEntity> findByUserId(Long userId);

    /*
     * Método para encontrar una inscripción específica por ID de usuario y ID de
     * curso.
     */
    Optional<EnrollmentEntity> findByUserIdAndCourseId(Long userId, Long courseId);

    /*
     * Método para verificar si una inscripción existe por ID de usuario y ID de
     * curso.
     */
    boolean existsByUserIdAndCourseId(Long userId, Long courseId);

    /* Método para encontrar inscripciones por ID de usuario y estado. */
    List<EnrollmentEntity> findByUserIdAndStatus(Long userId, StatusEnum status);

    /*
     * Método para encontrar inscripciones por ID de usuario y estado diferente a
     * uno específico.
     */
    List<EnrollmentEntity> findByUserIdAndStatusNot(Long userId, StatusEnum status);

}
