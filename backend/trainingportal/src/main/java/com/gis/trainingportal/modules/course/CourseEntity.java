package com.gis.trainingportal.modules.course;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

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

    @Column(length = 50)
    private String level;

    @Column(nullable = false)
    private Boolean active = true;

    public String getModuleDisplayName() {
        return module != null ? module.getDisplayName() : null;
    }

}
