package com.gis.trainingportal.modules.course;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository

public interface CourseRepo extends JpaRepository<CourseEntity, Long> {

    List<CourseEntity> findByModule(String module);

}
