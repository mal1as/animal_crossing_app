package com.itmo.coursework.repository;

import com.itmo.coursework.model.Animal;
import com.itmo.coursework.model.association.AnimalCharacteristic;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalCharacteristicRepository extends JpaRepository<AnimalCharacteristic, Long> {

    List<AnimalCharacteristic> findByAnimal(Animal animal);
}