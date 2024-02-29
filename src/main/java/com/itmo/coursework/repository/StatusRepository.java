package com.itmo.coursework.repository;

import com.itmo.coursework.model.Status;
import org.springframework.data.jpa.repository.JpaRepository;

public interface StatusRepository extends JpaRepository<Status, Long> {
}
