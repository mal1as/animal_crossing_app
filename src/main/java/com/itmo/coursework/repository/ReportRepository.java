package com.itmo.coursework.repository;

import com.itmo.coursework.model.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report, Long> {
}
