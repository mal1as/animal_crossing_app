package com.itmo.coursework.repository;

import com.itmo.coursework.model.Climate;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClimateRepository extends JpaRepository<Climate, Long> {
}
