package com.itmo.coursework.service;

import com.itmo.coursework.dto.AvailableWorkerDTO;
import com.itmo.coursework.dto.ReserveClimateForAnimalInfoDTO;
import com.itmo.coursework.model.Animal;
import com.itmo.coursework.model.Reserve;
import com.itmo.coursework.model.User;
import com.itmo.coursework.model.association.AnimalReserve;
import com.itmo.coursework.model.association.ReserveWorker;
import com.itmo.coursework.model.association.id.ReserveWorkerId;
import com.itmo.coursework.repository.*;
import com.itmo.coursework.utils.FunctionSQLExecutionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReserveService {

    @Autowired
    private ReserveRepository repository;
    @Autowired
    private ReserveWorkerRepository reserveWorkerRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;
    @Autowired
    private AnimalReserveRepository animalReserveRepository;
    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Reserve> getAll() {
        return repository.findAll();
    }

    public Optional<Reserve> getById(Long id) {
        return repository.findById(id);
    }

    public Reserve upsert(Reserve reserve) {
        return repository.save(reserve);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<ReserveClimateForAnimalInfoDTO> getReservesForAnimalByName(Long animalId) {
        return FunctionSQLExecutionUtils.getReservesForAnimalByName(jdbcTemplate, animalId);
    }

    public List<ReserveClimateForAnimalInfoDTO> getReservesForAnimalByTemp(Long animalId) {
        return FunctionSQLExecutionUtils.getReservesForAnimalByTemp(jdbcTemplate, animalId);
    }

    public Integer getReserveAvailableLoad(Long reserveId) {
        return FunctionSQLExecutionUtils.getReserveAvailableLoad(jdbcTemplate, reserveId);
    }

    public List<AvailableWorkerDTO> getAvailableWorkers() {
        return FunctionSQLExecutionUtils.getAvailableWorkers(jdbcTemplate);
    }

    public void moveWorkerToReserve(Long userId, Long oldReserveId, Long newReserveId) {
        if(Objects.nonNull(oldReserveId)) {
            ReserveWorkerId reserveWorkerId = new ReserveWorkerId();
            reserveWorkerId.setWorkerId(userId);
            reserveWorkerId.setReserveId(oldReserveId);
            reserveWorkerRepository.deleteById(reserveWorkerId);
        }

        User worker = new User();
        worker.setId(userId);
        Reserve reserve = new Reserve();
        reserve.setId(newReserveId);
        ReserveWorker reserveWorker = new ReserveWorker();
        reserveWorker.setWorker(worker);
        reserveWorker.setReserve(reserve);
        reserveWorker.setAvailableLoad(calculateAvailableLoad(userId));
        reserveWorkerRepository.save(reserveWorker);
    }

    public void moveAnimalToReserve(Long animalId, Long reserveId) {
        Animal animal = new Animal();
        animal.setId(animalId);
        Reserve reserve = new Reserve();
        reserve.setId(reserveId);
        AnimalReserve animalReserve = new AnimalReserve();
        animalReserve.setAnimal(animal);
        animalReserve.setReserve(reserve);
        animalReserveRepository.save(animalReserve);
    }

    public List<Reserve> getReserveAnimalIn(Long animalId) {
        Animal animal = new Animal();
        animal.setId(animalId);
        return animalReserveRepository.findByAnimal(animal).stream().map(AnimalReserve::getReserve).collect(Collectors.toList());
    }

    private Integer calculateAvailableLoad(Long userId) {
        User user = new User();
        user.setId(userId);
        List<String> roles = StreamSupport.stream(userRoleRepository.findUserRoleByUser(user).spliterator(), false)
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toList());
        return roles.contains("MANAGER") ? 5 : (roles.contains("RESERVE_WORKER") ? 3 : 0);
    }
}
