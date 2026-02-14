package com.gis.trainingportal.modules.reporting;

import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.gis.trainingportal.common.ApiResponseDto;

/* Controlador para gestionar los endpoints de reporting y estadísticas. */
@RestController
@RequestMapping("/reporting")
public class ReportingController {

    private final ReportingService reportingService;

    public ReportingController(ReportingService reportingService) {
        this.reportingService = reportingService;
    }

    /* Endpoint para obtener estadísticas generales. */
    @GetMapping("/stats")
    public ResponseEntity<ApiResponseDto<Map<String, Object>>> getAdminDashboardStats() {
        Map<String, Object> stats = reportingService.getAdminDashboardStats();
        return ResponseEntity.ok(ApiResponseDto.success("Estadísticas del dashboard", stats, 200));
    }

}
