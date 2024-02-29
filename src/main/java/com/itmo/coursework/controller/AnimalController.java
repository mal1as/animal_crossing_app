package com.itmo.coursework.controller;

import com.itmo.coursework.dto.*;
import com.itmo.coursework.model.Animal;
import com.itmo.coursework.service.AnimalClassService;
import com.itmo.coursework.service.AnimalService;
import com.itmo.coursework.service.ClimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/api/v1/animal")
public class AnimalController {

    @Autowired
    private AnimalService animalService;
    @Autowired
    private AnimalClassService animalClassService;
    @Autowired
    private ClimateService climateService;


    @GetMapping
    public ResponseEntity<List<Animal>> getAllAnimals() {
        return ResponseEntity.ok(animalService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Animal> getAnimalById(@PathVariable Long id) {
        return animalService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Animal> upsertAnimal(@RequestBody Animal animal) {
        return ResponseEntity.ok(animalService.upsert(animal));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteAnimal(@PathVariable Long id) {
        animalService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/byClass/{animalClassId}")
    public ResponseEntity<List<Animal>> getAnimalsByAnimalClass(@PathVariable Long animalClassId) {
        return ResponseEntity.ok(animalService.getAnimalsByClass(animalClassId));
    }

    @GetMapping("/withCharacteristics")
    public ResponseEntity<List<AnimalWithCharacteristicsDTO>> getAnimalsWithCharacteristics() {
        return ResponseEntity.ok(animalService.getAnimalsWithCharacteristics());
    }

    @GetMapping("/climatesByReport")
    public ResponseEntity<List<BestClimateByReportsDTO>> getClimatesByReport(@RequestParam Long animalId) {
        return ResponseEntity.ok(animalService.getClimatesByReport(animalId));
    }
}
