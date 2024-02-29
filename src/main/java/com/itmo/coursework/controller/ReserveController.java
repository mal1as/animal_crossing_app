package com.itmo.coursework.controller;

import com.itmo.coursework.dto.AvailableWorkerDTO;
import com.itmo.coursework.dto.ReserveClimateForAnimalInfoDTO;
import com.itmo.coursework.model.Reserve;
import com.itmo.coursework.service.ReserveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/api/v1/reserve")
public class ReserveController {

    @Autowired
    private ReserveService reserveService;

    @GetMapping
    public ResponseEntity<List<Reserve>> getAllReserves() {
        return ResponseEntity.ok(reserveService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reserve> getReserveById(@PathVariable Long id) {
        return reserveService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Reserve> upsertReserve(@RequestBody Reserve reserve) {
        return ResponseEntity.ok(reserveService.upsert(reserve));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteReserve(@PathVariable Long id) {
        reserveService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reservesByName")
    public ResponseEntity<List<ReserveClimateForAnimalInfoDTO>> getReservesForAnimalByName(@RequestParam Long animalId) {
        return ResponseEntity.ok(reserveService.getReservesForAnimalByName(animalId));
    }

    @GetMapping("/reservesByTemp")
    public ResponseEntity<List<ReserveClimateForAnimalInfoDTO>> getReservesForAnimalByTemp(@RequestParam Long animalId) {
        return ResponseEntity.ok(reserveService.getReservesForAnimalByTemp(animalId));
    }

    @GetMapping("/availableLoad")
    public ResponseEntity<Integer> getReserveAvailableLoad(@RequestParam Long id) {
        return ResponseEntity.ok(reserveService.getReserveAvailableLoad(id));
    }

    @GetMapping("/availableWorkers")
    public ResponseEntity<List<AvailableWorkerDTO>> getAvailableWorkers() {
        return ResponseEntity.ok(reserveService.getAvailableWorkers());
    }

    @GetMapping("/moveReserveWorker")
    public ResponseEntity<?> moveWorkerToReserve(@RequestParam Long userId, @RequestParam(required = false) Long oldId, @RequestParam Long newId) {
        reserveService.moveWorkerToReserve(userId, oldId, newId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/moveAnimal")
    public ResponseEntity<?> moveAnimal(@RequestParam Long animalId, @RequestParam Long reserveId) {
        reserveService.moveAnimalToReserve(animalId, reserveId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/reservesAnimalIn")
    public ResponseEntity<List<Reserve>> getReservesAnimalIn(@RequestParam Long animalId) {
        return ResponseEntity.ok(reserveService.getReserveAnimalIn(animalId));
    }
}
