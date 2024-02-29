package com.itmo.coursework.repository;

import com.itmo.coursework.model.AnimalClass;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalClassRepository extends JpaRepository<AnimalClass, Long> {
}
