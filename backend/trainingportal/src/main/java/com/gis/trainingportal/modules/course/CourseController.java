package com.gis.trainingportal.modules.course;

import java.util.*;
import java.util.stream.Collectors;

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

@RestController
@RequestMapping("/courses")
public class CourseController {

    private final CourseService courseService;

    public CourseController(CourseService courseService) {
        this.courseService = courseService;
    }

    // Listar cursos agrupados por módulo
    @GetMapping("/list")
    public ResponseEntity<ApiResponseDto<Map<String, List<CourseEntity>>>> listByModule() {
        Map<String, List<CourseEntity>> grouped = courseService.listByModule();
        return ResponseEntity.ok(ApiResponseDto.success("Cursos agrupados por módulo", grouped, 200));
    }

    // Crear un nuevo curso
    @PostMapping
    public ResponseEntity<ApiResponseDto<CourseEntity>> create(@RequestBody CourseEntity course) {
        CourseEntity saved = courseService.create(course);
        return ResponseEntity.ok(ApiResponseDto.success("Curso creado con éxito", saved, 200));
    }

    // Actualizar un curso existente
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CourseEntity>> update(
            @PathVariable Long id,
            @RequestBody CourseEntity course) {
        CourseEntity updated = courseService.update(id, course);
        return ResponseEntity.ok(ApiResponseDto.success("Curso actualizado con éxito", updated, 200));
    }

    // Activar/Desactivar un curso (soft delete)
    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CourseEntity>> softDelete(@PathVariable Long id) {
        CourseEntity updated = courseService.softDelete(id);
        return ResponseEntity.ok(
                ApiResponseDto.success("Estado del curso actualizado (activo/inactivo)", updated, 200));
    }

    // Obtener un curso por ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponseDto<CourseEntity>> getById(@PathVariable Long id) {
        CourseEntity course = courseService.getById(id);
        return ResponseEntity.ok(ApiResponseDto.success("Curso obtenido con éxito", course, 200));
    }

    @GetMapping("/modules")
    public ResponseEntity<ApiResponseDto<List<String>>> getModules() {
        List<String> modules = Arrays.stream(ModuleEnum.values())
                .map(ModuleEnum::getDisplayName)
                .collect(Collectors.toList());
        return ResponseEntity.ok(ApiResponseDto.success("Módulos disponibles", modules, 200));
    }

}
