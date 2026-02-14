package com.gis.trainingportal.modules.enrollment;

import com.gis.trainingportal.modules.course.CourseEntity;
import com.gis.trainingportal.modules.user.UserEntity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/* Entidad JPA para las inscripciones de los usuarios en los cursos. */
@Entity
@Table(name = "enrollment", schema = "portal")
@Getter
@Setter
public class EnrollmentEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "user_id", nullable = false)
    private UserEntity user;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "course_id", nullable = false)
    private CourseEntity course;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private StatusEnum status = StatusEnum.INSCRITO;

    @Column(nullable = false)
    private Integer progress = 0;

    /* Método para actualizar el estado de la inscripción basado en el progreso. */
    public void updateStatusByProgress() {
        if (progress == 100) {
            this.status = StatusEnum.COMPLETADO;
        } else if (progress > 0 && progress < 20) {
            this.status = StatusEnum.INICIADO;
        } else if (progress >= 20 && progress < 100) {
            this.status = StatusEnum.EN_PROGRESO;
        } else {
            this.status = StatusEnum.INSCRITO;
        }
    }

    public String getStatusDisplayName() {
        return status != null ? status.getDisplayName() : null;
    }

}
