package com.gis.trainingportal.modules.reporting;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.gis.trainingportal.modules.course.CourseRepo;
import com.gis.trainingportal.modules.enrollment.EnrollmentRepo;
import com.gis.trainingportal.modules.enrollment.StatusEnum;
import com.gis.trainingportal.modules.user.UserRepo;

/* Servicio para manejar la lógica relacionada con los reportes y estadísticas del portal. */
@Service
public class ReportingService {
    private final UserRepo userRepo;
    private final CourseRepo courseRepo;
    private final EnrollmentRepo enrollmentRepo;

    public ReportingService(UserRepo userRepo, CourseRepo courseRepo, EnrollmentRepo enrollmentRepo) {
        this.userRepo = userRepo;
        this.courseRepo = courseRepo;
        this.enrollmentRepo = enrollmentRepo;
    }

    /* Método para obtener estadísticas generales de administración. */
    public Map<String, Object> getAdminDashboardStats() {
        Map<String, Object> stats = new HashMap<>();

        stats.put("totalUsers", userRepo.count());
        stats.put("totalCourses", courseRepo.count());
        stats.put("activeCourses", courseRepo.countByActiveTrue());
        stats.put("inactiveCourses", courseRepo.countByActiveFalse());
        stats.put("totalEnrollments", enrollmentRepo.count());

        Map<String, Long> enrollmentsByStatus = new HashMap<>();
        enrollmentsByStatus.put("inscritos", enrollmentRepo.countByStatus(StatusEnum.INSCRITO));
        enrollmentsByStatus.put("iniciados", enrollmentRepo.countByStatus(StatusEnum.INICIADO));
        enrollmentsByStatus.put("enProgreso", enrollmentRepo.countByStatus(StatusEnum.EN_PROGRESO));
        enrollmentsByStatus.put("completados", enrollmentRepo.countByStatus(StatusEnum.COMPLETADO));

        stats.put("enrollmentsByStatus", enrollmentsByStatus);

        List<Object[]> topCoursesRaw = enrollmentRepo.findTopCourses();
        List<Map<String, Object>> topCourses = topCoursesRaw.stream()
                .limit(5)
                .map(row -> {
                    Map<String, Object> course = new HashMap<>();
                    course.put("title", row[0]);
                    course.put("enrollments", row[1]);
                    return course;
                })
                .collect(Collectors.toList());

        stats.put("topCourses", topCourses);

        return stats;
    }
}