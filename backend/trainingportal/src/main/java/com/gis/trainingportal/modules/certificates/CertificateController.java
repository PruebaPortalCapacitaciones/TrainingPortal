package com.gis.trainingportal.modules.certificates;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;

/* Controlador para manejar las solicitudes relacionadas con certificados */
@RestController
@RequestMapping("/certificates")
public class CertificateController {

    private final CertificateService certificateService;

    public CertificateController(CertificateService certificateService) {
        this.certificateService = certificateService;
    }

    /* Método para obtener el documento del usuario autenticado */
    private String getUserDocumentFromRequest(HttpServletRequest request) {
        String userDocument = (String) request.getAttribute("userDocument");
        if (userDocument == null) {
            throw new RuntimeException("Usuario no autenticado");
        }
        return userDocument;
    }

    /* Endpoint para descargar el certificado en formato PDF */
    @GetMapping("/download/{courseId}")
    public ResponseEntity<byte[]> downloadCertificate(
            @PathVariable Long courseId,
            HttpServletRequest request) {

        String userDocument = getUserDocumentFromRequest(request);
        byte[] pdfBytes = certificateService.generateCertificate(userDocument, courseId);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_PDF);
        headers.setContentDispositionFormData("attachment", "certificado-" + courseId + ".pdf");
        headers.setContentLength(pdfBytes.length);

        return ResponseEntity.ok()
                .headers(headers)
                .body(pdfBytes);
    }

}
