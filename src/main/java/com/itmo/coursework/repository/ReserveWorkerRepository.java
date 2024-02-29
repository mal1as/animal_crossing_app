package com.itmo.coursework.repository;

import com.itmo.coursework.model.User;
import com.itmo.coursework.model.association.ReserveWorker;
import com.itmo.coursework.model.association.id.ReserveWorkerId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReserveWorkerRepository extends JpaRepository<ReserveWorker, ReserveWorkerId> {

    ReserveWorker findByWorker(User worker);
}
