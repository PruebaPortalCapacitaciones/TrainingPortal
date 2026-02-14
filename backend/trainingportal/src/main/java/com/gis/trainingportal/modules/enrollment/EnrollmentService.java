package com.gis.trainingportal.modules.enrollment;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gis.trainingportal.modules.course.CourseEntity;
import com.gis.trainingportal.modules.course.CourseRepo;
import com.gis.trainingportal.modules.user.UserEntity;
import com.gis.trainingportal.modules.user.UserRepo;

/* Servicio para manejar la lógica relacionada con las inscripciones de los usuarios en los cursos. */
@Service
public class EnrollmentService {

    private final EnrollmentRepo enrollmentRepo;
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    private final Random random = new Random();

    public EnrollmentService(EnrollmentRepo enrollmentRepo, UserRepo userRepo, CourseRepo courseRepo) {
        this.enrollmentRepo = enrollmentRepo;
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
    }

    /* Método para obtener un usuario por su documento, con validación. */
    public UserEntity getUserByDocument(String document) {
        return userRepo.findByDocument(document)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Usuario no encontrado"));
    }

    /* Método para inscribir a un usuario en un curso. */
    public EnrollmentEntity enrollUser(String userDocument, Long courseId) {
        UserEntity user = getUserByDocument(userDocument);

        CourseEntity course = courseRepo.findById(courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Curso no encontrado"));

        // Verificar si ya está inscrito
        if (enrollmentRepo.existsByUserIdAndCourseId(user.getId(), courseId)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "El usuario ya está inscrito en este curso");
        }

        EnrollmentEntity enrollment = new EnrollmentEntity();
        enrollment.setUser(user);
        enrollment.setCourse(course);
        enrollment.setStatus(StatusEnum.INSCRITO);
        enrollment.setProgress(0);

        return enrollmentRepo.save(enrollment);
    }

    /*
     * Método para eliminar la inscripción de un usuario en un curso no completado.
     */
    public void removeCourse(String userDocument, Long courseId) {
        UserEntity user = getUserByDocument(userDocument);

        EnrollmentEntity enrollment = enrollmentRepo.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Inscripción no encontrada"));

        if (enrollment.getStatus() == StatusEnum.COMPLETADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "No se puede eliminar un curso ya completado");
        }

        enrollmentRepo.delete(enrollment);
    }

    /*
     * Método para avanzar el progreso de un curso aleatorio entre 15% y 30%.
     */
    public EnrollmentEntity advanceProgress(String userDocument, Long courseId) {
        UserEntity user = getUserByDocument(userDocument);

        EnrollmentEntity enrollment = enrollmentRepo.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND,
                        "Inscripción no encontrada"));

        if (enrollment.getStatus() == StatusEnum.COMPLETADO) {
            throw new ResponseStatusException(HttpStatus.CONFLICT,
                    "El curso ya está completado");
        }

        int advance = random.nextInt(16) + 15;
        int newProgress = enrollment.getProgress() + advance;

        if (newProgress >= 100) {
            newProgress = 100;
            enrollment.setStatus(StatusEnum.COMPLETADO);
        }

        enrollment.setProgress(newProgress);

        // Actualizar estado según el nuevo progreso
        enrollment.updateStatusByProgress();

        return enrollmentRepo.save(enrollment);
    }

    /*
     * Método para obtener los cursos inscritos de un usuario.
     */
    public List<Map<String, Object>> getUserCourses(String userDocument) {
        UserEntity user = getUserByDocument(userDocument);

        List<EnrollmentEntity> enrollments = enrollmentRepo.findByUserId(user.getId());

        return enrollments.stream()
                .map(enrollment -> {
                    CourseEntity course = enrollment.getCourse();

                    Map<String, Object> courseMap = new HashMap<>();
                    courseMap.put("enrollmentId", enrollment.getId());
                    courseMap.put("courseId", course.getId());
                    courseMap.put("title", course.getTitle());
                    courseMap.put("module", course.getModule() != null ? course.getModule().getDisplayName() : null);
                    courseMap.put("level", course.getLevel() != null ? course.getLevel().getDisplayName() : null);
                    courseMap.put("status",
                            enrollment.getStatus() != null ? enrollment.getStatus().getDisplayName() : null);
                    courseMap.put("progress", enrollment.getProgress());
                    courseMap.put("active", course.getActive());

                    return courseMap;
                })
                .collect(Collectors.toList());
    }

}
