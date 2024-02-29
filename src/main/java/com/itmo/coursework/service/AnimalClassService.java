package com.itmo.coursework.service;

import com.itmo.coursework.model.AnimalClass;
import com.itmo.coursework.repository.AnimalClassRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class AnimalClassService {

    @Autowired
    private AnimalClassRepository repository;

    public List<AnimalClass> getAll() {
        return repository.findAll();
    }

    public Optional<AnimalClass> getById(Long id) {
        return repository.findById(id);
    }

    public AnimalClass upsert(AnimalClass animalClass) {
        return repository.save(animalClass);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
