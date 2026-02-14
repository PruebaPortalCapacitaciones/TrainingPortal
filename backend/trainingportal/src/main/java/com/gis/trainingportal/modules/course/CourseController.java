package com.gis.trainingportal.modules.course;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gis.trainingportal.common.ApiResponseDto;

/* Controlador para gestionar cursos. */
@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    /* Endpoint para listar todos los cursos agrupados por módulo. */
    @GetMapping("/list")
    public ResponseEntity<ApiResponseDto<Map<String, List<CourseEntity>>>> listByModule() {
        Map<String, List<CourseEntity>> grouped = courseService.listByModule();
        return ResponseEntity.ok(ApiResponseDto.success("Cursos Agrupados por Módulo", grouped, 200));
    }

    /* Endpoint para crear un nuevo curso. */
    @PostMapping
    public ResponseEntity<ApiResponseDto<CourseEntity>> create(@RequestBody CourseEntity course) {
        CourseEntity saved = courseService.create(course);
        return ResponseEntity.ok(ApiResponseDto.success("Curso Creado con Éxito", saved, 200));
    }

    /* Endpoint para actualizar un curso existente. */
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CourseEntity>> update(
            @PathVariable Long id,
            @RequestBody CourseEntity course) {
        CourseEntity updated = courseService.update(id, course);
        return ResponseEntity.ok(ApiResponseDto.success("Curso Actualizado con Éxito", updated, 200));
    }

    /* Endpoint para eliminar (soft delete) un curso. */
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CourseEntity>> softDelete(@PathVariable Long id) {
        CourseEntity updated = courseService.softDelete(id);
        return ResponseEntity.ok(
                ApiResponseDto.success("Estado del Curso Actualizado (Activo/Inactivo)", updated, 200));
    }

    /* Endpoint para obtener un curso por su ID. */
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CourseEntity>> getById(@PathVariable Long id) {
        CourseEntity course = courseService.getById(id);
        return ResponseEntity.ok(ApiResponseDto.success("Curso Obtenido con Éxito", course, 200));
    }

    /* Endpoint para listar módulos disponibles. */
    @GetMapping("/modules")
    public ResponseEntity<ApiResponseDto<List<String>>> getModules() {
        List<String> modules = courseService.getModules();
        return ResponseEntity.ok(ApiResponseDto.success("Módulos Disponibles", modules, 200));
    }

    /* Endpoint para listar niveles disponibles. */
    @GetMapping("/levels")
    public ResponseEntity<ApiResponseDto<List<String>>> getLevels() {
        List<String> levels = courseService.getLevels();
        return ResponseEntity.ok(ApiResponseDto.success("Niveles Disponibles", levels, 200));
    }
}