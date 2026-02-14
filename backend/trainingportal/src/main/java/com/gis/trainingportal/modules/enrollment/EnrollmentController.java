package com.gis.trainingportal.modules.enrollment;

import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.gis.trainingportal.common.ApiResponseDto;

import jakarta.servlet.http.HttpServletRequest;

/* Controlador para gestionar las inscripciones de los usuarios en los cursos. */
@RestController
@RequestMapping("/enrollments")
public class EnrollmentController {

    private final EnrollmentService enrollmentService;

    public EnrollmentController(EnrollmentService enrollmentService) {
        this.enrollmentService = enrollmentService;
    }

    /*
     * Método helper para obtener el documento del usuario autenticado desde el
     * request.
     */
    private String getUserDocumentFromRequest(HttpServletRequest request) {
        String userDocument = (String) request.getAttribute("userDocument");
        if (userDocument == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        return userDocument;
    }

    /* Endpoint para inscribir a un usuario en un curso. */
    @PostMapping("/enroll")
    public ResponseEntity<ApiResponseDto<EnrollmentEntity>> enroll(
            @RequestParam Long courseId,
            HttpServletRequest request) {

        String userDocument = getUserDocumentFromRequest(request);
        EnrollmentEntity enrollment = enrollmentService.enrollUser(userDocument, courseId);

        return ResponseEntity.ok(ApiResponseDto.success("Inscripción exitosa", enrollment, 200));
    }

    /*
     * Endpoint para eliminar la inscripción de un usuario en un curso no
     * completado.
     */
    @DeleteMapping("/remove")
    public ResponseEntity<ApiResponseDto<Void>> remove(
            @RequestParam Long courseId,
            HttpServletRequest request) {

        String userDocument = getUserDocumentFromRequest(request);
        enrollmentService.removeCourse(userDocument, courseId);

        return ResponseEntity.ok(ApiResponseDto.success("Curso eliminado correctamente", null, 200));
    }

    /* Endpoint para avanzar el progreso de un curso. */
    @PutMapping("/advance")
    public ResponseEntity<ApiResponseDto<EnrollmentEntity>> advanceProgress(
            @RequestParam Long courseId,
            HttpServletRequest request) {

        String userDocument = getUserDocumentFromRequest(request);
        EnrollmentEntity enrollment = enrollmentService.advanceProgress(userDocument, courseId);

        return ResponseEntity.ok(ApiResponseDto.success("Progreso actualizado", enrollment, 200));
    }

    /* Endpoint para obtener los cursos inscritos de un usuario. */
    @GetMapping("/my-courses")
    public ResponseEntity<ApiResponseDto<List<Map<String, Object>>>> getMyCourses(HttpServletRequest request) {
        String userDocument = getUserDocumentFromRequest(request);
        List<Map<String, Object>> courses = enrollmentService.getUserCourses(userDocument);

        return ResponseEntity.ok(ApiResponseDto.success("Cursos del usuario", courses, 200));
    }

}
