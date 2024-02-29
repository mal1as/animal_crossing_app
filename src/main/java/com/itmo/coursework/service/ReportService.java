package com.itmo.coursework.service;

import com.itmo.coursework.dto.AvailableToReportAnimalsDTO;
import com.itmo.coursework.dto.ReportHistoryParamsDTO;
import com.itmo.coursework.exception.CourseworkIllegalArgumentException;
import com.itmo.coursework.model.Animal;
import com.itmo.coursework.model.Report;
import com.itmo.coursework.model.Reserve;
import com.itmo.coursework.model.association.AnimalReserve;
import com.itmo.coursework.model.association.ReserveWorker;
import com.itmo.coursework.repository.AnimalReserveRepository;
import com.itmo.coursework.repository.ReportRepository;
import com.itmo.coursework.repository.ReserveWorkerRepository;
import com.itmo.coursework.repository.UserRepository;
import com.itmo.coursework.security.CustomUserDetails;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ReportService {

    @Autowired
    private ReportRepository repository;
    @Autowired
    private AnimalReserveRepository animalReserveRepository;
    @Autowired
    private ReserveWorkerRepository reserveWorkerRepository;
    @Autowired
    private UserRepository userRepository;

    public List<Report> getAll() {
        return repository.findAll();
    }

    public Optional<Report> getById(Long id) {
        return repository.findById(id);
    }

    public Report upsert(Report report) {
        if(Objects.isNull(report.getReporter())) {
            CustomUserDetails userDetails = (CustomUserDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
            report.setReporter(userDetails.getUser());
        }

        // hack for problem with date in js...
        report.setReportDate(report.getReportDate().plusDays(1));

        return repository.save(report);
    }

    public void delete(Long id) {
        repository.deleteById(id);
    }

    public AvailableToReportAnimalsDTO getAvailableToReportAnimals(Long reserveId, String username) {
        Reserve reserve = new Reserve();
        reserve.setId(reserveId);
        if(Objects.isNull(reserveId)) {
            ReserveWorker reserveWorker = reserveWorkerRepository.findByWorker(userRepository.findByUsername(username).orElseThrow(CourseworkIllegalArgumentException::new));
            reserve.setId(reserveWorker.getReserve().getId());
        }

        List<Animal> animals = StreamSupport.stream(animalReserveRepository.findByReserve(reserve).spliterator(), false)
                .map(AnimalReserve::getAnimal).collect(Collectors.toList());
        return AvailableToReportAnimalsDTO.builder()
                .animals(animals).reserve(reserve).build();
    }

    public List<Report> getReportHistory(ReportHistoryParamsDTO params) {
        // hack for problem with date in js...
        params.setStartDate(params.getStartDate() == null ? null : params.getStartDate().plusDays(1));
        params.setEndDate(params.getEndDate() == null ? null : params.getEndDate().plusDays(1));

        // for small data volumes, temporary
        return repository.findAll().stream()
                .filter(report -> Objects.isNull(params.getAnimalId()) || report.getAnimal().getId().equals(params.getAnimalId()))
                .filter(report -> Objects.isNull(params.getReserveId()) || report.getReserve().getId().equals(params.getReserveId()))
                .filter(report -> Objects.isNull(params.getReporterId()) || report.getReporter().getId().equals(params.getReporterId()))
                .filter(report -> (Objects.isNull(params.getMinRate()) || report.getHealthRate() >= params.getMinRate()) && (Objects.isNull(params.getMaxRate()) || report.getHealthRate() <= params.getMaxRate()))
                .filter(report -> (Objects.isNull(params.getStartDate()) || report.getReportDate().isAfter(params.getStartDate())) && (Objects.isNull(params.getEndDate()) || report.getReportDate().isBefore(params.getEndDate())))
                .collect(Collectors.toList());
    }
}
