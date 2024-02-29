package com.itmo.coursework.service;

import com.itmo.coursework.model.Climate;
import com.itmo.coursework.repository.ClimateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ClimateService {

    @Autowired
    private ClimateRepository repository;

    public List<Climate> getAll() {
        return repository.findAll();
    }

    public Optional<Climate> getById(Long id) {
        return repository.findById(id);
    }

    public Climate upsert(Climate climate) {
        return repository.save(climate);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }
}
