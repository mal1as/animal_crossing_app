package com.itmo.coursework.controller;

import com.itmo.coursework.model.Characteristic;
import com.itmo.coursework.service.CharacteristicService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/backend/api/v1/characteristic")
public class CharacteristicController {

    @Autowired
    private CharacteristicService characteristicService;

    @GetMapping
    public ResponseEntity<List<Characteristic>> getAllCharacteristics() {
        return ResponseEntity.ok(characteristicService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Characteristic> getCharacteristicById(@PathVariable Long id) {
        return characteristicService.getById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Characteristic> upsertCharacteristic(@RequestBody Characteristic characteristic) {
        return ResponseEntity.ok(characteristicService.upsert(characteristic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCharacteristic(@PathVariable Long id) {
        characteristicService.getById(id);
        return ResponseEntity.ok().build();
    }
}
