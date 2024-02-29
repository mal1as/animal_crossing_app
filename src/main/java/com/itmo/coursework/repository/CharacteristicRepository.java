package com.itmo.coursework.repository;

import com.itmo.coursework.model.Characteristic;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CharacteristicRepository extends JpaRepository<Characteristic, Long> {
}