package com.gis.trainingportal.modules.course;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class CourseService {

    private final CourseRepo courseRepo;

    public CourseService(CourseRepo courseRepo) {
        this.courseRepo = courseRepo;
    }

    /* Listas de módulos válidos. */
    private static final List<String> VALID_MODULES = Arrays.stream(ModuleEnum.values())
            .map(ModuleEnum::getDisplayName)
            .collect(Collectors.toList());

    /* Obtener módulos disponibles */
    public List<String> getModules() {
        return VALID_MODULES;
    }

    /* Listas de niveles válidos. */
    private static final List<String> VALID_LEVELS = Arrays.stream(LevelEnum.values())
            .map(LevelEnum::getDisplayName)
            .collect(Collectors.toList());

    /* Obtener niveles disponibles */
    public List<String> getLevels() {
        return VALID_LEVELS;
    }

    /* Validar módulo */
    private void validateModule(String moduleStr) {
        try {
            ModuleEnum.fromString(moduleStr);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Módulo no válido. Valores permitidos: " + String.join(", ", VALID_MODULES));
        }
    }

    /* Validar nivel */
    private void validateLevel(String levelStr) {
        try {
            LevelEnum.fromString(levelStr);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "Nivel no válido. Valores permitidos: " + String.join(", ", VALID_LEVELS));
        }
    }

    /* Listar cursos agrupados por módulo */
    public Map<String, List<CourseEntity>> listByModule() {
        List<CourseEntity> allCourses = courseRepo.findAll();

        return allCourses.stream()
                .collect(Collectors.groupingBy(
                        course -> course.getModule().getDisplayName()));
    }

    /* Obtener un curso por su ID */
    public CourseEntity getById(Long id) {
        return courseRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));
    }

    /* Crear un nuevo curso */
    public CourseEntity create(CourseEntity course) {
        // Validaciones básicas
        if (course.getTitle() == null || course.getTitle().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título es obligatorio");
        }
        if (course.getModule() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El módulo es obligatorio");
        }
        if (course.getDescription() == null || course.getDescription().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción es obligatoria");
        }
        if (course.getDurationHours() == null || course.getDurationHours() <= 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "La duración en horas es obligatoria y debe ser mayor a 0");
        }
        if (course.getLevel() == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nivel del curso es obligatorio");
        }

        // Validar módulo y nivel
        validateModule(course.getModule().getDisplayName());
        validateLevel(course.getLevel().getDisplayName());

        return courseRepo.save(course);
    }

    /* Actualizar un curso existente */
    public CourseEntity update(Long id, CourseEntity courseData) {
        CourseEntity course = getById(id);

        if (courseData.getTitle() != null) {
            if (courseData.getTitle().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El título no puede quedar vacío");
            }
            course.setTitle(courseData.getTitle());
        }

        if (courseData.getDescription() != null) {
            if (courseData.getDescription().isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La descripción no puede quedar vacía");
            }
            course.setDescription(courseData.getDescription());
        }

        if (courseData.getModule() != null) {
            validateModule(courseData.getModule().getDisplayName());
            course.setModule(courseData.getModule());
        }

        if (courseData.getDurationHours() != null) {
            if (courseData.getDurationHours() <= 0) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "La duración debe ser mayor a 0");
            }
            course.setDurationHours(courseData.getDurationHours());
        }

        if (courseData.getLevel() != null) {
            validateLevel(courseData.getLevel().getDisplayName());
            course.setLevel(courseData.getLevel());
        }

        return courseRepo.save(course);
    }

    /* Eliminar (soft delete) un curso */
    public CourseEntity softDelete(Long id) {
        CourseEntity course = getById(id);
        course.setActive(!Boolean.TRUE.equals(course.getActive()));
        return courseRepo.save(course);
    }

}