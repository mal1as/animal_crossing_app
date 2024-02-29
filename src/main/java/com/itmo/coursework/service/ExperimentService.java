package com.itmo.coursework.service;

import com.itmo.coursework.dto.*;
import com.itmo.coursework.exception.CourseworkIllegalArgumentException;
import com.itmo.coursework.model.*;
import com.itmo.coursework.model.association.AnimalCharacteristic;
import com.itmo.coursework.model.association.AnimalInExperiment;
import com.itmo.coursework.model.association.ChosenCharacteristic;
import com.itmo.coursework.repository.*;
import com.itmo.coursework.security.CustomUserDetails;
import com.itmo.coursework.utils.FunctionSQLExecutionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ExperimentService {

    @Autowired
    private ExperimentRepository repository;
    @Autowired
    private AnimalInExperimentRepository animalInExperimentRepository;
    @Autowired
    private ChosenCharacteristicRepository chosenCharacteristicRepository;
    @Autowired
    private AnimalRepository animalRepository;
    @Autowired
    private StatusRepository statusRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private AnimalCharacteristicRepository animalCharacteristicRepository;

    @Autowired
    private NamedParameterJdbcTemplate jdbcTemplate;


    public Experiment createEmptyExperiment() {
        Long experimenterId = ((CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).getUser().getId();
        User experimenter = new User();
        experimenter.setId(experimenterId);

        Experiment experiment = new Experiment();
        experiment.setStartDate(LocalDate.now());
        experiment.setExperimenter(experimenter);
        return repository.save(experiment);
    }

    public ValidationResultDTO checkAnimalsForExperiment(Integer[] animalIds) {
        return FunctionSQLExecutionUtils.checkAnimalsForExperiment(jdbcTemplate, animalIds);
    }

    public void createAnimalInExperiment(Long experimentId, List<Long> animalIds) {
        Experiment experiment = new Experiment();
        experiment.setId(experimentId);

        animalIds.forEach(animalId -> {
            Animal animal = new Animal();
            animal.setId(animalId);
            AnimalInExperiment animalInExperiment = new AnimalInExperiment();
            animalInExperiment.setExperiment(experiment);
            animalInExperiment.setAnimal(animal);
            animalInExperimentRepository.save(animalInExperiment);
        });
    }

    public List<AvailableToChooseCharacteristicDTO> getAvailableCharacteristics(Long experimentId) {
        return FunctionSQLExecutionUtils.getAvailableToChooseCharacteristics(jdbcTemplate, experimentId);
    }

    public List<ConflictedCharacteristicDTO> getConflictedCharacteristics(Integer[] characteristicIds) {
        return FunctionSQLExecutionUtils.getConflictedCharacteristics(jdbcTemplate, characteristicIds);
    }

    public void createChosenCharacteristic(Long experimentId, List<Long> characteristicIds) {
        Experiment experiment = new Experiment();
        experiment.setId(experimentId);

        characteristicIds.forEach(characteristicId -> {
            Characteristic characteristic = new Characteristic();
            characteristic.setId(characteristicId);
            ChosenCharacteristic chosenCharacteristic = new ChosenCharacteristic();
            chosenCharacteristic.setExperiment(experiment);
            chosenCharacteristic.setCharacteristic(characteristic);
            chosenCharacteristicRepository.save(chosenCharacteristic);
        });
    }

    public ExecuteExperimentResultDTO executeExperiment(Long experimentId) {
        double successProbability = FunctionSQLExecutionUtils.calculateSuccessProbability(jdbcTemplate, experimentId);
        double mutationProbability = FunctionSQLExecutionUtils.calculateMutationProbability(jdbcTemplate, experimentId);
        long statusId = calculateExperimentResult(successProbability, mutationProbability);
        saveExperimentStatus(experimentId, statusId);

        if(statusId == 1) return postProcessExperimentSuccess(experimentId);
        if(statusId == 2) return postProcessExperimentFailure(experimentId);
        return postProcessExperimentMutation(experimentId);
    }

    public List<Experiment> getExperimentHistory(ExperimentHistoryParamsDTO params) {
        // hack for problem with date in js...
        params.setStartDate(params.getStartDate() == null ? null : params.getStartDate().plusDays(1));
        params.setEndDate(params.getEndDate() == null ? null : params.getEndDate().plusDays(1));

        List<ExperimentHistoryDTO> experimentHistory = FunctionSQLExecutionUtils.getExperimentHistory(jdbcTemplate, params.getStatusId(), params.getStartDate(), params.getEndDate(),
                params.getAnimalIds() == null ? null : params.getAnimalIds().toArray(new Integer[0]), params.getExperimenterId());
        return experimentHistory.stream().map(experimentHistoryDTO -> {
            Experiment experiment = new Experiment();
            experiment.setId(experimentHistoryDTO.getExperimentId());
            experiment.setStatus(statusRepository.findById(experimentHistoryDTO.getStatusId()).orElse(null));
            experiment.setStartDate(experimentHistoryDTO.getExperimentDate());
            experiment.setCreatedAnimal(animalRepository.findById(experimentHistoryDTO.getCreatedAnimalId()).orElse(null));
            experiment.setExperimenter(userRepository.findById(experimentHistoryDTO.getExperimenterId()).orElse(null));
            return experiment;
        }).collect(Collectors.toList());
    }

    private ExecuteExperimentResultDTO postProcessExperimentMutation(Long experimentId) {
        Animal animal = new Animal();
        animal.setName(FunctionSQLExecutionUtils.generateMutatedAnimalName(jdbcTemplate, experimentId));
        animal.setArtificial(true);
        animal.setAnimalClass(findAnimalClassByExperiment(experimentId));
        Long animalId = animalRepository.save(animal).getId();
        saveCreatedAnimal(experimentId, animalId);
        createMutationCharacteristics(experimentId, animalId);
        return ExecuteExperimentResultDTO.builder()
                .status(statusRepository.findById(3L).orElseThrow(CourseworkIllegalArgumentException::new))
                .animal(animalRepository.findById(animalId).orElseThrow(CourseworkIllegalArgumentException::new))
                .build();
    }

    private ExecuteExperimentResultDTO postProcessExperimentFailure(Long experimentId) {
        return ExecuteExperimentResultDTO.builder()
                .status(statusRepository.findById(2L).orElseThrow(CourseworkIllegalArgumentException::new))
                .build();
    }

    private ExecuteExperimentResultDTO postProcessExperimentSuccess(Long experimentId) {
        Animal animal = new Animal();
        animal.setName(FunctionSQLExecutionUtils.generateDefaultAnimalName(jdbcTemplate, experimentId));
        animal.setArtificial(true);
        animal.setAnimalClass(findAnimalClassByExperiment(experimentId));
        Long animalId = animalRepository.save(animal).getId();
        saveCreatedAnimal(experimentId, animalId);
        copyChosenCharacteristics(experimentId, animalId);
        return ExecuteExperimentResultDTO.builder()
                .status(statusRepository.findById(1L).orElseThrow(CourseworkIllegalArgumentException::new))
                .animal(animalRepository.findById(animalId).orElseThrow(CourseworkIllegalArgumentException::new))
                .build();
    }

    private void createMutationCharacteristics(Long experimentId, Long animalId) {
        Animal animal = new Animal();
        animal.setId(animalId);
        FunctionSQLExecutionUtils.generateMutationCharacteristics(jdbcTemplate, experimentId).forEach(characteristicId -> {
            // don't check conflicted characteristics, just insert them, trigger will check it
            try {
                Characteristic characteristic = new Characteristic();
                characteristic.setId(characteristicId.longValue());
                AnimalCharacteristic animalCharacteristic = new AnimalCharacteristic();
                animalCharacteristic.setAnimal(animal);
                animalCharacteristic.setCharacteristic(characteristic);
                animalCharacteristicRepository.save(animalCharacteristic);
            } catch (Exception e) {
                // ignore conflicted characteristic insert
            }
        });
    }

    private void copyChosenCharacteristics(Long experimentId, Long animalId) {
        Animal animal = new Animal();
        animal.setId(animalId);
        Experiment experiment = new Experiment();
        experiment.setId(experimentId);
        chosenCharacteristicRepository.findByExperiment(experiment).forEach(chosenCharacteristic -> {
            AnimalCharacteristic animalCharacteristic = new AnimalCharacteristic();
            animalCharacteristic.setAnimal(animal);
            animalCharacteristic.setCharacteristic(chosenCharacteristic.getCharacteristic());
            animalCharacteristicRepository.save(animalCharacteristic);
        });
    }

    private AnimalClass findAnimalClassByExperiment(Long experimentId) {
        Experiment experiment = new Experiment();
        experiment.setId(experimentId);
        AnimalInExperiment animalInExperiment = StreamSupport.stream(animalInExperimentRepository.findByExperiment(experiment).spliterator(), false)
                .findFirst().orElseThrow(CourseworkIllegalArgumentException::new);
        return animalInExperiment.getAnimal().getAnimalClass();
    }

    private void saveCreatedAnimal(Long experimentId, Long animalId) {
        Animal animal = new Animal();
        animal.setId(animalId);
        Experiment experiment = repository.findById(experimentId).orElseThrow(CourseworkIllegalArgumentException::new);
        experiment.setCreatedAnimal(animal);
        repository.save(experiment);
    }

    private void saveExperimentStatus(Long experimentId, Long statusId) {
        Status resultStatus = new Status();
        resultStatus.setId(statusId);
        Experiment experiment = repository.findById(experimentId).orElseThrow(CourseworkIllegalArgumentException::new);
        experiment.setStatus(resultStatus);
        repository.save(experiment);
    }

    // return 1 - SUCCESS, 2 - FAILURE, 3 - MUTATION
    private long calculateExperimentResult(double successProb, double mutationProb) {
        if (Math.random() < successProb) return 1;
        if (Math.random() < successProb * mutationProb) return 3;
        return 2;
    }
}
