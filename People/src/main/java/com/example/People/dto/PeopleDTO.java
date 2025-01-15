package com.example.People.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PeopleDTO {
    private Long id;
    private String name;
    private String email;
    private Area area;
}
