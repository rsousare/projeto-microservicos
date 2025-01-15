package com.example.Project.repository;

import com.example.Project.entity.Project;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT t.areaId, COUNT(t.id) FROM Project t GROUP BY t.areaId")
    List<Object[]>findProjectCountByArea();
}

