package com.example.Project.rec;


import com.example.Project.dto.ProjectDTO;

public record ProjectDetails(Long id, String name, String startDate, String endDate) {
        public ProjectDetails(ProjectDTO project) {
        this(project.getId(), project.getName(), project.getStartDate(), project.getEndDate());
    }
}
