package com.itmo.coursework.repository;

import com.itmo.coursework.model.Animal;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalRepository extends JpaRepository<Animal, Long> {

    List<Animal> findAllByAnimalClassId(Long animalClassId);

}
