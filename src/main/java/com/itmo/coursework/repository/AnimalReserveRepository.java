package com.itmo.coursework.repository;

import com.itmo.coursework.model.Animal;
import com.itmo.coursework.model.Reserve;
import com.itmo.coursework.model.association.AnimalReserve;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnimalReserveRepository extends JpaRepository<AnimalReserve, Long> {

    Iterable<AnimalReserve> findByReserve(Reserve reserve);

    List<AnimalReserve> findByAnimal(Animal animal);
}
