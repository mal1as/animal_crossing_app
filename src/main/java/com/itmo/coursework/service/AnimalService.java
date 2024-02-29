package com.itmo.coursework.service;

import com.itmo.coursework.dto.AnimalWithCharacteristicsDTO;
import com.itmo.coursework.dto.BestClimateByReportsDTO;
import com.itmo.coursework.model.Animal;
import com.itmo.coursework.model.Characteristic;
import com.itmo.coursework.model.association.AnimalCharacteristic;
import com.itmo.coursework.repository.AnimalCharacteristicRepository;
import com.itmo.coursework.repository.AnimalRepository;
import com.itmo.coursework.utils.FunctionSQLExecutionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    @Autowired
    private AnimalRepository repository;
    @Autowired
    private AnimalCharacteristicRepository animalCharacteristicRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;

    public List<Animal> getAll() {
        return repository.findAll();
    }

    public Optional<Animal> getById(Long id) {
        return repository.findById(id);
    }

    public List<Animal> getAnimalsByClass(Long animalClassId) {
        FunctionSQLExecutionUtils.generateMutationCharacteristics(jdbcTemplate, 1L);
        return repository.findAllByAnimalClassId(animalClassId);
    }

    public Animal upsert(Animal animal) {
        return repository.save(animal);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public List<AnimalWithCharacteristicsDTO> getAnimalsWithCharacteristics() {
        return repository.findAll().stream().map(animal -> {
            List<AnimalCharacteristic> animalCharacteristics = animalCharacteristicRepository.findByAnimal(animal);
            List<Characteristic> characteristics = animalCharacteristics.stream().map(AnimalCharacteristic::getCharacteristic).collect(Collectors.toList());
            return AnimalWithCharacteristicsDTO.builder()
                    .animal(animal)
                    .characteristics(characteristics)
                    .build();
        }).collect(Collectors.toList());
    }

    public List<BestClimateByReportsDTO> getClimatesByReport(Long animalId) {
        return FunctionSQLExecutionUtils.getBestClimatesByReports(jdbcTemplate, animalId);
    }
}
