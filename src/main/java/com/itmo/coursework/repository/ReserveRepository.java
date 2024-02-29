package com.itmo.coursework.repository;

import com.itmo.coursework.model.Reserve;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveRepository extends JpaRepository<Reserve, Long> {
}
