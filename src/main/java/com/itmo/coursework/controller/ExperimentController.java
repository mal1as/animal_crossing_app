package com.itmo.coursework.controller;

import com.itmo.coursework.dto.*;
import com.itmo.coursework.model.Experiment;
import com.itmo.coursework.service.ExperimentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/backend/api/v1/experiment")
public class ExperimentController {

    @Autowired
    private ExperimentService experimentService;

    @GetMapping("/start")
    public ResponseEntity<Long> startExperiment() {
        return ResponseEntity.ok(experimentService.createEmptyExperiment().getId());
    }

    @GetMapping("/selectAnimals")
    public ResponseEntity<?> selectAnimalsForExperiment(@RequestParam Long experimentId, @RequestParam List<Integer> animalIds) {
        ValidationResultDTO validationResultDTO = experimentService.checkAnimalsForExperiment(animalIds.toArray(new Integer[0]));
        if(!validationResultDTO.isResult()) {
            return ResponseEntity.badRequest().body(validationResultDTO.getMessage());
        }
        experimentService.createAnimalInExperiment(experimentId, animalIds.stream().map(Integer::longValue).collect(Collectors.toList()));
        return ResponseEntity.ok().build();
    }

    @GetMapping("/availableCharacteristics")
    public ResponseEntity<List<AvailableToChooseCharacteristicDTO>> getAvailableCharacteristics(@RequestParam Long experimentId) {
        return ResponseEntity.ok(experimentService.getAvailableCharacteristics(experimentId));
    }

    @GetMapping("/conflictedCharacteristics")
    public ResponseEntity<List<ConflictedCharacteristicDTO>> getConflictedCharacteristics(@RequestParam List<Integer> characteristicIds) {
        return ResponseEntity.ok(experimentService.getConflictedCharacteristics(characteristicIds.toArray(new Integer[0])));
    }

    @GetMapping("/chooseCharacteristics")
    public ResponseEntity<?> chooseCharacteristics(@RequestParam Long experimentId, @RequestParam List<Long> characteristicIds) {
        experimentService.createChosenCharacteristic(experimentId, characteristicIds);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/execute")
    public ResponseEntity<ExecuteExperimentResultDTO> executeExperiment(@RequestParam Long experimentId) {
        return ResponseEntity.ok(experimentService.executeExperiment(experimentId));
    }

    @PostMapping("/history")
    public ResponseEntity<List<Experiment>> experimentHistory(@RequestBody ExperimentHistoryParamsDTO params) {
        return ResponseEntity.ok(experimentService.getExperimentHistory(params));
    }
}
