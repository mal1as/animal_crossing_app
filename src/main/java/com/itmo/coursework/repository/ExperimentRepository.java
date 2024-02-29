package com.itmo.coursework.repository;

import com.itmo.coursework.model.Experiment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ExperimentRepository extends JpaRepository<Experiment, Long> {
}
