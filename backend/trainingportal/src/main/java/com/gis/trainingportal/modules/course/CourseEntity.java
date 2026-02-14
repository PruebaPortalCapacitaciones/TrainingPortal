package com.gis.trainingportal.modules.course;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

/* Entidad JPA para los cursos en la base de datos */
@Entity
@Table(name = "course", schema = "portal")
@Getter
@Setter
public class CourseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 50)
    private String title;

    @Column(length = 200)
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 50)
    private ModuleEnum module;

    @Column(nullable = true)
    private Integer durationHours;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private LevelEnum level;

    @Column(nullable = false)
    private Boolean active = true;

    public String getModuleDisplayName() {
        return module != null ? module.getDisplayName() : null;
    }

    public String getLevelDisplayName() {
        return level != null ? level.getDisplayName() : null;
    }

}
