package com.itmo.coursework.controller;

import com.itmo.coursework.model.Climate;
import com.itmo.coursework.service.ClimateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/api/v1/climate")
public class ClimateController {

    @Autowired
    private ClimateService climateService;

    @GetMapping
    public ResponseEntity<List<Climate>> getAllClimates() {
        return ResponseEntity.ok(climateService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Climate> getClimateById(@PathVariable Long id) {
        return climateService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Climate> upsertClimate(@RequestBody Climate climate) {
        return ResponseEntity.ok(climateService.upsert(climate));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteClimate(@PathVariable Long id) {
        climateService.delete(id);
        return ResponseEntity.ok().build();
    }
}
