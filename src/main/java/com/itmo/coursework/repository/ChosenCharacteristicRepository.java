package com.itmo.coursework.repository;

import com.itmo.coursework.model.Experiment;
import com.itmo.coursework.model.association.ChosenCharacteristic;
import com.itmo.coursework.model.association.id.ChosenCharacteristicId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ChosenCharacteristicRepository extends JpaRepository<ChosenCharacteristic, ChosenCharacteristicId> {

    Iterable<ChosenCharacteristic> findByExperiment(Experiment experiment);
}
