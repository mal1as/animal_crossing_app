package com.itmo.coursework.repository;

import com.itmo.coursework.model.Experiment;
import com.itmo.coursework.model.association.AnimalInExperiment;
import com.itmo.coursework.model.association.id.AnimalInExperimentId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalInExperimentRepository extends JpaRepository<AnimalInExperiment, AnimalInExperimentId> {

    Iterable<AnimalInExperiment> findByExperiment(Experiment experiment);
}
