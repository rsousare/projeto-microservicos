package com.example.Project.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "project")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Project {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(updatable = false)
    private long id;

    @NotBlank(message = "The name cannot be empty")
    @Size(max = 50, message = "Max 50 characters")
    @Column(length = 50, nullable = false)
    private String name;

    @Column(length = 50, nullable = false)
    private String startDate;

    @Column(length = 20, nullable = false)
    private String endDate;

    private Long areaId;
}
