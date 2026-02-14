package com.gis.trainingportal.modules.course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/* Repositorio para acceder a los datos de los cursos. */
@Repository
public interface CourseRepo extends JpaRepository<CourseEntity, Long> {

    /* Método para encontrar cursos por módulo. */
    List<CourseEntity> findByModule(String module);

    /* Método para contar cursos activos. */
    long countByActiveTrue();

    /* Método para contar cursos inactivos. */
    long countByActiveFalse();
}
