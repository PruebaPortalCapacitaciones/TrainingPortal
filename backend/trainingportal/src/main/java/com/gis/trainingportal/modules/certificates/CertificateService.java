package com.gis.trainingportal.modules.certificates;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.gis.trainingportal.modules.course.CourseEntity;
import com.gis.trainingportal.modules.enrollment.EnrollmentEntity;
import com.gis.trainingportal.modules.enrollment.EnrollmentRepo;
import com.gis.trainingportal.modules.enrollment.StatusEnum;
import com.gis.trainingportal.modules.user.UserEntity;
import com.gis.trainingportal.modules.user.UserRepo;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.colors.DeviceRgb;
import com.itextpdf.kernel.geom.PageSize;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.element.Image;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Text;
import com.itextpdf.layout.properties.TextAlignment;

/* Servicio para generar certificados en formato PDF */
@Service
public class CertificateService {
    private final UserRepo userRepo;
    private final EnrollmentRepo enrollmentRepo;

    private final DeviceRgb PRIMARY_DARK = new DeviceRgb(0, 128, 128); // #008080
    private final DeviceRgb MINT_DARK = new DeviceRgb(70, 130, 180); // #4682B4
    private final DeviceRgb TEXT_DARK = new DeviceRgb(44, 62, 80); // #2C3E50

    public CertificateService(UserRepo userRepo, EnrollmentRepo enrollmentRepo) {
        this.userRepo = userRepo;
        this.enrollmentRepo = enrollmentRepo;
    }

    public byte[] generateCertificate(String userDocument, Long courseId) {
        // Obtener información del usuario
        UserEntity user = userRepo.findByDocument(userDocument)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));

        // Obtener inscripción del usuario en el curso
        EnrollmentEntity enrollment = enrollmentRepo.findByUserIdAndCourseId(user.getId(), courseId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Inscripción no encontrada"));

        // Validar estado de la inscripción
        if (enrollment.getStatus() != StatusEnum.COMPLETADO) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST,
                    "El curso debe estar completado para generar el certificado");
        }

        CourseEntity course = enrollment.getCourse();

        // Generar PDF usando iText
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            PdfWriter writer = new PdfWriter(baos);
            PdfDocument pdf = new PdfDocument(writer);
            Document document = new Document(pdf, PageSize.A4.rotate());

            try {
                InputStream logoStream = new ClassPathResource("static/logo.png").getInputStream();
                byte[] logoBytes = logoStream.readAllBytes();
                Image logo = new Image(ImageDataFactory.create(logoBytes))
                        .setWidth(70)
                        .setHeight(70)
                        .setTextAlignment(TextAlignment.CENTER)
                        .setMarginBottom(20);
                document.add(logo);
            } catch (Exception e) {
                System.out.println("Logo no encontrado, continuando sin logo");
            }

            // Titulo del certificado
            Paragraph title = new Paragraph()
                    .add(new Text("TRAINING PORTAL\n")
                            .setFontSize(30)
                            .setBold()
                            .setFontColor(PRIMARY_DARK))
                    .add(new Text("CERTIFICADO DE FINALIZACIÓN")
                            .setFontSize(20)
                            .setFontColor(TEXT_DARK))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setMarginBottom(20);
            document.add(title);

            // Texto introductorio
            document.add(new Paragraph("Se otorga el presente certificado a:")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(15)
                    .setFontColor(TEXT_DARK)
                    .setMarginBottom(5));

            // Nombre del usuario
            Paragraph name = new Paragraph(user.getName())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(25)
                    .setBold()
                    .setFontColor(PRIMARY_DARK)
                    .setMarginBottom(30);
            document.add(name);

            // Texto del curso
            document.add(new Paragraph("Por haber completado satisfactoriamente el curso:")
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(14)
                    .setFontColor(TEXT_DARK)
                    .setMarginBottom(5));

            // Nombre del curso
            Paragraph courseName = new Paragraph(course.getTitle())
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(16)
                    .setBold()
                    .setFontColor(MINT_DARK)
                    .setMarginBottom(5);
            document.add(courseName);

            // Detalles del curso
            Paragraph details = new Paragraph()
                    .add(new Text("Módulo: ").setBold())
                    .add(new Text(course.getModule().getDisplayName() + "\n"))
                    .add(new Text("Nivel: ").setBold())
                    .add(new Text(course.getLevel().getDisplayName()
                            + "\n"))
                    .add(new Text("Duración: ").setBold())
                    .add(new Text(course.getDurationHours() + " horas"))
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(10)
                    .setFontColor(TEXT_DARK)
                    .setMarginBottom(20);
            document.add(details);

            // Fecha
            String fecha = LocalDate.now().format(DateTimeFormatter.ofPattern("dd 'de' MMMM 'de' yyyy"));
            document.add(new Paragraph("Expedido el " + fecha)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(8)
                    .setFontColor(TEXT_DARK)
                    .setMarginBottom(5));

            // Número de certificado
            String certificadoId = "CERT-" + courseId + "-" + user.getId() + "-" + LocalDate.now().getYear();
            document.add(new Paragraph("Certificado N°: " + certificadoId)
                    .setTextAlignment(TextAlignment.CENTER)
                    .setFontSize(7)
                    .setFontColor(ColorConstants.LIGHT_GRAY)
                    .setMarginTop(5));

            document.close();
            return baos.toByteArray();

        } catch (Exception e) {
            throw new RuntimeException("Error generando certificado", e);
        }
    }
}