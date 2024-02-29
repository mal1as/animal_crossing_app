package com.itmo.coursework.service;

import com.itmo.coursework.model.Characteristic;
import com.itmo.coursework.repository.CharacteristicRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class CharacteristicService {

    @Autowired
    private CharacteristicRepository repository;

    public List<Characteristic> getAll() {
        return repository.findAll();
    }

    public Optional<Characteristic> getById(Long id) {
        return repository.findById(id);
    }

    public Characteristic upsert(Characteristic characteristic) {
        return repository.save(characteristic);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
