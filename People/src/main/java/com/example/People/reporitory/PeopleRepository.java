package com.example.People.reporitory;


import com.example.People.entity.People;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PeopleRepository extends JpaRepository<People, Long> {
    List<People> findByName(String name);
}
